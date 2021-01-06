package com.tongtech.auth.vo;

import lombok.Data;
import org.springframework.security.web.csrf.CsrfToken;

@Data
public class LoginResult<T> {
    private int			status;
    private String		message;
    private CsrfToken token;
    private	T   		data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CsrfToken getToken() {
        return token;
    }

    public void setToken(CsrfToken token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
