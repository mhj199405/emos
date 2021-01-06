package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * -- 表单项目实例表
 * drop table if exists frm_form_item_inst;
 * create table frm_form_item_inst
 * (
 *   id               int auto_increment   comment '表单项目实例ID',
 *   form_inst_id     int                  comment '表单实例ID',
 *   ins_proc_id      int                  comment '流程实例id',
 *   ins_node_id      int                  comment '节点实例id',
 *   item_id          varchar(40)          comment '表单项目ID ',
 *   item_key         varchar(256)         comment '数据项英文名称',
 *   seq_id           int                  comment '数据序号',
 *   seq_name         varchar(128)         comment '数据名称',
 *   data             varchar(512)         comment '数据值',
 *   create_time      datetime             comment '创建时间',
 *   update_time      datetime             comment '更新时间',
 *   proc_id          int                  comment '流程id'
 *   ver_number       int                  comment '版本号'
 *   primary key (id)
 * );
 * create unique index idx_frm_form_item_inst1
 *   on frm_form_item_inst(form_inst_id,ins_proc_id,ins_node_id,item_id,seq_id);
 */
@Data
@Entity
@Table(name="frm_form_item_inst")
public class InstanceFormItem extends BaseEntity{

    private Integer formInstId;

    private Integer insProcId;

    private Integer insNodeId;

    private String itemId;

    private String itemKey;

    private Integer seqId;

    private String seqName;

    private String data;

    private Integer procId;

    private Integer verNumber;

}
