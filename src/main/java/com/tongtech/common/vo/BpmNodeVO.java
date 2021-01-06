package com.tongtech.common.vo;

import lombok.Data;

@Data
public class BpmNodeVO {
    private Integer procId;
    private Integer nodeId;
    private String nodeName;
    private Long avgTime;
    private Integer nodeSum;
    private Integer exceptionNum;  //
    private String exceptionRate;  //
}
