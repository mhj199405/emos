package com.tongtech.auth.data.db_sys_relation_dm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SysRelationDmRepository extends JpaRepository<SysRelationDm,SysRelationDmPk>, JpaSpecificationExecutor<SysRelationDm> {
    /**
     * 根据部门id,删除相应的条目
     * @param deptId
     */
    @Modifying
    @Transactional
    @Query(value="delete from sys_relation_dm where dept_id=:deptId",nativeQuery=true)
    void deleteByDeptId(@Param("deptId") Integer deptId);

    /**
     * ddWrapper.lambda().eq(SysRelationDm::getMenuId, item.getId()).eq(SysRelationDm::getDeptId, deptId);
     * 根据菜单id和部门id进行删除
     */
    @Transactional
    @Modifying
    @Query(value="delete from sys_relation_dm where sys_relation_dm.menu_id=:menuId and sys_relation_dm.dept_id=:deptId",nativeQuery=true)
    void deleteByMenuIdAndDeptIds(@Param("menuId") Integer menuId, @Param("deptId") Integer childrenDeptId);



}
