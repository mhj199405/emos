package com.tongtech.auth.ch_auth;

import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class MyFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    private BaseService baseService;
    /*
     * 这个例子放在构造方法里初始化url权限数据，我们只要保证在 getAttributes()之前初始好数据就可以了
     */
    public MyFilterSecurityMetadataSource(BaseService baseService) {
        this.baseService=baseService;
        Map<RequestMatcher, Collection<ConfigAttribute>> map = new HashMap<>();

        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/test/test1");
        SecurityConfig config = new SecurityConfig("ROLE_34");
        ArrayList<ConfigAttribute> configs = new ArrayList<>();
        configs.add(config);
        map.put(matcher,configs);

        AntPathRequestMatcher matcher2 = new AntPathRequestMatcher("/sys/getcurrentuser");
        SecurityConfig config2 = new SecurityConfig("ROLE_ADMIN");
        ArrayList<ConfigAttribute> configs2 = new ArrayList<>();
        configs2.add(config2);
        map.put(matcher2,configs2);

        this.requestMap = map;
    }

    /**
     * 在我们初始化的权限数据中找到对应当前url的权限数据
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        HttpServletRequest request = fi.getRequest();
        String url = fi.getRequestUrl();
        String httpMethod = fi.getRequest().getMethod();

        // Lookup your database (or other source) using this information and populate the
        // list of attributes (这里初始话你的权限数据)
        //List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
        //先查找出来所有的，测试一下功能
        List<DbSysMenu> dbSysOperationAll = baseService.findDbSysOperationAll();
        //然后遍历相应的条目，构建授权项进行返回
        requestMap.clear();
        for (DbSysMenu dbSysMenu : dbSysOperationAll) {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(dbSysMenu.getMenuUrl());
            SecurityConfig config = new SecurityConfig("ROLE_"+dbSysMenu.getId());
            ArrayList<ConfigAttribute> configs = new ArrayList<>();
            configs.add(config);
            requestMap.put(matcher,configs);
        }
        //遍历我们初始化的权限数据，找到对应的url对应的权限
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        ArrayList<ConfigAttribute> configs = new ArrayList<>();
        configs.add(new SecurityConfig("ROLE_ADMIN"));
        return configs;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
