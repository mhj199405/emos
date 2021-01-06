package com.tongtech.auth.service;


import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.vo.RestResult;

import java.util.Map;

public interface UserService {
    RestResult getUserList(Map<String, Object> model);

    RestResult deleteControl(String id);

    RestResult updateUser(String id, String operType, String value);

    RestResult insertUser(DbSysUser model);

    RestResult getDept();

    RestResult getUserInfo(String id);

    RestResult findAllUser(String userName);

    RestResult getAllUser();
}
