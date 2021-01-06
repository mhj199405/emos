package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "taw_system_userrefrole")
public class TawSystemUserRefRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer roleid;
    private String rolename;
    private String userid;
    private String username;
    private String remark;
    private String subroleid;
    private String status;
    private Integer currentsheetcount;
    private String grouptype;
    private String roletype;
    private String version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubroleid() {
        return subroleid;
    }

    public void setSubroleid(String subroleid) {
        this.subroleid = subroleid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCurrentsheetcount() {
        return currentsheetcount;
    }

    public void setCurrentsheetcount(Integer currentsheetcount) {
        this.currentsheetcount = currentsheetcount;
    }

    public String getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
