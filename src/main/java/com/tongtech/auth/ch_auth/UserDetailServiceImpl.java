package com.tongtech.auth.ch_auth;


import com.tongtech.auth.data.db_sys_department.DbSysDepartmentRepository;
import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_menu.DbSysMenuRepository;
import com.tongtech.auth.data.db_sys_relation_dm.SysRelationDmRepository;
import com.tongtech.auth.data.db_sys_role.DbSysRoleRepository;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.data.db_sys_user.DbsysUserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	private static final String URL_ROLE_PREFIX = "ROLE_";
	
	@Autowired
	private DbsysUserRepository dbsysUserRepository;
	@Autowired
    private DbSysMenuRepository dbSysMenuRepository;
	@Autowired
	private DbSysRoleRepository dbSysRoleRepository;
//	@Autowired
//	private DbSysOperationRepository dbSysOperationRepository;
    @Autowired
    private DbSysDepartmentRepository dbSysDepartmentRepository;
    @Autowired
    private SysRelationDmRepository sysRelationDmRepository;
	@Autowired
    private EntityManager entityManager;
	@Autowired
    private BaseService baseService;
	//无论怎么验证，授权加载信息都是在这里进行的，url肯定是在这里搞定

    @SneakyThrows
    @Transactional
	@Override
	public UserDetails loadUserByUsername(String username)  {
        User                    user=null;
		List<GrantedAuthority>	authorities	= null;
        DbSysUser dbSysUser =null;
//        DbSysUser               dbSysUser=null;
        if (username==null){
            throw new UsernameNotFoundException("username: " + username + "can  not be null");
        }

        dbSysUser = dbsysUserRepository.findByLoginName(username);
        //当前用户不存在，我就给前端返回一个异常
        if (dbSysUser==null){
            throw new UsernameNotFoundException("username: " + username + " could not be found");
        }

//        Boolean canLogin = baseService.findWrongTryNumOfLogin(dbSysUser.getId());
//        if (!canLogin){
//            baseService.updateUserAccountStatus(dbSysUser.getId());
//            throw new UsernameNotFoundException("username: " + username + " try login too much ");
//        }
//        //根据部门id查找当前用户所具有的权限，具体权限对应非前端用的菜单的url，需要将个人的角色的，部门的，所有人的三者合到一块
//        Integer deptId = dbSysUser.getDeptId();
//        //查找部门部分
//        List<Integer> deptAuth=new ArrayList<>();
//        if (deptId != null){
//            deptAuth = dbSysDepartmentRepository.findAllAuthoritiesByDepartmentId(deptId);
//        }
//        //查找角色部分和公开的部分
//         List<Integer> priAndPubAuth =dbSysMenuRepository.findAllAuthoritiesByUserId(dbSysUser.getId());
        //根据用户角色来获取权限信息
        List<DbSysMenu> authInteger= dbSysMenuRepository.findAllAuthoritiesByUserIdThroughRole(dbSysUser.getId());
        List<DbSysMenu> dbSysMenus = dbSysMenuRepository.findAllPublicAuthorities();
        //菜单中存在重复的url,故需要查询出来相同的条目，添加到当前用户的权限中
        authInteger.addAll(dbSysMenus);
//        List<Integer> authoritiesNum=new ArrayList<>();
//        authoritiesNum.addAll(deptAuth);
//        authoritiesNum.addAll(priAndPubAuth);
        //创建一个封装所有的权限的集合
        authorities = new LinkedList<GrantedAuthority>();
//        for (Integer integer : authoritiesNum) {
//        for (Integer integer : authInteger) {
        for (DbSysMenu integer : authInteger) {
            authorities.add( new SimpleGrantedAuthority(URL_ROLE_PREFIX+integer.getId()));
        }

        user=new User(dbSysUser.getLoginName(),dbSysUser.getPassword(),authorities);

        CustomUser customUser = new CustomUser(user, dbSysUser,null);

        return customUser;
	}
}
