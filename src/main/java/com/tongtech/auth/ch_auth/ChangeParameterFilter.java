package com.tongtech.auth.ch_auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtech.auth.utils.MD5;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangeParameterFilter implements Filter {
	static private int		BUFF_SIZE			= 8192;
	static private int		READ_NUM_MAX		= 2;
	static private String	PARAMETER_USERNAME	= "username";
	static private String	PARAMETER_PASSWORD	= "password";

	private ObjectMapper mapper = new ObjectMapper();
	private RequestMatcher requiresAuthenticationRequestMatcher;

	public ChangeParameterFilter(String loginPage) {
		this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(loginPage, "POST");
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request  = (HttpServletRequest)  req;
		HttpServletResponse response = (HttpServletResponse) res;
		BufferedReader		reader	 = null;
		char[] 				cbuf	 = new char[BUFF_SIZE];
		StringBuffer 		sb   	 = new StringBuffer();
		int 				read_len = 0;
		int 				read_num = 0;
		UsernamePassword	usernamePassword = null;
		if(!requiresAuthenticationRequestMatcher.matches(request) ||
			request.getParameter(PARAMETER_USERNAME) != null      ||
			request.getContentType() == null                      ||
			request.getContentType().indexOf(MediaType.APPLICATION_JSON_VALUE) == -1
	) {
			chain.doFilter(request, response);
			return;
		}

		//-----------------------------------------------------------
		reader = request.getReader();
		while ((read_len = reader.read(cbuf, 0, BUFF_SIZE)) != -1) {
			sb.append(cbuf, 0, read_len);
			if(++read_num > READ_NUM_MAX)
				throw new ServletException("json body too long");
		}
		
		usernamePassword = mapper.readValue(sb.toString(), UsernamePassword.class);
//		usernamePassword=new UsernamePassword();
//		usernamePassword.setPassword(request.getParameter(PARAMETER_PASSWORD));
//		usernamePassword.setUsername(request.getParameter(PARAMETER_USERNAME));
		ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper (request);
		requestWrapper.addParameter(PARAMETER_USERNAME, usernamePassword.getUsername());
		String encode = MD5.encode(usernamePassword.getPassword());
		requestWrapper.addParameter(PARAMETER_PASSWORD, encode);
        chain.doFilter(requestWrapper, response);
	}
	
	public static class ParameterRequestWrapper extends HttpServletRequestWrapper {
		private Map<String, String[]> parameter;
		
		public ParameterRequestWrapper(HttpServletRequest request) {
			super(request);
			parameter = new HashMap<String, String[]>();  
		}
		
		public void addParameter (String name , Object value) {
			if(value == null)
				return;
            if(value instanceof String[]) {
            	parameter.put(name, (String [])value);
            }else if(value instanceof String) {
            	parameter.put(name , new String[] {(String)value});  
            }else {  
            	parameter.put(name , new String[] {String.valueOf(value)});  
            }
		}
		
	    @Override
	    public String getParameter(String name) {
	    	String[] value = parameter.get(name);
	    	if(value == null)
	    		return super.getParameter(name);
	    	else if (value.length == 0)
	    		return null;
	    	else
	    		return value[0];
	    }
	}
}
