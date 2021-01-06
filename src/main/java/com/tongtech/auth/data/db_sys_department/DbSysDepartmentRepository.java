package com.tongtech.auth.data.db_sys_department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DbSysDepartmentRepository extends JpaRepository<DbSysDepartment,Integer>,JpaSpecificationExecutor<DbSysDepartment> {
//    /**
//     * 根据用户姓名，查找当前用户所能够访问的所有部门
//     * @param name
//     * @return
//     */
//    @Query(value = "select t2.* from base_user t1 left join base_department t2 on t1.dept_id=t2.dept_id where t1.login_name=:loginName",nativeQuery = true)
//    List<DbSysDepartment> findDepartmentForOne(@Param("loginName") String name);

    /**
     * 根据部门id，查找当前用户所具有的权限
     */
    @Query(value="select c.id from sys_department a LEFT JOIN sys_relation_dm b on a.id=b.dept_id LEFT JOIN sys_menu c on b.menu_id=c.id where a.id=:deptId and c.is_menu=0 and c.issued=1 and c.menu_status=1 order by c.menu_order desc ",nativeQuery=true)
    List<Integer> findAllAuthoritiesByDepartmentId(@Param("deptId") Integer deptId);

    /**
     * 根据部门id,查找当前部门的所有子部门
     * @param id
     * @return
     */
    @Query(value="select t.* from sys_department t where t.parent_id=:deptId",nativeQuery=true)
    List<DbSysDepartment> findChildrenDeptById(@Param("deptId") Integer id);

    /**
     * 根据部门id进行部门的删除
     * 注：mysql当中的删除语句不识别表的别名，真实项目中级联删除是禁止使用的。
     * @param deptIdList
     */
    @Modifying
    @Transactional
    @Query(value="DELETE FROM sys_department  WHERE id in (:deptIds)",nativeQuery=true)
    void deleteDeptsByIds(@Param("deptIds") List<Integer> deptIdList);

    /**
     * 降序查询所有，by关键字不能少
     * @return
     */
    List<DbSysDepartment> findAllByOrderByOrderSortDesc();

    /**
     *根据父部门id查找所有的子部门
     * @param deptId
     * @return
     */
    @Query(value="select t.* from sys_department t WHERE t.parent_id=:deptId",nativeQuery=true)
    List<DbSysDepartment> findAllDepartmentByParentId(@Param("deptId") Integer deptId);
}
