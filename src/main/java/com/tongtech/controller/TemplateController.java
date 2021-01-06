package com.tongtech.controller;


import com.tongtech.common.utils.JsonResult;
import com.tongtech.common.vo.analysis.*;
import com.tongtech.dao.entity.analysis.*;
import com.tongtech.service.analysis.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TemplateController {

    @Autowired
    private IUdaThemeService iUdaThemeService;

    @Autowired
    private IUdaThemeMeasureService iUdaThemeMeasureService;

    @Autowired
    private IUdaThemeDimService iUdaThemeDimService;

    @Autowired
    private IUdaTemplateService iUdaTemplateService;

    @Autowired
    private IUdaTemplateLevelService iUdaTemplateLevelService;

    @Autowired
    private IUdaTemplateMeasureService iUdaTemplateMeasureService;

    @Autowired
    private IUdaThemeDimLevelService iUdaThemeDimLevelService;

    @Autowired
    private IUdaTemplateLevelSingleService iUdaTemplateLevelSingleService;

    @Autowired
    private IUdaThemeDimLevelSingleService iUdaThemeDimLevelSingleService;

    // 1.2.5.2	获取模板列表
    // api/custom/analysis/gtemplate/{themeId}
    @RequestMapping(value = "api/custom/analysis/gtemplate/{themeId}", method = RequestMethod.GET)
    public JsonResult GetTemplateList(@PathVariable String themeId) {
//        //根据themeId获取相应的模板列表
//        String themeId = model.get("themeId").toString();
        List<TemplateVo> resultList = new ArrayList<>();
        try {
            List<UdaTemplate> udaTemplates = iUdaTemplateService.findByThemeId(Integer.parseInt(themeId));
            if (udaTemplates == null || udaTemplates.size() == 0) {
                return JsonResult.normal("数据库中无相关模板");
            }
            //遍历模板，拼装所需要的返回结果
            for (UdaTemplate udaTemplate : udaTemplates) {
                TemplateVo template = new TemplateVo();
                template.setConditions(udaTemplate.getConditions());
                if (udaTemplate.getHasShare() != null){
                    template.setHasShare(udaTemplate.getHasShare() == 0 ? false : true);
                }
                template.setTemplateId(udaTemplate.getTemplateId() + "");
                template.setTemplateName(udaTemplate.getTemplateName());
                template.setThemeId(udaTemplate.getThemeId() + "");
                //获取每个list当中所需要的Level
                List<UdaTemplateLevel> list1 = iUdaTemplateLevelService.findByTemplateId(udaTemplate.getTemplateId());
                List<LevelVo> filterLevels = new ArrayList<>();
                List<LevelVo> showLevels = new ArrayList<>();
                if (list1 == null || list1.size() == 0) {
                    template.setFilterLevelList(null);
                    template.setFilterLevelList(null);
                } else {
                    LevelVo level = null;
                    for (UdaTemplateLevel udaTemplateLevel : list1) {
                        level = new LevelVo();
                        level.setDimId(udaTemplateLevel.getDimId());
                        level.setLevelId(udaTemplateLevel.getLevelId());
                        level.setLevelName(udaTemplateLevel.getLevelName());
                        level.setHasDict(udaTemplateLevel.getHasDict());
                        ValType[] values = ValType.values();
                        ValType valType = null;
                        for (ValType value : values) {
                            if (value.getCode() == udaTemplateLevel.getValType()) {
                                valType = value;
                            }
                        }
                        level.setValType(udaTemplateLevel.getValType());
                        List<SingleVo> singleVos = new ArrayList<>();
                        List<UdaTemplateLevelSingle> list = iUdaTemplateLevelSingleService.findByDimIdAndGetLevelIdAndTemplatedId(udaTemplateLevel.getDimId(),udaTemplateLevel.getLevelId(),udaTemplateLevel.getTemplateId());
                        SingleVo singleVo = null;
                        if (list != null && list.size() > 0) {
                            for (UdaTemplateLevelSingle udaTemplateLevelSingle : list) {
                                singleVo = new SingleVo();
                                singleVo.setId(udaTemplateLevelSingle.getSingleId());
                                singleVo.setValue(udaTemplateLevelSingle.getSingleValue());
                                singleVos.add(singleVo);
                            }
                        }
                        level.setValList(singleVos);
                        if (udaTemplateLevel.getShowOrFilter().equals("show")) {
                            showLevels.add(level);
                        } else {
                            filterLevels.add(level);
                        }
                    }
                    template.setFilterLevelList(filterLevels);
                    template.setShowLevelList(showLevels);
                }
                //获取每个list所需要的measure
                List<UdaTemplateMeasure> list = iUdaTemplateMeasureService.findByTemplateId(udaTemplate.getTemplateId());
                List<MeasureVo> measures = new ArrayList<>();
                if (list == null || list.size() == 0) {
                    template.setMeasureList(measures);
                } else {
                    MeasureVo measure = null;
                    for (UdaTemplateMeasure udaTemplateMeasure : list) {
                        measure = new MeasureVo();
                        measure.setMeasureId(udaTemplateMeasure.getMeasureId());
                        measure.setMeasureName(udaTemplateMeasure.getMeasureName());
                        measures.add(measure);
                    }
                    template.setMeasureList(measures);
                }

                resultList.add(template);
            }
        }
        catch (Exception e)
        {
            return  JsonResult.error(e.getMessage());
        }
        return JsonResult.normal(resultList);
    }

    // 1.2.5.3	保存模板
    // api/custom/analysis/ptemplate
    @RequestMapping(value = "/api/custom/analysis/ptemplate", method = RequestMethod.POST)
    @Transactional
    public JsonResult SaveTemplateList(@RequestBody TemplateVo templateVo) {

        try {
            String templateName = templateVo.getTemplateName();
            String themeId = templateVo.getThemeId() + "";
            Boolean hasShare = templateVo.getHasShare();
            String condition = templateVo.getConditions();
            List<MeasureVo> measureList = templateVo.getMeasureList();
            List<LevelVo> filterLevelList = templateVo.getFilterLevelList();
            List<LevelVo> showLevelList = templateVo.getShowLevelList();
            //保存模板
            UdaTemplate udaTemplate = new UdaTemplate();
            udaTemplate.setThemeId(Integer.parseInt(themeId));
            udaTemplate.setConditions(condition);
            if (hasShare) {
                udaTemplate.setHasShare(0);
            } else {
                udaTemplate.setHasShare(1);
            }
            udaTemplate.setTemplateName(templateName);
            iUdaTemplateService.save(udaTemplate);
            System.out.println(udaTemplate.getTemplateId());
            //保存指标
            UdaTemplateMeasure udaTemplateMeasure = null;
            if (measureList != null && measureList.size() > 0) {
                List<UdaTemplateMeasure> udaTemplateMeasureList = new ArrayList<>();
                for (MeasureVo udaThemeMeasure : measureList) {
                    udaTemplateMeasure = new UdaTemplateMeasure();
                    udaTemplateMeasure.setTemplateId(udaTemplate.getTemplateId());
                    udaTemplateMeasure.setMeasureId(udaThemeMeasure.getMeasureId());
                    udaTemplateMeasure.setMeasureName(udaThemeMeasure.getMeasureName());
                    udaTemplateMeasureList.add(udaTemplateMeasure);
                }
                iUdaTemplateMeasureService.saveBatch(udaTemplateMeasureList);
            }
            //保存维度
            UdaTemplateLevel udaTemplateLevel = null;
            if (filterLevelList != null && filterLevelList.size() > 0) {
                List<UdaTemplateLevel> udaTemplateLevelList = new ArrayList<>();
                for (LevelVo udaThemeDimLevel : filterLevelList) {
                    udaTemplateLevel = new UdaTemplateLevel();
                    udaTemplateLevel.setDimId(udaThemeDimLevel.getDimId());
                    udaTemplateLevel.setLevelId(udaThemeDimLevel.getLevelId());
                    udaTemplateLevel.setLevelName(udaThemeDimLevel.getLevelName());
                    udaTemplateLevel.setTemplateId(udaTemplate.getTemplateId());
                    udaTemplateLevel.setHasDict(udaThemeDimLevel.getHasDict());
                    if (udaThemeDimLevel.getValType() != null) {
                        udaTemplateLevel.setValType(udaThemeDimLevel.getValType());
                    }
                    udaTemplateLevel.setShowOrFilter("filter");
                    udaTemplateLevelList.add(udaTemplateLevel);
                    //需要封装single
                    List<SingleVo> valList = udaThemeDimLevel.getValList();
                    List<UdaTemplateLevelSingle> udaTemplateLevelSingleList = new ArrayList<>();
                    UdaTemplateLevelSingle udaTemplateLevelSingle = null;
                    if (valList != null && valList.size() > 0) {
                        for (SingleVo singleVo : valList) {
                            udaTemplateLevelSingle = new UdaTemplateLevelSingle();
                            udaTemplateLevelSingle.setDimId(udaThemeDimLevel.getDimId());
                            udaTemplateLevelSingle.setTemplateId(udaTemplate.getTemplateId());
                            udaTemplateLevelSingle.setLevelId(udaThemeDimLevel.getLevelId());
                            udaTemplateLevelSingle.setLevelName(udaThemeDimLevel.getLevelName());
                            udaTemplateLevelSingle.setSingleId(singleVo.getId() == null ? "" : singleVo.getId());
                            udaTemplateLevelSingle.setSingleValue(singleVo.getValue());
                            udaTemplateLevelSingleList.add(udaTemplateLevelSingle);
                        }
                        iUdaTemplateLevelSingleService.saveBatch(udaTemplateLevelSingleList);
                    }
                }
                iUdaTemplateLevelService.saveBatch(udaTemplateLevelList);
            }

            if (showLevelList != null && showLevelList.size() > 0) {
                List<UdaTemplateLevel> udaTemplateLevelList = new ArrayList<>();
                for (LevelVo udaThemeDimLevel : showLevelList) {
                    udaTemplateLevel = new UdaTemplateLevel();
                    udaTemplateLevel.setDimId(udaThemeDimLevel.getDimId());
                    udaTemplateLevel.setLevelId(udaThemeDimLevel.getLevelId());
                    udaTemplateLevel.setLevelName(udaThemeDimLevel.getLevelName());
                    udaTemplateLevel.setTemplateId(udaTemplate.getTemplateId());
                    udaTemplateLevel.setHasDict(udaThemeDimLevel.getHasDict());
                    if (udaThemeDimLevel.getValType() != null) {
                        udaTemplateLevel.setValType(udaThemeDimLevel.getValType());
                    }
                    udaTemplateLevel.setShowOrFilter("show");
                    udaTemplateLevelList.add(udaTemplateLevel);
                    //需要封装single
                    List<SingleVo> valList = udaThemeDimLevel.getValList();
                    List<UdaTemplateLevelSingle> udaTemplateLevelSingleList = new ArrayList<>();
                    UdaTemplateLevelSingle udaTemplateLevelSingle = null;
                    if (valList != null && valList.size() > 0) {
                        for (SingleVo singleVo : valList) {
                            udaTemplateLevelSingle = new UdaTemplateLevelSingle();
                            udaTemplateLevelSingle.setDimId(udaThemeDimLevel.getDimId());
                            udaTemplateLevelSingle.setTemplateId(udaTemplate.getTemplateId());
                            udaTemplateLevelSingle.setLevelId(udaThemeDimLevel.getLevelId());
                            udaTemplateLevelSingle.setLevelName(udaThemeDimLevel.getLevelName());
                            udaTemplateLevelSingle.setSingleId(singleVo.getId() == null ? "" : singleVo.getId());
                            udaTemplateLevelSingle.setSingleValue(singleVo.getValue());
                            udaTemplateLevelSingleList.add(udaTemplateLevelSingle);
                        }
                        iUdaTemplateLevelSingleService.saveBatch(udaTemplateLevelSingleList);
                    }
                }
                iUdaTemplateLevelService.saveBatch(udaTemplateLevelList);
            }
            //将这些数据保存到数据库当中相应的表中。下面需要判断更新库中的插入的指标。
//        model.get("dimensions")
        }
        catch (Exception e)
        {
            return  JsonResult.error(e.getMessage());
        }
        return JsonResult.normal("保存成功");
    }

    /**
     * 查询粒度值的接口目前全部返回了，按照分页的要求
     * @param page
     * @param size
     * @param themeId
     * @param version
     * @param dimId
     * @param levelId
     * @return
     */
    @RequestMapping(value = "/api/custom/analysis/glevel", method = RequestMethod.GET)
    public JsonResult getGLevel(@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "5") Integer size,String themeId,
                                String version,String dimId,String levelId){

        try {
            Pageable pageable = new PageRequest(page, size);
            Page<UdaThemeDimLevelSingle> page1 = iUdaThemeDimLevelSingleService.page(pageable);
            return JsonResult.normal(page1);
        }
        catch (Exception e)
        {
            return  JsonResult.error(e.getMessage());
        }

    }

}
