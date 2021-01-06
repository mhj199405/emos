package com.tongtech.auth.log;

import com.tongtech.auth.data.db_sys_log.SysLog;
import com.tongtech.auth.data.db_sys_log.SysLogRepository;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.data.db_sys_user.DbsysUserRepository;
import com.tongtech.auth.utils.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class SaveLogEntity {
    @Autowired
    private DbsysUserRepository dbsysUserRepository;

    @Autowired
    private SysLogRepository sysLogRepository;
    @Transactional
    public void saveLog(String userName, boolean isSuccess) {
        SysLog sysLog = new SysLog();
        //获取操作
        sysLog.setOperation("登录");//保存获取的操作
        sysLog.setLogType(String.valueOf(LogType.Login));
        sysLog.setMethod("login");
        sysLog.setParams("no");
        sysLog.setIsSuccess(isSuccess ? 1 : 0);
        sysLog.setCreateTime(new Date());
        //获取用户名
        DbSysUser dbSysUser = dbsysUserRepository.findByLoginName(userName);
        if (dbSysUser==null||dbSysUser.getId()==null){
            sysLog.setUserId(-1);
        }else {
            sysLog.setUserId(dbSysUser.getId());
        }

        //获取用户ip地址
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        sysLog.setIp(IPUtil.getIpAddr(request));
        //调用service保存SysLog实体类到数据库
        sysLogRepository.save(sysLog);
    }
}
