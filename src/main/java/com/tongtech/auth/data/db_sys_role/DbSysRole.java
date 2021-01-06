package com.tongtech.auth.data.db_sys_role;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * CREATE TABLE sys_role (
 *   id                varchar(20)   NOT NULL,
 *   role_name         varchar(255)   DEFAULT NULL COMMENT '角色名称',
 *   role_status       int DEFAULT '1' COMMENT '状态',
 *   role_desc         varchar(500)   DEFAULT NULL COMMENT '描述',
 *   dept_id           int DEFAULT NULL COMMENT '归属部门',
 *   update_time       datetime DEFAULT NULL,
 *   create_time       datetime NOT NULL,
 *
 *   PRIMARY KEY (id),
 *   KEY dept_id (dept_id),
 *   CONSTRAINT sys_role_ibfk_1 FOREIGN KEY (dept_id) REFERENCES sys_department (id) ON DELETE CASCADE ON UPDATE CASCADE
 * );
 */
@Data
@Entity
@Table(name = "sys_role")
public class DbSysRole {

    @Id
//    @SequenceGenerator(name = "role_seq",sequenceName = "role_seq",allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "role_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id; //主键

    private String roleName;//角色名称

    private  Integer roleStatus;//角色状态

    private String roleDesc;//角色描述

    private Integer deptId;//归属部门

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  Date updateTime;//更新时间

    private Integer beProtected;//受保护字段，指示是否受保护，默认是0，0是不被保护

//    @ManyToMany
//    @JoinTable(
//            name = "base_rela_role_oper",
//            joinColumns = @JoinColumn(name="roleId",referencedColumnName = "roleId"),
//            inverseJoinColumns = @JoinColumn(name = "operId",referencedColumnName = "operId")
//    )
//    private List<DbSysOperation> dbSysOperationList;


}
