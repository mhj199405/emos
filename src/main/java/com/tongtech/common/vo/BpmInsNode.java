package com.tongtech.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BpmInsNode implements Serializable {
    private int             insNodeId;      //节点实例id

    private int             insProcId;      //流程实例id
    private int             procId;         //流程id
    private int             nodeId;         //节点id
    private int             readyNum;       //就绪次数
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readyTime;      //就绪时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime   beginTime;      //开始处理时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime   endTime;        //结束时间
    private int             insNodeState;   //节点实例状态 0:原始, 1:就绪, 2:处理中 3:结束

    private int             optOrganizationId;  //操作机构id
    private int             optPersonId;       //操作人员id

    private int             groupId;        //分组id
    private int             userId;         //用户id
    private int             assignGroupId;  //指派分组id
    private int             assignUserId;   //指派用户id

    private int             inCondition;    //进入条件
    private String          decision;       //输出决策逗号分隔

    private int             nodeDynNum;     //节点创建子流程动态数量, 用于multi-instance sub_proc活动类型节点
    private int             timeout;        //超时时间(秒), 0:不超时
    private int             verNumber;      //ver_number

    private String          procName;
    private String          nodeName;
    private String          groupName;
    private String          userName;
    private String          optOrganizationName;
    private String          optPersonName;
}
