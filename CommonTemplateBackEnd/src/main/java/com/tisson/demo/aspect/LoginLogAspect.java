package com.tisson.demo.aspect;

import java.util.Date;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.entity.sys.SysLoggerLogin;
import com.tisson.demo.service.sys.SysLoggerLoginService;

/**  
* @Title: LoginLogAspect.java  
* @Package com.tisson.demo.aspect  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月12日  
* @version V1.0  
*/
@Aspect
@Component
@SuppressWarnings("rawtypes")
public class LoginLogAspect {
	private final static Logger LOGGER = LoggerFactory.getLogger(LoginLogAspect.class);
	@Autowired
	private SysLoggerLoginService sysLoggerLoginService;
	@Resource(name = "defaultThreadPool")
	private ThreadPoolTaskExecutor executor;
	
	@Pointcut(" execution(public * com.tisson.demo.controller..*.login*(..)) && "
			+ " @annotation(org.springframework.web.bind.annotation.RestController) ")
	public void loginAspectPoint() {
	}
	
	@Around("loginAspectPoint()")
	public Object process(ProceedingJoinPoint joinPoint) throws Throwable{
		SysLoggerLogin loginLog=new SysLoggerLogin();
        Object[] args = joinPoint.getArgs();
		//切面返回值
        Object returnValue = joinPoint.proceed(args);
        try {
			if(!StringUtils.isEmpty(args[0])) {
				String name=(String)args[0];
				ResponseBean result=(ResponseBean)returnValue;
				
				loginLog.setUserName(name);
				loginLog.setTime(new Date());
				loginLog.setCode(StringUtils.isEmpty(result.getCode())?"ErrCode":String.valueOf(result.getCode()));
				loginLog.setResult(result.getMsg());
				
				executor.execute(()->{
					sysLoggerLoginService.save(loginLog);
				});
			}
		} catch (Exception e) {
			LOGGER.error("ERR MSG:{}",e);
		}
		return returnValue;
	}
}
