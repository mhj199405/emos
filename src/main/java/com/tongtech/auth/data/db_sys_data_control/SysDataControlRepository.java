package com.tongtech.auth.data.db_sys_data_control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SysDataControlRepository extends JpaRepository<SysDataControl,Integer>, JpaSpecificationExecutor<SysDataControl> {
    /**
     * 根据deptId查询数据
     * @param
     * @return
     */
    @Query(value="select data_id as id,data_name,data_param,data_operation,data_value1,data_value2,data_desc from sys_relation_dd where dept_id=:deptId ORDER BY control_order desc",nativeQuery=true)
    List<SysDataControl> getDataControlListByDeptId(@Param("deptId") Integer deptId);
}
