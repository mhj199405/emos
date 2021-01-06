package com.tongtech.auth.data.db_sys_role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DbSysRoleRepository extends JpaRepository<DbSysRole,Integer>,JpaSpecificationExecutor<DbSysRole> {

    Integer countAllByRoleName(String roleName);

    /**
     * 查询deptId包含在子类id集合中的角色实体
     * @param children
     * @return
     */
    List<DbSysRole> findAllByDeptIdIn(List<Integer> children);

    /**
     * 根据id进行删除，且必须是不受保护的
     * @param parseInt
     */
    @Modifying
    @Query(value="delete from DbSysRole t where t.id=:roleId and t.beProtected <> 1")
    void deleteByIdWithNotProtected(@Param("roleId") int parseInt);

    /**
     * 根据用户的id查找当前用户所拥有的角色
     * @param id
     * @return
     */
    @Query(value="select n.* from sys_user t join sys_relation_ru m  on t.id = m.user_id join sys_role n on m.role_id=n.id where t.id =:userId",nativeQuery=true)
    List<DbSysRole> findAllByUserId(@Param("userId") Integer id);
}
