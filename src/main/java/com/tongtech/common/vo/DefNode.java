package com.tongtech.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DefNode implements Serializable {
    //节点id
    private int     nodeId;
    //节点名称
    private String  nodeName;
    //节点描述
    private String  nodeDesc;

    //节点类型  1:event, 2:activity, 3:gateway
    private int     nodeType;

    //事件类型  0:none, 1:start, 2:end, 3:signal
    //        4:catching signal, 5:catching timer
    private int     eventType;

    //活动类型  0:none, 1:user task, 2:script task,
    //        3:service task, 4:rule task,
    //        5:sub-proc, 6:multi-instance sub_proc,
    //        7:rollback task
    private int     activityType;

    //网关类型A 0:none, 1:diverging, 2:converging
    private int     gatewayTypeA;

    //网关类型B 0:none, 1:parallel, 2:exclusive, 3:inclusive
    private int     gatewayTypeB;

    //子流程或多实例子流程类型 0:none，1:等待结束, 2:创建后不管
    private int     subProcType;
}
