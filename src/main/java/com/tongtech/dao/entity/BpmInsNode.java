package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bpm_ins_node_t")
@Data
public class BpmInsNode {
    @Id
    private Integer insNodeId;     //        int auto_increment, ##节点实例id
    private Integer insProcId;   //        int,                ##流程实例id
    private Integer procId; //             int,                ##流程id
    private Integer nodeId;   //          int,                ##节点id
    private Integer readyNum;  //         int,                ##就绪次数
    private LocalDateTime readyTime;  //           ##就绪时间
    private LocalDateTime beginTime;//          datetime,           ##开始处理时间
    private LocalDateTime endTime;//            datetime,           ##结束时间
    private Integer insNodeState;//      int,                ##节点实例状态 0:原始, 1:就绪, 2:处理中 3:结束

    private Integer optOrganizationId;//int,                ##操作机构id
    private Integer optPersonId;//     int,                ##操作人员id

    private Integer groupId;//            int,                ##分组id
    private Integer userId;//             int,                ##用户id
    private Integer assignGroupId;//     int,                ##指派分组id
    private Integer assignUserId;//      int,                ##指派用户id

    private Integer inCondition;//       int,                ##进入条件
    private String decision;//            varchar(100),       ##输出决策逗号分隔

    private Integer nodeDynNum;//        int,                ##节点创建子流程动态数量, 用于multi-instance sub_proc活动类型节点

    private Integer timeout;//             int,                ##超时时间(秒), 0:不超时

    private Integer verNumber;//          int,                ##ver_number

}
