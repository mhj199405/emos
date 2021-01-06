package com.tongtech.common.vo;


import lombok.Data;

import java.util.List;

@Data
public class StatInsVO {
    private Integer insSumNum;
    private Integer exceptionSumNum;
    private String exceptionSumRate;
    private List<BpmInsVO> bpmInsVOList;
}
