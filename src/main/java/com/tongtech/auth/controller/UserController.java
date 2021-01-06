package com.tongtech.auth.controller;

import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.service.UserService;
import com.tongtech.auth.vo.RestResult;
import com.tongtech.auth.vo.RestResultFactory;
import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/sys/user/operation1")
    public RestResult getAllUser(){
        RestResult restResult = userService.getAllUser();
        return restResult;
    }

    @GetMapping("/sys/user/operation2")
    public RestResult getAllUser1(){
        RestResult restResult = userService.getAllUser();
        return restResult;
    }

    @RequestMapping(value = "/sys/user/operation", method = RequestMethod.GET)
    public RestResult GetUserList(@RequestParam Map<String, Object> model) {
        RestResult restResult = userService.getUserList(model);
        return restResult;
    }

    @RequestMapping(value = "/sys/user/operation", method = RequestMethod.DELETE)
    public RestResult DeleteControl(@RequestParam String id) {
        if (id == null) {
            return RestResultFactory.createFailedResult("删除用户ID不能为空");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && principal instanceof CustomUser){
            CustomUser customUser= (CustomUser) principal;
            DbSysUser dbSysUser = customUser.getVoUserMenu();
            if (id.equals(dbSysUser.getId())) {
                return RestResultFactory.createFailedResult("当前用户不能删除当前用户");
            }
        }else {
            return RestResultFactory.createFailedResult("当前用户不存在");
        }
        RestResult restResult = userService.deleteControl(id);
        return restResult;
    }

    @RequestMapping(value = "/sys/user/operation", method = RequestMethod.PUT)
    public RestResult UpdateUser(@RequestParam String id, @RequestParam String operType, String value) {
       RestResult restResult = userService.updateUser(id,operType,value);
       return  restResult;
    }

    @RequestMapping(value = "/sys/user/operation", method = RequestMethod.POST)
    public RestResult InsertUser(@RequestBody DbSysUser model) {
        if (model == null) {
            return RestResultFactory.createFailedResult("参数异常");
        }
        if (model.getLoginName().length() == 0) {
            return RestResultFactory.createFailedResult("用户名不能为空");
        }
        if (model.getPassword().length() == 0) {
            return RestResultFactory.createFailedResult("密码不能为空");
        }
        if (model.getDeptId() == null) {
            return RestResultFactory.createFailedResult("部门不能为空");
        }
        RestResult restResult = userService.insertUser(model);
        return restResult;
    }

    @RequestMapping(value = "/sys/user/dept", method = RequestMethod.GET)
    public RestResult GetDept() {
       RestResult restResult = userService.getDept();
       return restResult;
    }

    @RequestMapping(value = "/sys/user/getuserinfo", method = RequestMethod.GET)
    public RestResult GetUserInfo(@RequestParam String id) {
        if (id == null) {
            return RestResultFactory.createFailedResult("用户ID不能为空");
        }
        RestResult restResult = userService.getUserInfo(id);
        return restResult;
    }
    @GetMapping("/sys/user/getAllUsers")
    public RestResult findAllUser(@RequestParam(name = "userName",defaultValue = "") String userName){
        RestResult restResult = userService.findAllUser(userName);
        return restResult;
    }

}
