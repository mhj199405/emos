package com.tongtech.auth.ch_auth;


import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import lombok.Data;
import org.springframework.security.core.userdetails.User;

@Data
public class CustomUser extends org.springframework.security.core.userdetails.User {
	private static final long serialVersionUID = 1L;

	private DbSysUser voUserMenu;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public DbSysUser getVoUserMenu() {
		return voUserMenu;
	}

	public void setVoUserMenu(DbSysUser voUserMenu) {
		this.voUserMenu = voUserMenu;
	}

	public DbSysMenu getDbSysMenu() {
		return dbSysMenu;
	}

	public void setDbSysMenu(DbSysMenu dbSysMenu) {
		this.dbSysMenu = dbSysMenu;
	}

	private DbSysMenu dbSysMenu
			;
	
	public CustomUser(User voUserMenu,DbSysUser dbSysUser,DbSysMenu dbSysMenu)  {
		super(voUserMenu.getUsername(),
			  voUserMenu.getPassword(),
			  voUserMenu.getAuthorities());
		
		this.voUserMenu = dbSysUser;
		this.dbSysMenu=dbSysMenu;
	}


	
//	public static BaseUser getSessionBaseUser() {
//		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		if(obj == null || !(obj instanceof CustomUser)) {
//			return null;
//		}
//		CustomUser customUser = (CustomUser) obj;
//
//		return customUser.getVoUserMenu().getBaseUser();
//	}
}
