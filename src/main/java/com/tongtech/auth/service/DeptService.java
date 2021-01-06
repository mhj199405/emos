package com.tongtech.auth.service;


import com.tongtech.auth.data.db_sys_department.DbSysDepartment;
import com.tongtech.auth.vo.RestResult;

import java.util.Map;

public interface DeptService {

    RestResult deleteControl(Integer id);

    RestResult updateControl(DbSysDepartment model);

    RestResult insertControl(DbSysDepartment model);

    RestResult getBaseInfo();

    RestResult getDeptInfo(Integer id);

    RestResult onAuthorized(Map<String, Object> dict);

    RestResult getAllDepartment(String deptName, Integer pageIndex, Integer pageSize);
}
