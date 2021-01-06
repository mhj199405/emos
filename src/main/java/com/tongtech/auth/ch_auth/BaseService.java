package com.tongtech.auth.ch_auth;

import com.tongtech.auth.data.db_sys_log.SysLog;
import com.tongtech.auth.data.db_sys_log.SysLogRepository;
import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_menu.DbSysMenuRepository;
import com.tongtech.auth.data.db_sys_user.DbsysUserRepository;
import com.tongtech.auth.log.SaveLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class BaseService {

    @Autowired
    private DbSysMenuRepository dbSysMenuRepository;
	@Autowired
    private DbsysUserRepository dbsysUserRepository;
    @Autowired
    private SysLogRepository sysLogRepository;
    @Autowired
    private SaveLogEntity saveLogEntity;

	public List<DbSysMenu> findDbSysOperationAll() {
        List<DbSysMenu> allAuthorities = dbSysMenuRepository.findAllAuthorities();
        return allAuthorities;
    }
//	//获取当前登录的用户
//	public DbSysUser findCurrentUser(String username){
//        Optional<DbSysUser> byLoginName = dbsysUserRepository.findByLoginName(username);
//        if (byLoginName.isPresent()){
//            DbSysUser dbSysUser = byLoginName.get();
//            return dbSysUser;
//        }
//        return null;
//    }

    /**
     * 判断是否可以再次登录，如果锁定用户，返回false；如果不满足锁定条件，返回true;
     * @return
     */
    public Boolean findWrongTryNumOfLogin(Integer userId){
        List<SysLog> loginList = sysLogRepository.findWrongTryNumOfLogin(userId);
        if (loginList.size()==0){
            return true;
        }
        int num=0;
        for (SysLog sysLog : loginList) {
            if (sysLog.getIsSuccess()!=null && sysLog.getIsSuccess()==0){
                return true;
            }
            num++;
        }
        if (num==30){
            return false;
        }
        return true;
    }

    /**
     * 将当前用户的账号进行锁定
     * @param id
     */
    public void updateUserAccountStatus(Integer id) {
        dbsysUserRepository.updateUserAccountStatus(id);
    }

    /**
     * 保存日志
     */
    public void saveLogEntity(String username,Boolean flag){
        saveLogEntity.saveLog(username,flag);
    }
}
