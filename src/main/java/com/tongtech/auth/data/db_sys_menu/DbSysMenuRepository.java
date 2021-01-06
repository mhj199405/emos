package com.tongtech.auth.data.db_sys_menu;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DbSysMenuRepository  extends JpaRepository<DbSysMenu,Integer>,JpaSpecificationExecutor<DbSysMenu>{
    /**
     * 根据登陆名查找当前用户所具有的菜单访问权限
     * @param name
     * @return
     */
    @Query(value = "select t5.* from base_user t1 left join base_rela_user_role t2 on t1.user_id=t2.user_id\n" +
            "left join base_role t3 on t2.role_id=t3.role_id \n" +
            "left join base_rela_role_menu t4 on t3.role_id=t4.role_id \n" +
            "left join base_menu t5 on t4.menu_id=t5.menu_id where t1.login_name=:loginName and t5.is_show=1 order by t4.order_index,t5.order_index",nativeQuery = true)
    List<DbSysMenu> findAllMenusByOne(@Param("loginName") String name);

    /**
     * 根据roleid进行相关菜单的查找
     * @param roleId
     * @return
     */
    @Query(value = "select t2.* from base_rela_role_menu t1 left join base_menu t2 on t1.menu_id=t2.menu_id where t1.role_id=:roleId order by t1.order_index,t2.order_index",nativeQuery = true)
    List<DbSysMenu> findByRoleId(@Param("roleId") Integer roleId);

    /**
     * 查询所有的权限
     * @return
     */
    @Query(value="select t.* from sys_menu t where t.is_menu=0 and t.menu_status=1 order by t.menu_order desc ",nativeQuery=true)
    List<DbSysMenu> findAllAuthorities();

    /**
     * 根据用户id，查询所有的私有权限和公开的权限
     * @param
     * @return
     */
    @Query(value="SELECT m.id" +
            "        FROM\n" +
            "            sys_menu m\n" +
            "        LEFT JOIN sys_relation_rm rd ON m.id = rd.menu_id\n" +
            "        LEFT JOIN sys_relation_ru ur ON rd.role_id = ur.role_id\n" +
            "        WHERE\n" +
            "            is_menu = 0\n" +
            "        AND m.menu_status = 1\n" +
            "        AND (ur.user_id = 1 OR issued = 2)\n" +
            "        ORDER BY\n" +
            "            menu_order DESC",nativeQuery=true)
    List<Integer> findAllAuthoritiesByUserId(@Param("userId") Integer userId);

    @Query(value="select t.* from sys_menu  t where t.menu_status <> 0 and t.issued <> 2",nativeQuery=true)
    List<DbSysMenu> findMenuList();

    @Query(value="SELECT\n" +
            "            id,\n" +
            "            menu_name,\n" +
            "            menu_url,\n" +
            "            open_type,\n" +
            "            parent_id,\n" +
            "            menu_component,\n" +
            "            menu_icon,\n" +
            "            menu_order,\n" +
            "            menu_type,\n" +
            "            is_show,\n" +
            "            is_route,\n" +
            "            is_menu,\n" +
            "            issued,\n" +
            "            menu_status,\n" +
            "            create_time,\n" +
            "            method_type \n "+
            "        FROM\n" +
            "            sys_menu m\n" +
            "        LEFT JOIN sys_relation_dm dm ON m.id = dm.menu_id\n" +
            "        WHERE\n" +
            "            m.menu_status = 1\n" +
            "        AND dm.dept_id =:deptId\n" +
            "        ORDER BY\n" +
            "            m.menu_order DESC",nativeQuery=true)
    List<DbSysMenu> getMenuListByDeptId(@Param("deptId") Integer deptId);

    @Query(value="select t.* from sys_menu t ORDER BY t.menu_order DESC",nativeQuery=true)
    List<DbSysMenu> findAllMenu();

    /**
     * 根据id进行多项删除
     * @param ids
     */
    @Transactional
    @Modifying
    @Query(value="delete from sys_menu where sys_menu.id in (:ids)",nativeQuery=true)
    void deleteAllInIds(@Param("ids") List<Integer> ids);

    /**
     * 根据用户id查询当前用户能看的菜单
     * @param
     * @param
     * @return
     */
    @Query(value="SELECT " +
            "            m.id, " +
            "            m.menu_name, " +
            "            m.menu_url, " +
            "            m.open_type, " +
            "            m.parent_id, " +
            "            m.menu_component," +
            "            m.menu_icon, " +
            "            m.menu_order, " +
            "            m.menu_type, " +
            "            m.is_show, " +
            "            m.is_route, " +
            "            m.is_menu, " +
            "            m.issued, " +
            "            m.menu_status, " +
            "            m.create_time, " +
            "m.method_type  " +
            "        FROM " +
            "            sys_menu m " +
            "        LEFT JOIN sys_relation_rm rd ON m.id = rd.menu_id " +
            "        LEFT JOIN sys_relation_ru ur ON rd.role_id = ur.role_id " +
            "        WHERE " +
            "            is_menu = :menuType " +
            "        AND m.menu_status = 1 " +
            "        AND (ur.user_id = :userId OR issued = 2) " +
            "        ORDER BY " +
            "            menu_order DESC",nativeQuery=true)
    List<DbSysMenu> getListByUserId(@Param("userId") Integer userId, @Param("menuType") int type);

    /**
     * 根据角色id，查询当前角色所拥有的菜单
     * @param id
     * @return
     */
    @Query(value="select  m.id,menu_name,menu_url,open_type,parent_id,menu_component,menu_icon,menu_order,menu_type, " +
            "       is_show,is_route,is_menu,issued,menu_status,create_time,method_type from sys_menu m ,sys_relation_rm dm " +
            "       where m.id=dm.menu_id and dm.role_id=:roleId and m.menu_status=1 order by m.menu_order desc",nativeQuery=true)
    List<DbSysMenu> getMenuListByRoleId(@Param("roleId") String id);

    /**
     * 根据角色的id，获取当前用户所有的权限
     * @param
     * @return
     */
    @Query(value="select u.* from sys_relation_ru t left join sys_role m on t.role_id=m.id LEFT JOIN sys_relation_rm n on m.id=n.role_id LEFT JOIN sys_menu u on n.menu_id= u.id where u.is_menu=0 and u.menu_status=1 and ( (t.user_id=:userId and u.issued=0) or u.issued = 2) order by u.menu_order DESC",nativeQuery=true)
    List<DbSysMenu> findAllAuthoritiesByUserIdThroughRole(@Param("userId") Integer userId);

    /**
     * 查找所有的公共权限
     * @return
     */
    @Query(value="select t.* from sys_menu t where t.issued = 2 and t.menu_status <> 0",nativeQuery=true)
    List<DbSysMenu> findAllPublicAuthorities();
}
