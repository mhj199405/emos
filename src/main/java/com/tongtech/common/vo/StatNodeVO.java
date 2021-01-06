package com.tongtech.common.vo;


import lombok.Data;

import java.util.List;

@Data
public class StatNodeVO {
    private Integer nodeSumNum;
    private Integer exceptionSumNum;
    private String exceptionSumRate;
    private List<BpmNodeVO> bpmNodeVOList;
}
