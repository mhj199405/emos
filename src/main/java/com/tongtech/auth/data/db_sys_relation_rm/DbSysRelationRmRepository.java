package com.tongtech.auth.data.db_sys_relation_rm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DbSysRelationRmRepository extends JpaRepository<DbSysRelationRm,Integer>,JpaSpecificationExecutor<DbSysRelationRm> {
    /**
     * 查询出所有的菜单对象
     * @param name
     * @return
     */
    @Query(value = "select * from  where role_id in (select t2.role_id from base_rela_user_role t2 where t2.user_id=(select t3.user_id from base_user t3 where t3.login_name=:name)) ",nativeQuery = true)
    List<DbSysRelationRm> findAllMenus(@Param(value = "name") String name);

    /**
     * 根据roleid来进行角色的查找
     * @return
     */
    List<DbSysRelationRm> findByRoleId(Integer roleId);

    /**
     * 根据roleID来进行角色的删除
     * @param roleId
     */
    void deleteByRoleId(Integer roleId);

    /**
     * 根据roleId确定有多少条记录
     * @param roleId
     */
    @Query(value = "select count(t.roleId) from DbSysRelationRm t where t.roleId=:roleId")
    long getCountByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据角色id和菜单id进行删除
     *
     */
    @Transactional
    @Modifying
    @Query(value="delete from sys_relation_rm where sys_relation_rm.role_id=:roleId and sys_relation_rm.menu_id=:menuId",nativeQuery=true)
    void deleteByRoleIdAndMenuIds(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);

    @Transactional
    void deleteAllByRoleId(Integer roleId);
}
