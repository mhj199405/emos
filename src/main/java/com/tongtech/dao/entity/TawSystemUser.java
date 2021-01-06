package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="taw_system_user")
public class TawSystemUser {
    @Id
    private String id;
    private String cptroomid;
    private String cptroomname;
    private String deptid;
    private String deptname;
    private String email;
    private String familyaddress;
    private String fax;
    private String mobile;
    private String operuserid;
    private String phone;
    private String remark;
    private String sex;
    private String userdegree;
    private String userid;
    private String username;
    private String operremotip;
    private LocalDateTime savetime;
    private String updatetime;
    private String account_enabled;
    private String account_expired;
    private String account_locked;
    private String credentials_expired;
    private String password;
    private String postal_code;
    private String deleted;
    private String isfullemploy;
    private String isrest;
    private String userstatus;
    private String portalrolename;
    private Integer flag;
    private Integer fail_count;
    private String ispartners;

    @Transient
    private List<Integer> roleList;
    @Transient
    private List<TawSystemRole> roleObjectList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCptroomid() {
        return cptroomid;
    }

    public void setCptroomid(String cptroomid) {
        this.cptroomid = cptroomid;
    }

    public String getCptroomname() {
        return cptroomname;
    }

    public void setCptroomname(String cptroomname) {
        this.cptroomname = cptroomname;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFamilyaddress() {
        return familyaddress;
    }

    public void setFamilyaddress(String familyaddress) {
        this.familyaddress = familyaddress;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOperuserid() {
        return operuserid;
    }

    public void setOperuserid(String operuserid) {
        this.operuserid = operuserid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserdegree() {
        return userdegree;
    }

    public void setUserdegree(String userdegree) {
        this.userdegree = userdegree;
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

    public String getOperremotip() {
        return operremotip;
    }

    public void setOperremotip(String operremotip) {
        this.operremotip = operremotip;
    }

    public LocalDateTime getSavetime() {
        return savetime;
    }

    public void setSavetime(LocalDateTime savetime) {
        this.savetime = savetime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getAccount_enabled() {
        return account_enabled;
    }

    public void setAccount_enabled(String account_enabled) {
        this.account_enabled = account_enabled;
    }

    public String getAccount_expired() {
        return account_expired;
    }

    public void setAccount_expired(String account_expired) {
        this.account_expired = account_expired;
    }

    public String getAccount_locked() {
        return account_locked;
    }

    public void setAccount_locked(String account_locked) {
        this.account_locked = account_locked;
    }

    public String getCredentials_expired() {
        return credentials_expired;
    }

    public void setCredentials_expired(String credentials_expired) {
        this.credentials_expired = credentials_expired;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getIsfullemploy() {
        return isfullemploy;
    }

    public void setIsfullemploy(String isfullemploy) {
        this.isfullemploy = isfullemploy;
    }

    public String getIsrest() {
        return isrest;
    }

    public void setIsrest(String isrest) {
        this.isrest = isrest;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }

    public String getPortalrolename() {
        return portalrolename;
    }

    public void setPortalrolename(String portalrolename) {
        this.portalrolename = portalrolename;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getFail_count() {
        return fail_count;
    }

    public void setFail_count(Integer fail_count) {
        this.fail_count = fail_count;
    }

    public String getIspartners() {
        return ispartners;
    }

    public void setIspartners(String ispartners) {
        this.ispartners = ispartners;
    }

    public List<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Integer> roleList) {
        this.roleList = roleList;
    }

    public List<TawSystemRole> getRoleObjectList() {
        return roleObjectList;
    }

    public void setRoleObjectList(List<TawSystemRole> roleObjectList) {
        this.roleObjectList = roleObjectList;
    }
}
