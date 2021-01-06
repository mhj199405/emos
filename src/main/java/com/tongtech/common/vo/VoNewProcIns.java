package com.tongtech.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class VoNewProcIns implements Serializable {
    private int     procId;             //流程id

    private int     optOrganizationId;  //发起机构id
    private int     optPersonId;       //发起人员id
    private Map<String, String> data;               //data

    public int getProcId() {
        return procId;
    }

    public void setProcId(int procId) {
        this.procId = procId;
    }

    public int getOptOrganizationId() {
        return optOrganizationId;
    }

    public void setOptOrganizationId(int optOrganizationId) {
        this.optOrganizationId = optOrganizationId;
    }

    public int getOptPersonId() {
        return optPersonId;
    }

    public void setOptPersonId(int optPersonId) {
        this.optPersonId = optPersonId;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
