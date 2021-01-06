package com.tongtech.auth.data.db_sys_relation_dd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SysRelationDdRepository  extends JpaRepository<SysRelationDd,SysRelationDdPk>, JpaSpecificationExecutor<SysRelationDd> {

    @Transactional
    @Modifying
    @Query(value="delete from sys_relation_dd where dept_id=:deptId",nativeQuery=true)
    void deleteByDeptId(@Param("deptId") Integer deptId);

    /**
     * 根据子部门的部门id和dataId进行记录的删除
     * @param childrenDeptId
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value="delete from sys_relation_dd where sys_relation_dd.dept_id=:deptId and sys_relation_dd.data_id=:dataId",nativeQuery=true)
    void deleteAllByDeptIdAndDataIds(@Param("deptId") Integer childrenDeptId, @Param("dataId") Integer id);



}
