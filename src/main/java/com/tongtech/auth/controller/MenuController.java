package com.tongtech.auth.controller;

import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.service.MenuService;
import com.tongtech.auth.vo.RestResult;
import com.tongtech.auth.vo.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/sys/menu/operation", method = RequestMethod.GET)
    public RestResult GetDataControlList(@RequestParam Map<String, Object> model) {

        RestResult restResult= menuService.getDataControlList(model);
        return restResult;
    }

    @RequestMapping(value = "/sys/menu/operation", method = RequestMethod.DELETE)
    public RestResult DeleteControl(@RequestParam List<Integer> id) {
        if (id == null) {
            return RestResultFactory.createFailedResult("删除数据ID不能为空");
        }
        RestResult restResult= menuService.deleteControl(id);
        return restResult;
    }


    @RequestMapping(value = "/sys/menu/operation", method = RequestMethod.PUT)
    public RestResult UpdateControl(@RequestBody DbSysMenu model) {

        RestResult restResult = menuService.updateControl(model);
        return restResult;
    }

    @RequestMapping(value = "/sys/menu/operation", method = RequestMethod.POST)
    public RestResult InsertControl(@RequestBody DbSysMenu model) {
        if (model == null) {
            return RestResultFactory.createFailedResult("参数异常");
        }
        if (model.getMenuName().length() == 0) {
            return RestResultFactory.createFailedResult("菜单名称不能为空");
        }
        model.setCreateTime(new Date());
        RestResult restResult=menuService.insertControl(model);
        return restResult;
    }
}
