package com.tisson.demo.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.base.ResultCode;
import com.tisson.demo.common.expt.CaptchaValidateException;
import com.tisson.demo.common.expt.RegisterUserException;
import com.tisson.demo.common.expt.SessionKickoutException;
import com.tisson.demo.common.expt.TokenInvalidateException;
import com.tisson.demo.common.expt.UnauthorizedException;
import com.tisson.demo.common.expt.UserNameOrPwdException;

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
	 * 捕捉未授权异常
	 * 
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseBean<String> handleUnauthorized() {
		return new ResponseBean<String>(ResultCode.UNAUTHORIZED_ERROR.getCode(), 
				ResultCode.UNAUTHORIZED_ERROR.getDesc(), null);
	}

	// 捕捉shiro的异常
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(ShiroException.class)
	public ResponseBean handle401(ShiroException e) {
		return new ResponseBean(ResultCode.UNAUTHORIZED_ERROR.getCode(),
				ResultCode.UNAUTHORIZED_ERROR.getDesc(), null);
	}

    /**
     * 捕捉token失效异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenInvalidateException.class)
    public ResponseBean<String> handleTokenInvalidate() {
        return new ResponseBean<String>(ResultCode.TOKEN_INVALIDATE_ERROR.getCode(),
        		ResultCode.TOKEN_INVALIDATE_ERROR.getDesc(), null);
    }
    
    /**
     * 捕捉用户名或密码异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNameOrPwdException.class)
    public ResponseBean<String> handleUserNameOrPwd() {
        return new ResponseBean<String>(ResultCode.USERNAME_OR_PWD_ERROR.getCode(),
        		ResultCode.USERNAME_OR_PWD_ERROR.getDesc(), null);
    }
    
    /**
     * 捕捉token失效异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SessionKickoutException.class)
    public ResponseBean<String> handleSessionKickout() {
        return new ResponseBean<String>(ResultCode.SESSION_KICKOUT_ERROR.getCode(),
        		ResultCode.SESSION_KICKOUT_ERROR.getDesc(), null);
    }
    
    /**
     * 捕捉参数异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseBean<String> handleParams() {
        return new ResponseBean<String>(ResultCode.PARAMS_ERROR.getCode(),
        		ResultCode.PARAMS_ERROR.getDesc(), null);
    }
    
    /**
     * 捕捉方法请求方式异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseBean<String> handleMethodNotSupported() {
        return new ResponseBean<String>(ResultCode.REQUEST_METHOD_UNSUPPORTED_ERROR.getCode(),
        		ResultCode.REQUEST_METHOD_UNSUPPORTED_ERROR.getDesc(), null);
    }
    /**
     * 捕捉方法请求参数缺失异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseBean<String>  handlerParamsMissValidate(){
    	return new ResponseBean<String>(ResultCode.PARAMS_VALIDATE_FAILURE.getCode(),
        		ResultCode.PARAMS_VALIDATE_FAILURE.getDesc(), null);
    }
    
    
    /**
     * 捕捉方法请求参数检验错误异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseBean<String>  handlerMethodArgumentNotValid(){
    	return new ResponseBean<String>(ResultCode.PARAMS_VALIDATE_FAILURE.getCode(),
        		ResultCode.PARAMS_VALIDATE_FAILURE.getDesc(), null);
    }
    
    /**
     * 捕捉shiro校验异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnauthenticatedException.class)
	public ResponseBean<String>  handlerUnauthenticated(){
    	return new ResponseBean<String>(ResultCode.UNAUTHORIZED_ERROR.getCode(),
        		ResultCode.UNAUTHORIZED_ERROR.getDesc(), null);
    }
    
    /**
      * 捕捉验证码校验异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CaptchaValidateException.class)
	public ResponseBean<String>  handlerCaptchaInvalidate(){
    	return new ResponseBean<String>(ResultCode.CAPTCHA_INVALIDATE_ERROR.getCode(),
        		ResultCode.CAPTCHA_INVALIDATE_ERROR.getDesc(), null);
    }
    
    
    /**
       * 捕捉用户注册异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegisterUserException.class)
	public ResponseBean<String>  handlerRegisteUser(){
    	return new ResponseBean<String>(ResultCode.REGISTER_USER_ERROR.getCode(),
        		ResultCode.REGISTER_USER_ERROR.getDesc(), null);
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
		if(ex instanceof ClientAbortException || ex instanceof IllegalStateException) {
			LOGGER.info("可能是response getOutputStream打开两次以上,忽略该错误");
			return null;
		}else {
			LOGGER.error("捕捉系统错误类型:[{}],请求URL:[{}],请求参数:[{}]",
					ex.getClass().getName(),request.getRequestURI(),
					JSONUtil.toJsonStr(request.getParameterMap()));
			LOGGER.error("捕捉系统错误栈信息:",ex);
			return new ResponseBean<String>(ResultCode.INTERNAL_SERVER_ERROR.getCode(),
					ResultCode.INTERNAL_SERVER_ERROR.getDesc(),null);
		}
		
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

