package com.tongtech.auth.data.db_sys_relation_dd;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRelationDdPk implements Serializable {

    private static final long serialVersionUID = -7859455022432798034L;

    private Integer deptId;

    private Integer dataId;
}
