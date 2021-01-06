package com.tongtech.auth.ch_auth;

//import ch.ch_auth.data.base_user_t.BaseUser;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.data.db_sys_user.DbsysUserRepository;
import com.tongtech.auth.log.LogType;
import com.tongtech.auth.log.MyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
public class AuthController {
    @Autowired
    private DbsysUserRepository dbsysUserRepository;

	@GetMapping("/sys/csrf")
	public CsrfToken csrf(CsrfToken token, HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        return csrf;
	}

/*	@GetMapping("/currentUser")
	public BaseUser  currentUser () {
		return CustomUser.getSessionBaseUser();
	}*/
    @MyLog(value = "测试查询",type = LogType.Query)
    @RequestMapping("/get")
    public void getUser() {
        Optional<DbSysUser> dbSysUserOptional = dbsysUserRepository.findById(1);
        DbSysUser dbSysUser = dbSysUserOptional.get();
        System.out.println("查询成功");
    }

    @RequestMapping("/save")
    @Transactional
    public  void saveUser(@RequestBody(required = false) DbSysUser dbSysUser){
        dbSysUser=new DbSysUser();
//        dbSysUser.setUserId(2);
        dbSysUser.setLoginName("laowang");
        dbSysUser.setPassword("123456");
        DbSysUser save = dbsysUserRepository.save(dbSysUser);
        System.out.println("保存成功");
    }

    @RequestMapping("/test/test1")
    public Object test0(){
        return "xxx";
    }
}
