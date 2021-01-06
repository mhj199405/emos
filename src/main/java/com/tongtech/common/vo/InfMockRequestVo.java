package com.tongtech.common.vo;

import lombok.Data;

import java.util.Map;

@Data
public class InfMockRequestVo {
    private BpmDefProc process;
    private Map<String,String>  params;  // Map<String,String> 对象字符串

    public BpmDefProc getProcess() {
        return process;
    }

    public void setProcess(BpmDefProc process) {
        this.process = process;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
