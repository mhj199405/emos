package com.tongtech.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class VoNodeAttr implements Serializable {
    //流程id
    private int     procId;
    //节点id
    private int     nodeId;

    //关联表单ID
    private int     formId;
    //脚本节点的执行脚本ID
    private int     scriptId;
    //服务节点执行脚本ID
    private int     svcScriptId;
    //规则节点执行脚本ID
    private int     ruleScriptId;

    //分组id
    private int     groupId;
    //用户id
    private int     userId;

    //进入条件
    private int     inCondition;
    //区分子流程的key逗号分隔, 用于multi-instance sub_proc活动类型节点
    private String  diffSubKey;


    //重试次数, 用于script_task, service_task
    private int             retryCount;
    //重试间隔(秒)
    private int             retryInterval;
    //超时时间(秒), 0:不超时
    private int             timeout;

    private String  procName;
    private String  nodeName;
    private String  formName;
    private String  groupName;
    private String  userName;
}