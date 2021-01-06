package com.tongtech.auth.log;

import com.alibaba.fastjson.JSON;
import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_log.SysLog;
import com.tongtech.auth.data.db_sys_log.SysLogRepository;
import com.tongtech.auth.utils.IPUtil;
import com.tongtech.auth.vo.RestResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志：切面处理类
 */
@Aspect
@Component
public class SysLogAspect {
    private SysLogRepository logService;

    @Autowired
    private void setLogService(SysLogRepository logService) {
        this.logService = logService;
    }

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(com.tongtech.auth.log.MyLog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning(value = "logPoinCut()", returning = "keys")
    public void saveSysLog(@NotNull JoinPoint joinPoint, Object keys) {
        //保存日志
        SysLog sysLog = new SysLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取操作
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            String value = myLog.value();
            sysLog.setOperation(value);//保存获取的操作
            sysLog.setLogType(String.valueOf(myLog.type()));
        }

        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysLog.setMethod(className + "." + methodName);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        //joinPoint.getTarget()
        //将参数所在的数组转换成json
        String params = JSON.toJSONString(args);
        sysLog.setParams(params);

        sysLog.setCreateTime(new Date());
        //获取用户名
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && principal instanceof CustomUser){
            CustomUser customUser= (CustomUser) principal;
            sysLog.setUserId(customUser.getVoUserMenu().getId());
            }else {
            sysLog.setUserId(-1);
        }
        if (keys instanceof RestResult) {
            RestResult jr = (RestResult) keys;
            Integer resultCode=jr.getStatus();
            if (resultCode != null) {
                sysLog.setIsSuccess(resultCode);
            }else {
                //2的表示成功与否未知的
                sysLog.setIsSuccess(2);
            }
        }else {
            sysLog.setIsSuccess(2);
        }
        //获取用户ip地址
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        sysLog.setIp(IPUtil.getIpAddr(request));

        //调用service保存SysLog实体类到数据库
        logService.save(sysLog);
    }
}
