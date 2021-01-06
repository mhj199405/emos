package com.tongtech.auth.ch_auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtech.common.vo.RestResult;
import com.tongtech.common.vo.RestResultFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Http401EntryPoint implements AuthenticationEntryPoint {

	private ObjectMapper mapper;

	public Http401EntryPoint(ObjectMapper mapper){
		this.mapper=mapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
		RestResult<Object> errorResult = RestResultFactory.createErrorResult("内部错误");
		response.getOutputStream().write(mapper.writeValueAsBytes(errorResult));
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
