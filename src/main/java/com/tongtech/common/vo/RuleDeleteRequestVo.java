package com.tongtech.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class RuleDeleteRequestVo  {
    private List<Integer> ruleId;

    public List<Integer> getRuleId() {
        return ruleId;
    }

    public void setRuleId(List<Integer> ruleId) {
        this.ruleId = ruleId;
    }
}
