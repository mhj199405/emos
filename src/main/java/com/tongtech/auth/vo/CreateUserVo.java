package com.tongtech.auth.vo;

import com.tongtech.auth.data.db_sys_user.DbSysUser;
import lombok.Data;


import java.util.List;

@Data
public class CreateUserVo extends DbSysUser {
    private List<Integer> roleId;
}
