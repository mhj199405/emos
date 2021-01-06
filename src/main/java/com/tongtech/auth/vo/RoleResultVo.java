package com.tongtech.auth.vo;

import com.tongtech.auth.data.db_sys_role.DbSysRole;
import lombok.Data;

import java.util.List;

//用于封装查询角色的一些数据
@Data
public class RoleResultVo extends DbSysRole {

 private List menuList;

 private List operList;
}
