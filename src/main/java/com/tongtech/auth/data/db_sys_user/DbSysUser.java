package com.tongtech.auth.data.db_sys_user;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tongtech.auth.data.db_sys_role.DbSysRole;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * CREATE TABLE sys_user (
 *   id                varchar(20)   NOT NULL,
 *   dept_id           int NOT NULL COMMENT '归属部门',
 *   login_name        varchar(50)   NOT NULL COMMENT '登录名',
 *   real_name         varchar(50)   DEFAULT NULL COMMENT '真实姓名',
 *   mobile            varchar(100)   DEFAULT NULL COMMENT '手机号码',
 *   email             varchar(50)   DEFAULT NULL COMMENT '邮箱',
 *   `password`        varchar(32)   DEFAULT NULL COMMENT '密码',
 *   address           varchar(100)   DEFAULT NULL COMMENT '地址',
 *   work_no           varchar(50)   DEFAULT NULL,
 *   `status`          int DEFAULT NULL COMMENT '状态',
 *   avatar            varchar(100)   DEFAULT NULL COMMENT '头像',
 *   gender            int DEFAULT NULL COMMENT '性别',
 *   birthday          datetime DEFAULT NULL COMMENT '生日',
 *   identity_card_type int DEFAULT NULL COMMENT '证件类型',
 *   identity_card_no  varchar(100)   DEFAULT NULL COMMENT '证件号',
 *   is_supervisor     int DEFAULT '0' COMMENT '是否主管',
 *   is_delete         int DEFAULT '0',
 *   expired_time      datetime DEFAULT NULL,
 *   update_time       datetime DEFAULT NULL,
 *   create_time       datetime NOT NULL,
 *
 *   PRIMARY KEY (id),
 *   KEY dept_id (dept_id),
 *   CONSTRAINT sys_user_ibfk_1 FOREIGN KEY (dept_id) REFERENCES sys_department (id) ON DELETE CASCADE ON UPDATE CASCADE
 * );
 */
@Entity
@Table(name = "sys_user")
@Data
public class DbSysUser {
    @Id
//    @SequenceGenerator(name = "user_seq",sequenceName = "user_seq",allocationSize = 1,initialValue = 2 )
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //主键
    private Integer deptId; //归属部门
    private String loginName;//登录名
    private String password;//密码
    private String realName;//真实名称
    private String email;//邮箱
    private String mobile;//电话
    private String  address;//地址
    private String workNo;
    private Integer status;//状态
    private String avatar;//头像
    private Integer gender;//性别

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  birthday;//生日

    private Integer identityCardType;//证件类型

    private String identityCardNo;//证件号


    private Integer isSupervisor;//是否主管

    private Integer isDelete;


    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  Date expiredTime;//有效时间

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  Date updateTime;//修改密码时间

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

  /*  @ManyToMany
    @JoinTable(
            name = "base_rela_user_role",
            joinColumns = @JoinColumn(name="userId",referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId",referencedColumnName = "roleId")
    )
//    @OrderBy("id desc ")
//    @JsonIgnoreProperties("powers")
    private List<DbSysRole> dbSysRoleList;*/
  @Transient
  private List<DbSysRole> dbSysRoleList1;

  @Transient
  private List<Integer> roleIdList;
  /*public static void main(String[] args) {
        Date date = new Date(System.currentTimeMillis());
        System.out.println(date);
        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
        String format = dateTimeInstance.format(date);
        System.out.println(format);
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getIdentityCardType() {
        return identityCardType;
    }

    public void setIdentityCardType(Integer identityCardType) {
        this.identityCardType = identityCardType;
    }

    public String getIdentityCardNo() {
        return identityCardNo;
    }

    public void setIdentityCardNo(String identityCardNo) {
        this.identityCardNo = identityCardNo;
    }

    public Integer getIsSupervisor() {
        return isSupervisor;
    }

    public void setIsSupervisor(Integer isSupervisor) {
        this.isSupervisor = isSupervisor;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<DbSysRole> getDbSysRoleList1() {
        return dbSysRoleList1;
    }

    public void setDbSysRoleList1(List<DbSysRole> dbSysRoleList1) {
        this.dbSysRoleList1 = dbSysRoleList1;
    }

    public List<Integer> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Integer> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
