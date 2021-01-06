package com.tongtech.auth.service;


import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.vo.RestResult;

import java.util.List;
import java.util.Map;

public interface MenuService {

    RestResult getDataControlList(Map<String, Object> model);

    RestResult deleteControl(List<Integer> id);

    RestResult updateControl(DbSysMenu model);

    RestResult insertControl(DbSysMenu model);
}
