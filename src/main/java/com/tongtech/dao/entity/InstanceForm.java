package com.tongtech.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 *  id               int auto_increment   comment '表单实例ID',
 *   create_user      int                  comment '创建人ID',
 *   create_group     int                  comment '创建人组ID',
 *   update_user      int                  comment '更新人ID',
 *   form_id          int                  comment '表单ID',
 *   ver_number       int                  comment '版本号',
 *   form_key         varchar(128)         comment '表单编码',
 *   proc_id          int                  comment '流程ID',
 *   node_id          int                  comment '节点id',
 *   ins_proc_id      int                  comment '流程实例id',
 *   ins_node_id      int                  comment '节点实例id',
 *   create_time      datetime             comment '创建时间',
 *   update_time      datetime             comment '更新时间',
 */
@Data
@Entity
@Table(name="frm_form_inst")
public class InstanceForm {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();//创建时间

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//修改时间

    private Integer createUser;

    private Integer createGroup;

    private Integer updateUser;

    private Integer formId;

    private Integer verNumber;

    private String formKey;

    private Integer procId;

    private Integer nodeId;

    private Integer insProcId;

    private Integer insNodeId;

//    private Integer formVer;


}
