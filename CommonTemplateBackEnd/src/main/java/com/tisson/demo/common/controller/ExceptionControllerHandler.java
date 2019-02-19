package com.tisson.demo.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.expt.UnauthorizedException;

import cn.hutool.json.JSONUtil;

/**  
* @Title: ExceptionController.java  
* @Package com.tisson.demo.common.controller  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月8日  
* @version V1.0  
*/
@RestControllerAdvice
public class ExceptionControllerHandler {
	private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerHandler.class);

	/**
	 * 捕捉UnauthorizedException
	 * 
	 * @return
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseBean<String> handleUnauthorizedException() {
		return new ResponseBean<String>(10002, "userName or passsword error or token expired", null);
	}
	
	// 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResponseBean<String> handle401(ShiroException e) {
        return new ResponseBean<String>(401, e.getMessage(), null);
    }
	/**
	 * 捕捉所有其他异常
	 * 
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseBean<String> globalException(HttpServletRequest request, Throwable ex) {
		LOGGER.error("URL:{}########PARAMS:{}",
				request.getRequestURI(),JSONUtil.toJsonStr(request.getParameterMap()));
		LOGGER.error("ERROR MEG:{}",ex.getMessage());
		return new ResponseBean<String>(500,"INTERNAL_SERVER_ERROR",null);
	}

	@SuppressWarnings("unused")
	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

}

