package com.tongtech.auth.controller;//package org.ch.controller;
//
//import org.ch.data.db_sys_department.DbSysDepartment;
//import org.ch.data.db_sys_menu.DbSysMenu;
//import org.ch.data.db_sys_operation.DbSysOperation;
//import org.ch.data.db_sys_role.DbSysRole;
//import org.ch.data.db_sys_user.DbSysUser;
//import org.ch.service.ApplicationPlatfromInterface;
//import org.ch.vo.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.repository.query.Param;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/api")
//public class ApplicationPlatfromController {
//
//    @Autowired
//    private ApplicationPlatfromInterface applicationPlatfromInterface;
//
//    /**
//     * 获取登陆配置信息
//     * @return
//     */
//    @GetMapping("/getconfig")
//    public RestResult getConfig(){
//        HashMap<String, Object> resultMap = applicationPlatfromInterface.getConfig();
//        if (resultMap!=null){
//           return RestResultFactory.createSuccessResult(resultMap);
//        }
//       return RestResultFactory.createFailedResult("process failed!");
//    }
//
//    /**
//     * 获取部门信息（测试没问题）
//     */
//    @GetMapping("/access/getdepartments")
//    public RestResult<List<DbSysDepartment>> getDepartments(){
//       List<DbSysDepartment> dbSysDepartments= applicationPlatfromInterface.getDepartments();
//       return RestResultFactory.createSuccessResult(dbSysDepartments);
//    }
//
//    /**
//     * 创建部门信息（测试没问题）
//     * @param dbSysDepartment
//     * @return
//     */
//    @PostMapping("/access/department")
//    public RestResult createDepartment(@RequestBody DbSysDepartment dbSysDepartment){
//       dbSysDepartment.setCreateTime(new Date());
//       Boolean flag= applicationPlatfromInterface.createDepartment(dbSysDepartment);
//       if (flag){
//         return  RestResultFactory.createSuccessResult("create success");
//       }
//       return  RestResultFactory.createFailedResult("create failed");
//    }
//
//    /**
//     * 删除部门信息（删除部门信息没有问题）
//     * @param deptId
//     * @return
//     */
//    @DeleteMapping("/access/department")
//    public RestResult deleteDepartment(Integer deptId){
//       Boolean flag= applicationPlatfromInterface.deleteDepartment(deptId);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("delete success");
//        }
//        return  RestResultFactory.createFailedResult("delete failed");
//    }
//
//    /**
//     * 更新部门信息（测试没问题，注意有几个字段必须唯一）
//     * @param dbSysDepartment
//     * @return
//     */
//    @PutMapping("/access/department")
//    public  RestResult updateDepartment(@RequestBody DbSysDepartment dbSysDepartment){
//        dbSysDepartment.setUpdateTime(new Date());
//        Boolean flag= applicationPlatfromInterface.updateDepartment(dbSysDepartment);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("update success");
//        }
//        return  RestResultFactory.createFailedResult("update failed");
//    }
//    /**
//     * 获取功能信息（测试成功）
//     * @return
//     */
//    @GetMapping("/access/getoperations")
//    public RestResult getOperations(){
//      List<DbSysOperation> dbSysOperationList= applicationPlatfromInterface.getOperations();
//      if (dbSysOperationList==null){
//          return RestResultFactory.createFailedResult("query failed");
//      }
//      return RestResultFactory.createSuccessResult(dbSysOperationList);
//    }
//
//    /**
//     * 删除功能（测试成功）
//     * @param operId
//     * @return
//     */
//    @DeleteMapping("/access/operation")
//    public RestResult deleteOperation(Integer operId){
//    boolean flag= applicationPlatfromInterface.deleteOperation(operId);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("delete success");
//        }
//        return  RestResultFactory.createFailedResult("delete failed");
//    }
//
//    /**
//     * 创建功能信息（测试成功）
//     * @return
//     */
//    @PostMapping("/access/operation")
//    public RestResult createOperation(@RequestBody DbSysOperation dbSysOperation){
//        boolean flag=applicationPlatfromInterface.createOperation(dbSysOperation);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("create success");
//        }
//        return  RestResultFactory.createFailedResult("create failed");
//    }
//
//    /**
//     * 更新功能信息（测试成功）
//     * @param dbSysOperation
//     * @return
//     */
//    @PutMapping("/access/operation")
//    public RestResult updateOperation(@RequestBody DbSysOperation dbSysOperation){
//        dbSysOperation.setUpdateTime(new Date());
//        boolean flag=applicationPlatfromInterface.updateOperation(dbSysOperation);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("update success");
//        }
//        return  RestResultFactory.createFailedResult("update failed");
//    }
//
//    /**
//     * 获取角色信息（测试成功）
//     * @return
//     */
//    @GetMapping("/access/getroles")
//    public RestResult getRoles(){
//        List<RoleResultVo> resultMap = applicationPlatfromInterface.getRoles();
//        if (resultMap==null){
//              return RestResultFactory.createFailedResult("get failed!");
//          }
//          return RestResultFactory.createSuccessResult(resultMap);
//    }
//
//    /**
//     * 删除角色（测试成功）
//     */
//    @DeleteMapping("/access/role")
//    public RestResult deleteRoles(Integer roleId){
//       boolean flag= applicationPlatfromInterface.deleteRoles(roleId);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("delete success");
//        }
//        return  RestResultFactory.createFailedResult("delete failed");
//    }
//
//    /**
//     * 更新角色（测试成功）
//     * @return
//     */
//    @PutMapping("/access/role")
//    public RestResult updateRoles(@RequestBody DbSysRole dbSysRole){
//        boolean flag=applicationPlatfromInterface.updateRoles(dbSysRole);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("update success");
//        }
//        return  RestResultFactory.createFailedResult("update failed");
//    }
//
//    /**
//     * 创建角色（测试成功）
//     */
//    @PostMapping("/access/role")
//    public RestResult createRoles(@RequestBody DbSysRole dbSysRole){
//        Integer num=applicationPlatfromInterface.createRoles(dbSysRole);
//        switch (num){
//            case 0:
//                return RestResultFactory.createSuccessResult("create success");
//            case -2:
//                return RestResultFactory.createFailedResult("角色名称不能为空");
//            case -3:
//                return RestResultFactory.createFailedResult("角色名称已存在");
//            default:
//                return RestResultFactory.createFailedResult("create failed");
//        }
//    }
//
//    /**
//     * 设置角色权限信息（测试成功）
//     * @return
//     */
//    @PostMapping("/access/setrole")
//    public RestResult setRole(@RequestBody SetRoleVo setRoleVo){
//        boolean flag= applicationPlatfromInterface.setRole(setRoleVo);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("set authority success");
//        }
//        return  RestResultFactory.createFailedResult("set authority failed");
//    }
//
//    /**
//     * 获取用户信息（测试成功）
//     */
//    @GetMapping("/access/getusers")
//    public RestResult getUserInfo(Integer page,Integer size){
//        HashMap<String, Object> resultMap = applicationPlatfromInterface.getUserInfo(page, size);
//        if (resultMap!=null){
//            return RestResultFactory.createSuccessResult(resultMap);
//        }
//        return RestResultFactory.createFailedResult("there is no user in database!");
//    }
//
//    /**
//     * create user（测试成功）
//     */
//    @PostMapping("/access/user")
//    public RestResult createUser(@RequestBody CreateUserVo createUserVo){
//        boolean flag= applicationPlatfromInterface.createUser(createUserVo);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("create user success");
//        }
//        return  RestResultFactory.createFailedResult("create user failed");
//    }
//    /**
//     * delete user（测试成功）
//     */
//    @DeleteMapping("access/user")
//    public RestResult deleteUser(Integer userId){
//        boolean flag= applicationPlatfromInterface.deleteUser(userId);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("delete user success");
//        }
//        return  RestResultFactory.createFailedResult("delete user failed");
//    }
//
//    /**
//     * update user(测试成功)
//     */
//    @PutMapping("/access/user")
//    public RestResult updateUser(@RequestBody CreateUserVo createUserVo){
//        boolean flag= applicationPlatfromInterface.updateUser(createUserVo);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("update user success");
//        }
//        return  RestResultFactory.createFailedResult("update user failed");
//    }
//
//    /**
//     *judge weather the user exist(测试成功)
//     */
//    @GetMapping("access/exist")
//    public RestResult judgeUserExist(String username){
//        boolean flag= applicationPlatfromInterface.judgeUserExist(username);
//        if (flag){
//            return  RestResultFactory.createSuccessResult("user exist");
//        }
//        return  RestResultFactory.createFailedResult("user no exist");
//    }
//
//    /**
//     * 获取所有的菜单选项
//     */
//    @GetMapping("/access/getAllMenu")
//    public RestResult getAllMenu(){
//        List<DbSysMenu> menuList= applicationPlatfromInterface.getAllMenu();
//        if (menuList!=null){
//            return RestResultFactory.createSuccessResult(menuList);
//        }
//        return RestResultFactory.createFailedResult("there is no menu in database!");
//    }
//
//    /**
//     * 获取所有的用户列表
//     * @return
//     */
//    @GetMapping("/access/getAllUser")
//    public RestResult getAllUser(){
//        List<DbSysUser> dbSysUserList= applicationPlatfromInterface.getAllUser();
//        if (dbSysUserList!=null){
//            return RestResultFactory.createSuccessResult(dbSysUserList);
//        }
//        return RestResultFactory.createFailedResult("there is no user in database!");
//    }
//
//    /**
//     * 获取当前用户能访问的部门
//     * @return
//     */
//    @GetMapping("/access/getAccessedDepartment")
//    public RestResult getAccessedDepartment(){
//        List<DbSysDepartment> dbSysDepartmentList= applicationPlatfromInterface.getAccessedDepartment();
//        if (dbSysDepartmentList!=null){
//            return RestResultFactory.createSuccessResult(dbSysDepartmentList);
//        }
//        return RestResultFactory.createFailedResult("there is no department for user  in database!");
//    }
//
//    /**
//     * 上传文件到后端服务器
//     * @param file
//     * @return
//     */
//    @PostMapping("/access/upload")
//    public RestResult uploadImages(@RequestParam("file")MultipartFile file){
//       String url= applicationPlatfromInterface.uploadImages(file);
//        if (url==null){
//            return RestResultFactory.createFailedResult("image upload fail!");
//        }
//        return RestResultFactory.createSuccessResult(url);
//    }
//}
