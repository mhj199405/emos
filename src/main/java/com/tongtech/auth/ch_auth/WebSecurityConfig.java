package com.tongtech.auth.ch_auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.vo.LoginResult;
import com.tongtech.common.vo.RestResult;
import com.tongtech.common.vo.RestResultFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final static Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);	
	
	private static final int	LOGIN_SUCCESS_CODE    = 0;
	private static final String LOGIN_SUCCESS_MESSAGE = "success";
	private static final int	LOGIN_FAIL_CODE    	  = -1;
	private static final String LOGIN_FAIL_MESSAGE    = "fail";
	private static final int	LOGOUT_CODE			  = 0;
	private static final String LOGOUT_FORMAT         = "%s logout";
	private static final String REQUEST_ATTRIBUTE_CSRF= "_csrf";

	@Autowired
	private UserDetailServiceImpl userDetailsService;
	@Autowired
	private AllowOriginProperties	allowOrigin;
	@Autowired
	private PermitAllUrlProperties	permitAllUrl;
	@Autowired
    private  BaseService baseService;


	
	@Override
    protected void configure(HttpSecurity http) throws Exception {

		ObjectMapper mapper = (ObjectMapper) getApplicationContext().getBean(ObjectMapper.class);
    	ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry;
    	registry = http.authorizeRequests();
    	registry.antMatchers("/sys/csrf").permitAll();
//    	registry.antMatchers("/api/daiyun/queryRoleAndUser").permitAll();
//    	registry.antMatchers("/api/template/**").permitAll();
//    	registry.antMatchers("/currentUser").permitAll();
//    	registry.antMatchers("/error").permitAll();
    	permitAllUrl.getUrl().forEach(
    		url->{
    			registry.antMatchers(url).permitAll();
    			log.info(String.format("-->url:%s, permitAll", url));
    		}
    	);
//        //from base_url_t
    	for(DbSysMenu baseUrl : baseService.findDbSysOperationAll()) {
//            System.out.println("项目启动加载的权限："+baseUrl.getOperId()+"---"+baseUrl.getApiUrl());
    		registry
//				.antMatchers(HttpMethod.resolve(baseUrl.getOperType()), baseUrl.getApiUrl())
				.antMatchers(HttpMethod.resolve("".equals(baseUrl.getMethodType()) ? "GET" : baseUrl.getMethodType()),baseUrl.getMenuUrl())
				.hasRole(baseUrl.getId().toString());
//    		log.info(String.format("-->url:%s, method:%s, role:ROLE_%d#", baseUrl.getApiUrl(), baseUrl.getOperType(), baseUrl.getOperId()));
    		log.info(String.format("-->url:%s, role:ROLE_%d#", baseUrl.getMenuUrl(), baseUrl.getId()));
        }
//		  registry.antMatchers(HttpMethod.GET,"/test/test1").hasRole("1");
//        registry.antMatchers(HttpMethod.DELETE,"/api/**").permitAll();
//        registry.antMatchers(HttpMethod.PUT,"/api/**").permitAll();
//        registry.antMatchers(HttpMethod.POST,"/api/**").permitAll();
//        registry.antMatchers(HttpMethod.GET,"/api/**").permitAll();
//        registry.antMatchers(HttpMethod.GET,"/static/**").permitAll();
        registry.antMatchers(HttpMethod.GET,"/images/**").permitAll();
//        registry.antMatchers(HttpMethod.GET,"/sys/**").permitAll();
//        registry.antMatchers(HttpMethod.GET,"/download/**").permitAll();
//        registry.antMatchers(HttpMethod.POST,"/sys/**").permitAll();
//        registry.antMatchers(HttpMethod.DELETE,"/sys/**").permitAll();
//        registry.antMatchers(HttpMethod.PUT,"/sys/**").permitAll();
//        registry.antMatchers(HttpMethod.GET,"/sys/csrf").permitAll();
//		registry.antMatchers(HttpMethod.GET,"/api/**").permitAll();
//		registry.antMatchers(HttpMethod.POST,"/api/**").permitAll();
//		registry.antMatchers(HttpMethod.DELETE,"/api/**").permitAll();
//		registry.antMatchers(HttpMethod.PUT,"/api/**").permitAll();
//		registry.anyRequest().authenticated();
//		registry.antMatchers(HttpMethod.POST,"/api/template/getDictData").permitAll();
		registry.antMatchers(HttpMethod.POST,"/access/upload").permitAll();
//		registry.antMatchers(HttpMethod.GET,"/access/download").permitAll();
		registry.anyRequest().denyAll();
//        registry.anyRequest().permitAll();

        http
	    	.headers()
				.addHeaderWriter(new MyHeaderWriter())
				.and()
        	.cors()
        		.and()
        	.csrf()
        		.requireCsrfProtectionMatcher(new MyRequiresCsrfMatcher())
        		.ignoringAntMatchers("/sys/csrf","/images/**","/access/upload")
//        		.ignoringAntMatchers("/sys/csrf","/images/**","/access/upload","/api/template/**")
//        		.ignoringAntMatchers("/sys/**")
//				.ignoringAntMatchers("/download/**")
//				.ignoringAntMatchers("/api/**")
//				.ignoringAntMatchers("/**/**")
//        		.ignoringAntMatchers(permitAllUrl.getUrl().toArray(new String[0]))
        		.and()
        	.logout()
            	.logoutUrl("/api/logout")
            	.logoutSuccessHandler(new MyLogoutSuccessHandler(mapper))
                .permitAll()
                .and()
        	.addFilterBefore(new ChangeParameterFilter("/api/login"), UsernamePasswordAuthenticationFilter.class)
//			.addFilterAfter(dynamicallyUrlInterceptor(), FilterSecurityInterceptor.class)
        	.formLogin()
                .loginPage("/api/login")
                .loginProcessingUrl("/api/login")
        		.successHandler(new MyAuthenticationSuccessHandler(mapper))
        		.failureHandler(new MyAuthenticationFailureHandler(mapper))
        		.and()
        	.exceptionHandling()
        		.authenticationEntryPoint(new Http401EntryPoint(mapper))
				.accessDeniedHandler(new SimpleAccessDeniedHandler(mapper))
        		.and()
        	.sessionManagement()
        		.invalidSessionStrategy(new MyInvalidSessionStrategy(mapper));
    }
	
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//    	permitAllUrl.getUrl().forEach(
//        		url->{
//        			web.ignoring().antMatchers(url);
//            		log.info(String.format("-->url:%s, web ignoring", url));
//        		}
//        	);
//	}
	
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
        	.userDetailsService(userDetailsService);
//        	.inMemoryAuthentication()
//        		.withUser("aaa").password("aaa").roles("USER", "ACTUATOR").and()
//        		.withUser("mmm").password("mmm").roles("USER", "100");
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.HEAD);
        config.addAllowedMethod(HttpMethod.OPTIONS);
		config.addAllowedMethod(HttpMethod.POST);
		config.addAllowedHeader(HttpHeaders.CONTENT_TYPE);
		config.addAllowedMethod(HttpMethod.DELETE);
		config.addAllowedMethod(HttpMethod.PUT);
		config.addAllowedHeader("X-Csrf-Token");
		config.addAllowedHeader("X-Requested-With");
		config.addAllowedHeader(HttpHeaders.REFERER);
		//Allow All Header
		//config.addAllowedHeader("*");
        config.setAllowedOrigins(allowOrigin.getOrigin());
    	source.registerCorsConfiguration("/**", config);
    	
    	log.info(String.format("-->AllowedHeaders:%s$", config.getAllowedHeaders().toString()));
    	
    	return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
    	return NoOpPasswordEncoder.getInstance();
    }

    private static final class MyRequiresCsrfMatcher implements RequestMatcher {
		private final HashSet<String> allowedMethods = new HashSet<String>(
				Arrays.asList("HEAD", "TRACE", "OPTIONS"));

		@Override
		public boolean matches(HttpServletRequest request) {
			return !this.allowedMethods.contains(request.getMethod());
		}
	}
    
    private static class MyHeaderWriter implements HeaderWriter {
		@Override
		public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
			//###ABC
//			response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		}
    }

    private  class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
        private ObjectMapper mapper;

        public MyAuthenticationSuccessHandler(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {
            //###ABC
            response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

            CsrfToken token = (CsrfToken) request.getAttribute(REQUEST_ATTRIBUTE_CSRF);

            LoginResult loginResult  = new LoginResult();
            loginResult.setStatus(LOGIN_SUCCESS_CODE);
            loginResult.setMessage(LOGIN_SUCCESS_MESSAGE);
            loginResult.setToken(token);
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUser){
                CustomUser customUser= (CustomUser) principal;
                //将默认的菜单对象返回去
				DbSysUser voUserMenu = customUser.getVoUserMenu();
				if (voUserMenu==null){
                    loginResult.setData("");
                }else {
                    loginResult.setData(voUserMenu);
                }
            }else {
                loginResult.setData("");
            }
//            loginResult.setDetail(authentication.getPrincipal());
			//保存到数据库当中

			baseService.saveLogEntity(request.getParameter("username"),true);
			response.getOutputStream().write(mapper.writeValueAsBytes(loginResult));
        }
    }

    private  class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    	private ObjectMapper mapper;
    	
    	public MyAuthenticationFailureHandler(ObjectMapper mapper) {
    		this.mapper = mapper;
    	}

    	@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException exception) throws IOException, ServletException {

    		CsrfToken token = (CsrfToken) request.getAttribute(REQUEST_ATTRIBUTE_CSRF);
    		LoginResult loginResult = new LoginResult();
			loginResult.setToken(token);
//			loginResult.setResultCode(LOGIN_FAIL_CODE);
			loginResult.setStatus(1);
			if (exception.getMessage().equals("Bad credentials")){
				loginResult.setMessage("用户名或者密码错误");
			}else {
				loginResult.setMessage(LOGIN_FAIL_MESSAGE);
			}
//			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setStatus(HttpStatus.OK.value());
			//保存日志到数据库
			baseService.saveLogEntity(request.getParameter("username"),false);
			System.out.println(request.getParameter("username"));
			response.getOutputStream().write(mapper.writeValueAsBytes(loginResult));
		}
    }
    
    private static class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    	private ObjectMapper mapper;
    	
    	public MyLogoutSuccessHandler(ObjectMapper mapper) {
    		this.mapper = mapper;
    	}

		@Override
		public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
    		CsrfToken token = (CsrfToken) request.getAttribute(REQUEST_ATTRIBUTE_CSRF);
			LoginResult loginResult = new LoginResult();
			loginResult.setToken(token);
			loginResult.setStatus(LOGOUT_CODE);
			loginResult.setMessage(String.format(LOGOUT_FORMAT,
					(authentication == null) ? "" : authentication.getName() ));
			response.getOutputStream().write(mapper.writeValueAsBytes(loginResult));
		}
    }
    
    private static class MyInvalidSessionStrategy implements InvalidSessionStrategy {
		private ObjectMapper mapper;

		public MyInvalidSessionStrategy(ObjectMapper mapper){
			this.mapper=mapper;
		}
		@Override
		public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			RestResult<Object> noLoginResult = RestResultFactory.createNoLoginResult("请重新登录，会话已过期");
			HttpSession session = request.getSession();
			response.getOutputStream().write(mapper.writeValueAsBytes(noLoginResult));
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}
    }

	/**
	 * 用于支持动态权限加载
	 * @return
	 */
//	@Bean
//	public DynamicallyUrlInterceptor dynamicallyUrlInterceptor(){
//		List<DbSysMenu> dbSysOperationAll = baseService.findDbSysOperationAll();
//		DynamicallyUrlInterceptor interceptor = new DynamicallyUrlInterceptor();
//		interceptor.setSecurityMetadataSource(new MyFilterSecurityMetadataSource(baseService));
//
//		//配置RoleVoter决策
//		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
//		decisionVoters.add(new RoleVoter());
//		//设置认证决策管理器
//		interceptor.setAccessDecisionManager(new DynamicallyUrlAccessDecisionManager(decisionVoters));
//		return interceptor;
//	}

}
