package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * create table TAW_SYSTEM_DEPT
 * (
 *   id                int ,
 *   deleted           VARCHAR(10),
 *   deptemail         VARCHAR(100),
 *   deptfax           VARCHAR(100),
 *   deptid            VARCHAR(200),
 *   deptmanager       VARCHAR(100),
 *   deptmobile        VARCHAR(100),
 *   deptname          VARCHAR(100),
 *   deptphone         VARCHAR(100),
 *   depttype          VARCHAR(100),
 *   operremoteip      VARCHAR(100),
 *   opertime          VARCHAR(100),
 *   operuserid        VARCHAR(100),
 *   ordercode         int default 0,
 *   parentdeptid      VARCHAR(100),
 *   regionflag        int default 0,
 *   remark            VARCHAR(100),
 *   tmporarybegintime VARCHAR(50),
 *   tmporarymanager   VARCHAR(100),
 *   tmporarystoptime  VARCHAR(100),
 *   updatetime        VARCHAR(100),
 *   leaf              VARCHAR(10),
 *   areaid            VARCHAR(100),
 *   linkid            VARCHAR(100),
 *   parentlinkid      VARCHAR(100),
 *   tmpdeptid         VARCHAR(50),
 *   ispartners        VARCHAR(100),
 *   isdaiweiroot      VARCHAR(10),
 *   region_id         VARCHAR(20),
 *   region_name       VARCHAR(20),
 *   primary key(id)
 * );
 */
@Data
@Entity
@Table(name="taw_system_dept")
public class TawSystemDept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String deleted;
    private String deptemail;
    private String deptfax;
    private String deptid;
    private String deptmanager;
    private String deptmobile;
    private String deptname;
    private String deptphone;
    private String depttype;
    private String operremoteip;
    private String opertime;
    private String operuserid;
    private Integer ordercode;
    private Integer regionflag;
    private String parentdeptid;
    private String remark;
    private String tmporarybegintime;
    private String tmporarymanager;
    private String tmporarystoptime;
    private String updatetime;
    private String leaf;
    private String areaid;
    private String linkid;
    private String parentlinkid;
    private String tmpdeptid;
    private String ispartners;
    private String isdaiweiroot;
    private String region_id;
    private String region_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeptemail() {
        return deptemail;
    }

    public void setDeptemail(String deptemail) {
        this.deptemail = deptemail;
    }

    public String getDeptfax() {
        return deptfax;
    }

    public void setDeptfax(String deptfax) {
        this.deptfax = deptfax;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptmanager() {
        return deptmanager;
    }

    public void setDeptmanager(String deptmanager) {
        this.deptmanager = deptmanager;
    }

    public String getDeptmobile() {
        return deptmobile;
    }

    public void setDeptmobile(String deptmobile) {
        this.deptmobile = deptmobile;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getDeptphone() {
        return deptphone;
    }

    public void setDeptphone(String deptphone) {
        this.deptphone = deptphone;
    }

    public String getDepttype() {
        return depttype;
    }

    public void setDepttype(String depttype) {
        this.depttype = depttype;
    }

    public String getOperremoteip() {
        return operremoteip;
    }

    public void setOperremoteip(String operremoteip) {
        this.operremoteip = operremoteip;
    }

    public String getOpertime() {
        return opertime;
    }

    public void setOpertime(String opertime) {
        this.opertime = opertime;
    }

    public String getOperuserid() {
        return operuserid;
    }

    public void setOperuserid(String operuserid) {
        this.operuserid = operuserid;
    }

    public Integer getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(Integer ordercode) {
        this.ordercode = ordercode;
    }

    public Integer getRegionflag() {
        return regionflag;
    }

    public void setRegionflag(Integer regionflag) {
        this.regionflag = regionflag;
    }

    public String getParentdeptid() {
        return parentdeptid;
    }

    public void setParentdeptid(String parentdeptid) {
        this.parentdeptid = parentdeptid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTmporarybegintime() {
        return tmporarybegintime;
    }

    public void setTmporarybegintime(String tmporarybegintime) {
        this.tmporarybegintime = tmporarybegintime;
    }

    public String getTmporarymanager() {
        return tmporarymanager;
    }

    public void setTmporarymanager(String tmporarymanager) {
        this.tmporarymanager = tmporarymanager;
    }

    public String getTmporarystoptime() {
        return tmporarystoptime;
    }

    public void setTmporarystoptime(String tmporarystoptime) {
        this.tmporarystoptime = tmporarystoptime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getLeaf() {
        return leaf;
    }

    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getParentlinkid() {
        return parentlinkid;
    }

    public void setParentlinkid(String parentlinkid) {
        this.parentlinkid = parentlinkid;
    }

    public String getTmpdeptid() {
        return tmpdeptid;
    }

    public void setTmpdeptid(String tmpdeptid) {
        this.tmpdeptid = tmpdeptid;
    }

    public String getIspartners() {
        return ispartners;
    }

    public void setIspartners(String ispartners) {
        this.ispartners = ispartners;
    }

    public String getIsdaiweiroot() {
        return isdaiweiroot;
    }

    public void setIsdaiweiroot(String isdaiweiroot) {
        this.isdaiweiroot = isdaiweiroot;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }
}
