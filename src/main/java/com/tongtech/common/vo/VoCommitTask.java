package com.tongtech.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
public class VoCommitTask implements Serializable {
     private int     insNodeId;          //节点实例id

     private int     optOrganizationId;  //操作机构id
     private int     optPersonId;       //操作人员id

     private int     assignGroupId;      //指派分组id
     private int     assignUserId;       //指派用户id
     private String  decision;           //输出决策逗号分隔
}
