package com.tongtech.auth.ch_auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtech.common.vo.RestResult;
import com.tongtech.common.vo.RestResultFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleAccessDeniedHandler  implements AccessDeniedHandler {
    private ObjectMapper mapper;

    public SimpleAccessDeniedHandler(ObjectMapper mapper){
        this.mapper=mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        RestResult<Object> authorityResult = RestResultFactory.createNoAuthorityResult("没有权限");
        response.getOutputStream().write(mapper.writeValueAsBytes(authorityResult));
    }
}
