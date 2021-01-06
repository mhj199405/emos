package com.tongtech.common.vo.analysis;

import lombok.Data;

import java.util.List;

@Data
public class TemplateVo {
    private String templateId;
    private String themeId;
    private String templateName;
    private Boolean hasShare;
    private List<MeasureVo> measureList;
//    private List<LevelVo> levelList;
    private List<LevelVo> showLevelList;
    private List<LevelVo> filterLevelList;
    private String Conditions;
}
