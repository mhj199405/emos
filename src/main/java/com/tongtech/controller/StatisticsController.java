package com.tongtech.controller;

import com.tongtech.auth.data.db_sys_department.DbSysDepartment;
import com.tongtech.auth.data.db_sys_department.DbSysDepartmentRepository;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.data.db_sys_user.DbsysUserRepository;
import com.tongtech.common.vo.StatInsVO;
import com.tongtech.common.vo.StatNodeVO;
//import com.tongtech.dao.entity.SysDepartment;
//import com.tongtech.dao.entity.SysUser;
//import com.tongtech.dao.repository.SysDepartmentRepository;
//import com.tongtech.dao.repository.SysUserRepository;
import com.tongtech.service.statistice.NodeInsService;
import com.tongtech.service.statistice.ProcInsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StatisticsController {
    /**
     * 流程实例统计
     *
     * @param procId 流程 id  bpm_def_proc_t
     * @param insProcState 完成状态  1:运行中, 2:结束 bpm_ins_proc_t
     * @param state 异常状态 2超时  1正常
     * @param date 日期范围   ？？？？？
     * //     * @param unit 单位
     * @param deptId 部门 id
     * @param optPersionId 用户id
     * @return
     */
    @Autowired
    ProcInsService procInsService;
    @Autowired
    NodeInsService nodeInsService;
    @Autowired
    DbsysUserRepository sysUserRepository;
    @Autowired
    DbSysDepartmentRepository sysDepartmentRepository;

    @GetMapping("/statistics/user")
    public List<DbSysUser> getUser(){
        List<DbSysUser> user = new ArrayList<>();
        user=sysUserRepository.findAll();
        return user;
    }

    @GetMapping("/statistics/dept")
    public List<DbSysDepartment> getDept(){
        List<DbSysDepartment> dept = new ArrayList<>();
        dept=sysDepartmentRepository.findAll();
        return dept;
    }


    @GetMapping("/statistics/defproc")
    public StatInsVO getDefProc(@RequestParam(required = false) Integer procId,
                                     @RequestParam(required = false) Integer insProcState,
                                     @RequestParam(required = false) Integer state,
                                     @RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate,
//                                      @RequestParam(required = false) int unit,
                                     @RequestParam(required = false) Integer deptId,
                                     @RequestParam(required = false) Integer optPersionId) {

        StatInsVO bpmInsVOList =
                procInsService.getProcIns(procId, insProcState, state,
                        startDate,endDate, deptId, optPersionId);
        return bpmInsVOList;

    }

    @GetMapping("/statistics/defnode")
    public StatNodeVO getDefNode(@RequestParam(required = false) Integer procId,
                                @RequestParam(required = false) Integer insProcState,
                                @RequestParam(required = false) Integer state,
                                @RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate,
//                                      @RequestParam(required = false) int unit,
                                @RequestParam(required = false) Integer deptId,
                                @RequestParam(required = false) Integer optPersionId) {

        StatNodeVO statNodeVO =
                nodeInsService.getDefNode(procId, insProcState, state,
                        startDate,endDate, deptId, optPersionId);
        return statNodeVO;

    }
}
