package com.tisson.demo.common.expt;

import org.apache.shiro.authc.AuthenticationException;

/**
* @Title: SessionOnlineLimitException.java  
* @Package com.tisson.demo.common.expt  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2019年2月20日  
* @version V1.0  
*/
public class SessionOnlineLimitException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public SessionOnlineLimitException(String msg) {
        super(msg);
    }

    public SessionOnlineLimitException() {
        super();
    }
}
