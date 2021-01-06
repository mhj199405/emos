package com.tongtech.common.vo.analysis;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 专题数据集
@Data
public class ThemeVo
{
    private String themeId;
    private String themeName;
    private List<MeasureVo> measureList = new ArrayList<>();
    private List<DimensionVo> dimList = new ArrayList<>();
}

