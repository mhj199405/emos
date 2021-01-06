package com.tongtech.common.vo.analysis;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DimensionVo
{
    private String dimId;
    private String dimName;
    private List<LevelVo> levelList = new ArrayList<>();

}

