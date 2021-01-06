package com.tongtech.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonRuleTVo implements Serializable {

        private Integer ruleScriptId;  // 规则节点执行脚本ID
        private String content;      // 脚本
        private String scriptName ;  // 脚本名称
        private String scriptDesc;    // 脚本描述
        private Integer verNumber;
//        private String contentStr;

}
