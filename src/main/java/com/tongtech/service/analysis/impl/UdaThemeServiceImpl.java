package com.tongtech.service.analysis.impl;


import com.tongtech.common.utils.SysUtils;
import com.tongtech.common.vo.analysis.TemplateVo;
import com.tongtech.common.vo.analysis.ThemeVo;
import com.tongtech.dao.entity.analysis.UdaTheme;
import com.tongtech.dao.repository.analysis.UdaThemeRepository;
import com.tongtech.service.analysis.IUdaThemeDimService;
import com.tongtech.service.analysis.IUdaThemeMeasureService;
import com.tongtech.service.analysis.IUdaThemeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自定义分析专题表 服务实现类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Service
public class UdaThemeServiceImpl  implements IUdaThemeService {

    @Autowired
    UdaThemeRepository udaThemeRepository;

    @Autowired
    private IUdaThemeMeasureService themeMeasureService;

    @Autowired
    private IUdaThemeDimService themeDimService;

    @Override
    public List<UdaTheme> findAllUdaThemes() {
        return udaThemeRepository.findAll();
    }

    @Override
    public List<ThemeVo> buildTheme() {
        List<ThemeVo> result = new ArrayList<>();
        List<UdaTheme> lst = findAllUdaThemes();
        ThemeVo voObj;
        for(UdaTheme obj:lst) {
            voObj = new ThemeVo();
            BeanUtils.copyProperties(obj,voObj, SysUtils.getNullPropertyNames(obj));
            voObj.setThemeId(obj.getThemeId()+"");
            result.add(voObj);
        }
        if (result != null && !result.isEmpty()) {
            themeMeasureService.buildMeasure(result);
            themeDimService.buildDim(result);
        }
        return result;
    }

    @Override
    public UdaTheme findUdaThemeByTemplateVo(TemplateVo templateVo) {
        Integer themeId = 0;
        if(templateVo.getThemeId() != null) {
            themeId = Integer.valueOf(templateVo.getThemeId());
            return udaThemeRepository.findByThemeId(themeId);
        }else{
            return null;
        }

    }
}
