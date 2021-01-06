package com.tongtech.dao.repository.analysis;

import com.tongtech.dao.entity.TawSystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TawSystemRoleRepository  extends JpaSpecificationExecutor<TawSystemRole>, JpaRepository<TawSystemRole, Integer> {

    void deleteByRoleIdAndDeptId(Integer roleId, String deptId);

    List<TawSystemRole> findAllByDeptId(String deptId);

    @Query(value="select t from TawSystemRole  t where t.deleted = ?2 and t.roleName like ?1 ")
    List<TawSystemRole> findAllByDeptIdAndRoleNames(String roleName1, String deptId);


    List<TawSystemRole> findAllByParentId(Integer parentId);

    /**
     * 根据用户id，判断当前用户所拥有的所有角色
     * @param userId
     * @return
     */
    @Query(value="select t.* from taw_system_user m  JOIN taw_system_userrefrole n ON n.userid=m.id  JOIN taw_system_role t ON t.role_id=n.roleid WHERE m.userid=:userId ",nativeQuery = true)
    List<TawSystemRole> findUserRoles(@Param("userId") String userId);


    @Query(value="select t.* from taw_system_user m  JOIN taw_system_userrefrole n ON n.userid=m.id  JOIN taw_system_role t ON t.role_id=n.roleid WHERE m.id=:userId ",nativeQuery = true)
    List<TawSystemRole> findUserRoles1(@Param("userId") String userId);

}
