package com.tongtech.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
@Data
public class VoNewProcInsWithPa implements Serializable {
    private int                 procId;             //流程id
    private int                 optOrganizationId;  //发起机构id
    private int                 optPersonId;        //发起人员id
    private Map<String, String> data;               //data
}
