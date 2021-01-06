package com.tongtech.auth.service;//package org.ch.service;
//
//import org.ch.data.db_sys_department.DbSysDepartment;
//import org.ch.data.db_sys_menu.DbSysMenu;
//import org.ch.data.db_sys_operation.DbSysOperation;
//import org.ch.data.db_sys_role.DbSysRole;
//import org.ch.data.db_sys_user.DbSysUser;
//import org.ch.vo.CreateUserVo;
//import org.ch.vo.RoleResultVo;
//import org.ch.vo.SetRoleVo;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.HashMap;
//import java.util.List;
//
//public interface ApplicationPlatfromInterface {
//    /**
//     * 获取登录配置信息
//     */
//    HashMap<String, Object> getConfig();
//
//    /**
//     * 获取所有的部门信息
//     * @return
//     */
//    List<DbSysDepartment> getDepartments();
//
//    /**
//     * 创建部门信息
//     * @return
//     * @param dbSysDepartment
//     */
//    Boolean createDepartment(DbSysDepartment dbSysDepartment);
//
//    /**
//     * 删除部门信息
//     * @param deptId
//     * @return
//     */
//    Boolean deleteDepartment(Integer deptId);
//
//    /**
//     * 更新部门信息
//     * @param dbSysDepartment
//     * @return
//     */
//    Boolean updateDepartment(DbSysDepartment dbSysDepartment);
//
//    /**
//     * 查询功能信息
//     * @return
//     */
////    List<DbSysOperation> getOperations();
//
//    /**
//     * 删除功能信息
//     * @param operId
//     * @return
//     */
//    boolean deleteOperation(Integer operId);
//
//    /**
//     * 创建功能信息
//     * @param dbSysOperation
//     * @return
//     */
////    boolean createOperation(DbSysOperation dbSysOperation);
//
//    /**
//     * 更新功能信息
//     * @param dbSysOperation
//     * @return
//     */
////    boolean updateOperation(DbSysOperation dbSysOperation);
//
//    /**
//     * 获取角色信息
//     * @return
//     */
//    List<RoleResultVo> getRoles();
//
//    /**
//     * 删除角色
//     * @param roleId
//     * @return
//     */
//    boolean deleteRoles(Integer roleId);
//
//    /**
//     * 更新角色
//     * @param dbSysRole
//     * @return
//     */
//    boolean updateRoles(DbSysRole dbSysRole);
//
//    /**
//     * 创建角色
//     * @param dbSysRole
//     * @return
//     */
//    Integer createRoles(DbSysRole dbSysRole);
//
////    /**
////     * 设置角色的权限
////     * @param setRoleVo
////     * @return
////     */
////    boolean setRole(SetRoleVo setRoleVo);
//
//    /**
//     * 获取用户的信息
//     * @param page
//     * @param size
//     */
//    HashMap<String, Object> getUserInfo(Integer page, Integer size);
//
//    /**
//     * create user
//     * @param createUserVo
//     * @return
//     */
//    boolean createUser(CreateUserVo createUserVo);
//
//    /**
//     * delete user
//     * @param userId
//     * @return
//     */
//    boolean deleteUser(Integer userId);
//
//    /**
//     * update user
//     * @param createUserVo
//     * @return
//     */
//    boolean updateUser(CreateUserVo createUserVo);
//
//    /**
//     * judge weather user exist
//     * @param username
//     * @return
//     */
//    boolean judgeUserExist(String username);
//
//    /**
//     * 获取所有的菜单列表
//     * @return
//     */
//    List<DbSysMenu> getAllMenu();
//
//    /**
//     * 获取素有的用户列表
//     * @return
//     */
//    List<DbSysUser> getAllUser();
//
//    /**
//     * 获取当前用户能访问的部门
//     * @return
//     */
//    List<DbSysDepartment> getAccessedDepartment();
//
//    /**
//     * 上传文件到后端服务器
//     * @param file
//     * @return
//     */
//    String uploadImages(MultipartFile file);
//}
