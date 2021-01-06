package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bpm_ins_proc_t")
@Data
public class BpmInsProc {
    @Id
    private Integer insProcId;         //int auto_increment, ##流程实例id
    private Integer procId;          //  int,                ##流程id
    private Integer optOrganizationId; //int,                ##发起机构id
    private Integer optPersonId;      //int,                ##发起人员id
    private LocalDateTime createTime;         //datetime,           ##创建时间
    private LocalDateTime lastOptTime;       //datetime,           ##最后操作时间
    private Integer insProcState;      //int,                ##流程实例状态 1:运行中, 2:结束
    private Integer insFinishCode;     //int,                ##流程实例结束码  0:原始, 1:成功, 2:撤销, ...
    private Integer parentInsProcId;  //int,                ##父流程实例id
    private Integer parentProcId;      //int,                ##父流程id
    private Integer parentNodeId;     // int,                ##父流程节点id
    private Integer parentNodeDyn;    // int,                ##父流程节点创建子流程动态号, 用于multi-instance sub_proc活动类型节点
    private Integer timeout;   //             int,                ##超时时间(秒), 0:不超时
    private Integer verNumber;         // int,                ##ver_number

}
