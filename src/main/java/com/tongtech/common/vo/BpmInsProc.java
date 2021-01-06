package com.tongtech.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class BpmInsProc implements Serializable {
    private int             insProcId;      //流程实例id

    private int             procId;         //流程id

    private int             optOrganizationId;  //发起机构id
    private int             optPersonId;       //发起人员id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime   createTime;     //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOptTime;    //最后操作时间
    private int             insProcState;   //流程实例状态 1:运行中, 2:结束
    private int             insFinishCode;  //流程实例结束码  0:原始, 1:成功, 2:撤销, ...

    private int             parentInsProcId;//父流程实例id
    private int             parentProcId;   //父流程id
    private int             parentNodeId;   //父流程节点id
    private int             parentNodeDyn;  //父流程节点创建子流程动态号, 用于multi-instance sub_proc活动类型节点
    private int             timeout;        //超时时间(秒), 0:不超时
    private int             verNumber;      //ver_number
}
