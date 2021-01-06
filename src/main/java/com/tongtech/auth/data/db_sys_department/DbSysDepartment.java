package com.tongtech.auth.data.db_sys_department;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * CREATE TABLE sys_department (
 *   id                int NOT NULL AUTO_INCREMENT,
 *   dept_name         varchar(30)   DEFAULT NULL COMMENT '部门名称',
 *   parent_id         int NOT NULL DEFAULT '0',
 *   dept_no           varchar(30)   DEFAULT NULL COMMENT '部门编码',
 *   dept_telphone     varchar(30)   DEFAULT NULL COMMENT '联系方式',
 *   dept_email        varchar(100)   DEFAULT NULL,
 *   dept_level        int DEFAULT NULL,
 *   order_sort        int DEFAULT '0' COMMENT '排序',
 *   dept_position     varchar(200)   DEFAULT NULL COMMENT '部门地址',
 *   dept_desc         varchar(200)   DEFAULT NULL COMMENT '部门描述',
 *   create_time       datetime NOT NULL,
 *
 *   PRIMARY KEY (id)
 * );
 */
@Entity
@Table(name = "sys_department")
@Data
public class DbSysDepartment {
    @Id
//    @SequenceGenerator(name = "department_seq",sequenceName = "department_seq",allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "department_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer parentId;

    private  String deptNo;//部门编码

    private String  deptName;//部门名称

    private String deptTelphone;//联系方式

    private Integer deptLevel;

    private Integer orderSort;//排序

    private  String deptPosition;//部门地址

    private  String deptDesc;//部门描述

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  createTime; //创建时间

    @Transient
    private List<DbSysDepartment> children;

}
