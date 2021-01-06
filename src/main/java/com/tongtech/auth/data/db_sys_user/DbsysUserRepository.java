package com.tongtech.auth.data.db_sys_user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DbsysUserRepository extends JpaRepository<DbSysUser,Integer>,JpaSpecificationExecutor<DbSysUser> {

    /**
     * 根据用户登陆名称获取当前用户信息
     * @param loginName
     * @return
     */
    @Query(value="select t.* from sys_user t where t.login_name=:loginName and (t.`status` != 0 or t.`status` is NULL)",nativeQuery=true)
    DbSysUser findByLoginName(@Param("loginName") String loginName);

    /**
     * 将当前用户的状态进行锁定
     * @param id
     */
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    @Modifying
    @Query(value="update  sys_user t set t.status=0 where t.id=:idNum",nativeQuery=true)
    void updateUserAccountStatus(@Param("idNum") Integer id);

    /**
     * 根据部门列表查找该列表下的用户
     * @param deptIdList
     * @return
     */
    @Query(value="select t.* from sys_user t where t.dept_id in (:deptIds)",nativeQuery=true)
    List<DbSysUser> findUsersByDeptIds(@Param("deptIds") List<Integer> deptIdList);

//    void deleteByUserId(Integer userId);
//    Integer countAllByLoginName(String loginName);
}
