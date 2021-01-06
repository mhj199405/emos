package com.tongtech.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tongtech.auth.data.db_sys_data_control.SysDataControl;
import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_role.DbSysRole;
import com.tongtech.auth.service.RoleService;
import com.tongtech.auth.vo.RestResult;
import com.tongtech.auth.vo.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/sys/role/operation", method = RequestMethod.GET)
    public RestResult GetDataControlList(@RequestParam Map<String, Object> model) {

        RestResult restResult = roleService.getDataControlList(model);
        return restResult;
    }

    @RequestMapping(value = "/sys/role/operation", method = RequestMethod.DELETE)
    public RestResult DeleteRole(@RequestParam String id) {
        if (id == null) {
            return RestResultFactory.createFailedResult("删除角色ID不能为空");
        }
        RestResult restResult= roleService.deleteRole(id);
        return restResult;
    }

    @RequestMapping(value = "/sys/role/operation", method = RequestMethod.PUT)
    public RestResult UpdateRole(@RequestBody DbSysRole model) {
        if (model.getRoleName().length() == 0) {
            return RestResultFactory.createFailedResult("角色名称不能为空");
        }
        RestResult restResult= roleService.updateRole(model);
        return restResult;
    }

    @RequestMapping(value = "/sys/role/operation", method = RequestMethod.POST)
    public RestResult InsertRole(@RequestBody DbSysRole model) {
        if (model == null) {
            return RestResultFactory.createFailedResult("参数异常");
        }
        if (model.getRoleName().length() == 0) {
            return RestResultFactory.createFailedResult("角色名称不能为空");
        }
        RestResult restResult = roleService.insertRole(model);
        return restResult;
    }

    @RequestMapping(value = "/sys/role/getroleinfo", method = RequestMethod.GET)
//    @RequestMapping(value = "/sys/role/getRoleInfo", method = RequestMethod.GET)
    public RestResult GetRoleInfo(@RequestParam String id) {
       RestResult restResult = roleService.getRoleInfo(id);
       return restResult;
    }

    /**
     * 角色授权
     * @param dict
     * @return
     */
    @RequestMapping(value = "/sys/role/authorized", method = RequestMethod.POST)
    public RestResult onAuthorized(@RequestBody Map<String, Object> dict) {
        String roleId = dict.get("id").toString();
        List<DbSysMenu> menuList = JSON.parseArray(JSONArray.toJSONString(dict.get("menuList")), DbSysMenu.class);
        List<SysDataControl> controlList = JSON.parseArray( JSONArray.toJSONString(dict.get("controlList")), SysDataControl.class);
        RestResult restResult = roleService.onAuthorized(roleId,menuList,controlList);
        return restResult;
    }
}
