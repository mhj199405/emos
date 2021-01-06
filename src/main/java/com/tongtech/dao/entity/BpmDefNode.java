package com.tongtech.dao.entity;

import com.tongtech.dao.pk.DefNodePK;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "bpm_def_node_t")
@Data
@IdClass(DefNodePK.class)
public class BpmDefNode {
    @Id
    private Integer procId;//             int,                ##流程id
    @Id
    private Integer nodeId;//             int,                ##节点id
    private Integer nodeType;//           int,                ##节点类型  1:event, 2:activity, 3:gateway
    private Integer eventType;//          int,                ##事件类型  0:none, 1:start, 2:end, 3:signal, 4:catching signal, 5:catching timer
    private Integer activityType;//       int,                ##活动类型  0:none, 1:user task, 2:script task, 3:service task, 4:business rule task,
    //            ##                  5:sub-proc,6:multi-instance sub_proc, 7:rollback task
    private Integer gatewayTypeA;//      int,                ##网关类型A 0:none, 1:diverging, 2:converging,
    private Integer gatewayTypeB;//      int,                ##网关类型B 0:none, 1:parallel, 2:exclusive, 3:inclusive
    private Integer subProcType;//       int,                ##子流程或多实例子流程类型 0:none，1:等待结束, 2:创建后不管
    private String nodeName;//          varchar(100),       ##节点名称
    private String nodeDesc;//           varchar(500),       ##节点描述

    private Integer formId;//             int,                ##关联表单ID

    private Integer scriptId;//           int,                ##脚本节点的执行脚本ID
    private Integer svcScriptId;//       int,                ##服务节点执行脚本ID
    private Integer ruleScriptId;//      int,                ##规则节点执行脚本ID

    private Integer groupId;//            int,                ##分组id
    private Integer userId;//             int,                ##用户id
    private Integer assignGroupId;//     int,                ##指派分组id
    private Integer assignUserId;//     int,                ##指派用户id

    private Integer inCondition;//       int,                ##进入条件
    private String decision;//           varchar(100),       ##输出决策逗号分隔

    private String diffSubKey;//        varchar(1000),      ##区分子流程的key逗号分隔, 用于multi-instance sub_proc活动类型节点

    private Integer retryCount;//         int,                ##重试次数, 用于script_task, service_task
    private Integer retryInterval;//      int,                ##重试间隔(秒)

    private Integer timeout;//           int,                ##超时时间(秒), 0:不超时

    private Integer verNumber;//          int,                ##ver_number

}
