package com.tongtech.common.vo;

import lombok.Data;

@Data
public class BpmInsVO {

    private Integer procId;
    private String procName;
    private Integer insNum;
    private Integer exceptionNum;
    private String exceptionRate;
    private Integer insSumNum;
    private Integer exceptionSumNum;
    private Double exceptionSumRate;
}
