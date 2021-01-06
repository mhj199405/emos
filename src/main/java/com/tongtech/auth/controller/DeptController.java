package com.tongtech.auth.controller;

import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_department.DbSysDepartment;
import com.tongtech.auth.data.db_sys_department.DbSysDepartmentRepository;
import com.tongtech.auth.service.DeptService;
import com.tongtech.auth.vo.RestResult;
import com.tongtech.auth.vo.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class DeptController {
    @Autowired
    private DbSysDepartmentRepository dbSysDepartmentRepository;

    @Autowired
    private DeptService deptService;

    @RequestMapping(value = "/sys/dept/operation", method = RequestMethod.GET)
    public RestResult GetDataControlList(@RequestParam Map<String, Object> model) {
        Integer depId = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            return RestResultFactory.createFailedResult("");
        }
        if (principal instanceof CustomUser) {
            CustomUser customUser = (CustomUser) principal;
            depId = customUser.getVoUserMenu().getDeptId();
        }
        if (depId == null) {
            return RestResultFactory.createFailedResult("");
        } else {
            Optional<DbSysDepartment> depOptional = dbSysDepartmentRepository.findById(depId);
            if (!depOptional.isPresent()) {
                return RestResultFactory.createFailedResult("");
            } else {
                //获取到了当前部门，获取它的子部门
                DbSysDepartment dbSysDepartment = depOptional.get();
                buildDbSysDepartment(dbSysDepartment);
                return RestResultFactory.createSuccessResult(dbSysDepartment);
            }
        }
    }

    private void buildDbSysDepartment(DbSysDepartment dbSysDepartment) {
        Integer deptId = dbSysDepartment.getId();
        //根据当前部门id查找它的子部门
        List<DbSysDepartment> departments = dbSysDepartmentRepository.findAllDepartmentByParentId(deptId);
        if (departments.size() > 0) {
            dbSysDepartment.setChildren(departments);
            for (DbSysDepartment department : departments) {
                buildDbSysDepartment(department);
            }
        }
    }

    @RequestMapping(value = "/sys/dept/operation", method = RequestMethod.DELETE)
    public RestResult DeleteControl(@RequestParam Integer id) {

        RestResult restResult = deptService.deleteControl(id);
        return restResult;

    }


    @RequestMapping(value = "/sys/dept/operation", method = RequestMethod.PUT)
    public RestResult UpdateControl(@RequestBody DbSysDepartment model) {

        RestResult restResult = deptService.updateControl(model);
        return restResult;
    }

    @RequestMapping(value = "/sys/dept/operation", method = RequestMethod.POST)
    public RestResult InsertControl(@RequestBody DbSysDepartment model) {

        RestResult restResult = deptService.insertControl(model);
        return restResult;
    }

    @RequestMapping(value = "/sys/dept/getbaseinfo", method = RequestMethod.GET)
    public RestResult GetBaseInfo() {

        RestResult restResult = deptService.getBaseInfo();
        return restResult;
    }

    @RequestMapping(value = "/sys/dept/getdeptinfo", method = RequestMethod.GET)
    public RestResult GetDeptInfo(@RequestParam Integer id) {

        RestResult restResult = deptService.getDeptInfo(id);
        return restResult;
    }


    @RequestMapping(value = "/sys/dept/authorized", method = RequestMethod.POST)
    public RestResult onAuthorized(@RequestBody Map<String, Object> dict) {

        System.out.println(dict);
        RestResult restResult = deptService.onAuthorized(dict);
        return restResult;
    }

    @GetMapping("/sys/dept/getAllDepartment")
    public RestResult getAllDepartment(@RequestParam(name = "deptName",defaultValue = "")String deptName,
                                       @RequestParam(name = "pageIndex",defaultValue = "1")Integer pageIndex,
                                       @RequestParam(name = "pageSize",defaultValue = "10")Integer  pageSize){

        RestResult restResult = deptService.getAllDepartment(deptName,pageIndex,pageSize);
        return restResult;
    }

}
