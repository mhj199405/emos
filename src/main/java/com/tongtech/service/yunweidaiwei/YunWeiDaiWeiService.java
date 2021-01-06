package com.tongtech.service.yunweidaiwei;

import com.tongtech.common.vo.RestResult;
import com.tongtech.dao.entity.TawSystemDept;
import com.tongtech.dao.entity.TawSystemRole;
import com.tongtech.dao.entity.TawSystemUser;

public interface YunWeiDaiWeiService {
    /**
     *
     * @return 添加角色
     * @param role
     */
    RestResult addRole(TawSystemRole role);

    /**
     * 删除角色
     *
     * @param
     * @param roleId
     * @return
     */
    RestResult deleteRole(String deptId, Integer roleId);

    /**
     * 修改角色
     * @param role
     * @return
     */
    RestResult modifyRole(TawSystemRole role);

    /**
     * 查询角色
     * @param roleName
     * @param deptId
     * @return
     */
    RestResult queryRole(String roleName, String deptId);


    /**
     * 查询角色，构建树状结构
     * @param deptId
     * @return
     */
     RestResult queryRoleByTree(String deptId);

    /**
     * 添加部门
     * @param dept
     * @return
     */
    RestResult addDept(TawSystemDept dept);

    /**
     * 删除部门
     * @param id
     * @return
     */
    RestResult deleteDept(Integer id);

    /**
     * 修改部门
     * @param dept
     * @return
     */
    RestResult modifyDept(TawSystemDept dept);

    /**
     * 查询部门
     * @param deptName
     * @param deptId
     * @return
     */
    RestResult queryDept(String deptName, String deptId);

    /**
     * 添加用户
     * @param user
     * @return
     */
    RestResult addUser(TawSystemUser user);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    RestResult deleteUser(String userId);

    /**
     * 修改用户
     * @param user
     * @return
     */
    RestResult modifyUser(TawSystemUser user);

    /**
     * 查询用户
     * @param userName
     * @param deptId
     * @return
     */
    RestResult queryUser(String userName, String deptId);

    /**
     * 查询当前登录用户的部门及其子部门
     * @return
     */
    RestResult queryCurrentLoginUserDept();

    /**
     * 查询用户和角色，构建树状结构
     * @param deptId
     * @return
     */
    RestResult queryTreeOfRoleAndUser(String deptId);


}
