package com.tongtech.controller;

import com.tongtech.common.vo.RestResult;
import com.tongtech.common.vo.RestResultFactory;
import com.tongtech.dao.entity.TawSystemDept;
import com.tongtech.dao.entity.TawSystemRole;
import com.tongtech.dao.entity.TawSystemUser;
import com.tongtech.service.yunweidaiwei.YunWeiDaiWeiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class YunWeiDaiWeiController {

    @Autowired
    private YunWeiDaiWeiService yunWeiDaiWeiService;
    /**
     * 角色部分
     */


    //添加角色
    @PostMapping("/daiyun/addRole")
    public RestResult addRole(@RequestBody TawSystemRole role){
        RestResult restResult = yunWeiDaiWeiService.addRole(role);
        return restResult;
    }

    @PostMapping("/daiyun/addRole1")
    public RestResult addRole1(@RequestBody TawSystemRole role){
        RestResult restResult = addRole(role);
        return restResult;
    }

    //删除角色
    @DeleteMapping("/daiyun/deleteRole")
    public RestResult deleteRole(@RequestParam String deptId,@RequestParam Integer roleId){
        RestResult restResult = yunWeiDaiWeiService.deleteRole(deptId,roleId);
        return restResult;
    }

    @DeleteMapping("/daiyun/deleteRole1")
    public RestResult deleteRole1(@RequestParam String deptId,@RequestParam Integer roleId){
        RestResult restResult = deleteRole(deptId,roleId);
        return restResult;
    }

    //修改角色
    @PutMapping("/daiyun/modifyRole")
    public RestResult modifyRole(@RequestBody TawSystemRole role){
        RestResult restResult = yunWeiDaiWeiService.modifyRole(role);
        return restResult;
    }

    @PutMapping("/daiyun/modifyRole1")
    public RestResult modifyRole1(@RequestBody TawSystemRole role){
        RestResult restResult = modifyRole(role);
        return restResult;
    }

    //查询角色
    @GetMapping("/daiyun/queryRole")
    public RestResult queryRole(@RequestParam(required = false,defaultValue = "") String roleName,@RequestParam String deptId){
        RestResult restResult = yunWeiDaiWeiService.queryRole(roleName,deptId);
        return  restResult;
    }

    //查询角色，构建树状结构
    @GetMapping("/daiyun/queryRoleByTree")
    public RestResult queryRoleByTree(@RequestParam String deptId){
        RestResult restResult = yunWeiDaiWeiService.queryRoleByTree(deptId);
        return restResult;
    }


    //查询角色，构建树状结构
    @GetMapping("/daiyun/queryRoleByTree1")
    public RestResult queryRoleByTree1(@RequestParam String deptId){
        RestResult restResult = yunWeiDaiWeiService.queryRoleByTree(deptId);
        return restResult;
    }

    //查询角色和用户，构建树状结构
    @GetMapping("/daiyun/queryRoleAndUser")
    public RestResult queryTreeOfRoleAndUser(@RequestParam String deptId){
        RestResult restResult = yunWeiDaiWeiService.queryTreeOfRoleAndUser(deptId);
        return restResult;
    }

    @GetMapping("/daiyun/queryRole1")
    public RestResult queryRole1(@RequestParam(required = false,defaultValue = "") String roleName,@RequestParam String deptId){
        RestResult restResult = queryRole(roleName,deptId);
        return  restResult;
    }
    /**
     * 部门部分
     */
    //添加部门
    @PostMapping("/daiyun/addDepartment")
    public RestResult addDepartment(@RequestBody TawSystemDept dept){
        RestResult restResult = yunWeiDaiWeiService.addDept(dept);
        return restResult;
    }

    @PostMapping("/daiyun/addDepartment1")
    public RestResult addDepartment1(@RequestBody TawSystemDept dept){
        RestResult restResult = addDepartment(dept);
        return restResult;
    }

    //删除部门
    @DeleteMapping("/daiyun/deleteDepartment")
    public RestResult deleteDepartment(@RequestParam Integer id){
        RestResult restResult = yunWeiDaiWeiService.deleteDept(id);
        return restResult;
    }

    @DeleteMapping("/daiyun/deleteDepartment1")
    public RestResult deleteDepartment1(@RequestParam Integer id){
        RestResult restResult = deleteDepartment(id);
        return restResult;
    }

    @PutMapping("/daiyun/modifyDepartment")
    public RestResult modifyDepartment(@RequestBody TawSystemDept dept){
       RestResult restResult = yunWeiDaiWeiService.modifyDept(dept);
        return restResult;
    }

    @PutMapping("/daiyun/modifyDepartment1")
    public RestResult modifyDepartment1(@RequestBody TawSystemDept dept){
        RestResult restResult = modifyDepartment(dept);
        return restResult;
    }

    @GetMapping("/daiyun/queryDepartment")
    public RestResult queryDepartment(@RequestParam(required = false,defaultValue = "") String deptName,@RequestParam String deptId){
        RestResult restResult = yunWeiDaiWeiService.queryDept(deptName,deptId);
        return  restResult;
    }

    @GetMapping("/daiyun/queryDepartment1")
    public RestResult queryDepartment1(@RequestParam(required = false,defaultValue = "") String deptName,@RequestParam String deptId){
        RestResult restResult = queryDepartment(deptName,deptId);
        return  restResult;
    }

    /**
     * 用户部分
     */

    /**
     * 添加用户
     */
    @PostMapping("/daiyun/addUser")
    public RestResult addUser(@RequestBody TawSystemUser user){
       RestResult restResult = yunWeiDaiWeiService.addUser(user);
       return restResult;
    }

    @PostMapping("/daiyun/addUser1")
    public RestResult addUser1(@RequestBody TawSystemUser user){
        RestResult restResult = addUser(user);
        return restResult;
    }

    /**
     * 删除用户
     * @return
     */
    @DeleteMapping("/daiyun/deleteUser")
    public RestResult deleteUser(@RequestParam String userId){
        RestResult restResult = yunWeiDaiWeiService.deleteUser(userId);
        return restResult;
    }

    @DeleteMapping("/daiyun/deleteUser1")
    public RestResult deleteUser1(@RequestParam String userId){
        RestResult restResult = deleteUser(userId);
        return restResult;
    }
    /**
     * 更新用户
     * @return
     */
    @PutMapping("/daiyun/modifyUser")
    public RestResult modifyUser(@RequestBody TawSystemUser user){
        RestResult restResult = yunWeiDaiWeiService.modifyUser(user);
        return restResult;
    }

    @PutMapping("/daiyun/modifyUser1")
    public RestResult modifyUser1(@RequestBody TawSystemUser user){
        RestResult restResult = modifyUser(user);
        return restResult;
    }

    /**
     * 查询用户
     * @return
     */
    @GetMapping("/daiyun/queryUser")
    public RestResult queryUser(@RequestParam(required = false,defaultValue = "") String userName,@RequestParam String deptId){
        RestResult restResult = yunWeiDaiWeiService.queryUser(userName,deptId);
        return restResult;
    }
    @GetMapping("/daiyun/queryUser1")
    public RestResult queryUser1(@RequestParam(required = false,defaultValue = "") String userName,@RequestParam String deptId){
        RestResult restResult = queryUser(userName,deptId);
        return restResult;
    }

    /**
     * 查询当前登录用户所在部门及其子部门
     */
    @GetMapping("/daiyun/queryCurrentUserDept")
    public RestResult queryCurrentLoginUserDept(){
       RestResult restResult = yunWeiDaiWeiService.queryCurrentLoginUserDept();
       return restResult;
    }
    @GetMapping("/daiyun/queryCurrentUserDept1")
    public RestResult queryCurrentLoginUserDept1(){
        RestResult restResult =queryCurrentLoginUserDept();
        return restResult;
    }
}
