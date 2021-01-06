package com.tongtech.common.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LevelVo
{
    private String dimId;
    private String levelId;
    private String LevelName;
    private Integer ValType;
    private List<SingleVo> ValList=new ArrayList<>();
    private Boolean hasDict;//是否有字典表
}

