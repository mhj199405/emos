package com.tongtech.common;

/**
 * 业务通用异常
 * 
 * @author 1
 *
 */
public class BusinessException extends RuntimeException {
	public BusinessException() {
		super();
	}
	
	public BusinessException(String msg) {
		super(msg);
	}

}
