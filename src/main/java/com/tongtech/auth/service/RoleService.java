package com.tongtech.auth.service;



import com.tongtech.auth.data.db_sys_data_control.SysDataControl;
import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_role.DbSysRole;
import com.tongtech.auth.vo.RestResult;

import java.util.List;
import java.util.Map;

public interface RoleService {
    RestResult getDataControlList(Map<String, Object> model);

    RestResult deleteRole(String id);

    RestResult updateRole(DbSysRole model);

    RestResult insertRole(DbSysRole model);

    RestResult getRoleInfo(String id);

    RestResult onAuthorized(String roleId, List<DbSysMenu> menuList, List<SysDataControl> controlList);
}
