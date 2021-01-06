package com.tongtech.controller;


import com.tongtech.common.utils.JsonResult;
import com.tongtech.common.vo.analysis.TemplateVo;
import com.tongtech.common.vo.analysis.ThemeVo;
import com.tongtech.service.analysis.IUdaTemplateService;
import com.tongtech.service.analysis.IUdaThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AnalysisController {
    @Autowired
    private IUdaThemeService themeService;

    @Autowired
    private IUdaTemplateService udaTemplateService;

    Logger myloger = LoggerFactory.getLogger(getClass().getName());

    // 1.2.5.1	获取专题集合
    @RequestMapping(value = "/api/custom/analysis/gtheme", method = RequestMethod.GET)
    public JsonResult getThemesList() {
        List<ThemeVo> result =themeService.buildTheme();
        return JsonResult.normal(result);
    }
    // 1.2.5.5	查询数据
    @RequestMapping(value = "/api/custom/analysis/query", method = RequestMethod.POST)
    public JsonResult searchData(@RequestBody TemplateVo model, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum) {
        Map result = null;
        try {
          result = udaTemplateService.queryDataByTemplate(model, pageSize, pageNum);
        } catch (Exception e) {
            myloger.warn("查询错误：" + e.getMessage());
            return JsonResult.error("查询错误：" + e.getMessage());
        }
        return JsonResult.normal(result);
    }
    // 1.2.5.4	导出数据
    @RequestMapping(value = "/api/custom/analysis/export", method = RequestMethod.POST)
    public ResponseEntity<Resource> exportDataForTemplate(@RequestBody TemplateVo model) {
        return udaTemplateService.exportDataForTemplate(model);
    }


}
