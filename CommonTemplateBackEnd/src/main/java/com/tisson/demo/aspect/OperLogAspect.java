package com.tisson.demo.aspect;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.util.JWTUtil;
import com.tisson.demo.entity.sys.SysLoggerOpration;
import com.tisson.demo.service.sys.SysLoggerOprationService;

import cn.hutool.json.JSONUtil;

/**  
* @Title: OperLogAspect.java  
* @Package com.tisson.demo.aspect  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月12日  
* @version V1.0  
*/
@Aspect
@Component
@SuppressWarnings("rawtypes")
public class OperLogAspect {
	private final static Logger LOGGER = LoggerFactory.getLogger(OperLogAspect.class);
	@Autowired
	private SysLoggerOprationService sysLoggerOprationService;
	@Resource(name = "defaultThreadPool")
	private ThreadPoolTaskExecutor executor;
	
	@Pointcut("execution(public * com.tisson.demo.controller..*.*(..)) && "
			+ " !execution(public * com.tisson.demo.controller..*.login*(..)) && "
			+ " @within(org.springframework.web.bind.annotation.RestController) ")
	public void operAspectPoint() {
	}
	
	@Around("operAspectPoint()")
	public Object process(ProceedingJoinPoint joinPoint) throws Throwable{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String token = request.getHeader("Authorization");
		SysLoggerOpration operLog=new SysLoggerOpration();
		//入参  value
		Object[] args = joinPoint.getArgs();
		//切面返回值
        Object returnValue = joinPoint.proceed(args);
        
		try {
			if(!StringUtils.isEmpty(token)) { //针对登录后的方法拦截
				String name=JWTUtil.getUserName(token);
				operLog.setUserName(name);
				//入参名称
			    String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
			    Map<String, Object> params = new HashMap<>();
			    //获取所有参数对象
			    for (int i = 0; i < args.length; i++) {
			        if (null != args[i]) {
			            if (args[i] instanceof BindingResult) {
			                params.put(paramNames[i], "bindingResult");
			            } else {
			                params.put(paramNames[i], args[i]);
			            }
			        } else {
			            params.put(paramNames[i], "无");
			        }
			    }
			    Signature sig = joinPoint.getSignature();
			    MethodSignature msig = null;
			    if (!(sig instanceof MethodSignature)) {
			        throw new IllegalArgumentException("该注解只能用于方法");
			    }
			    msig = (MethodSignature) sig;
			    Object target = joinPoint.getTarget();
			    Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
			    
			    String requestUrl=request.getRequestURI().length()>110?request.getRequestURI().substring(0, 110):
			    	request.getRequestURI();
			    String paramsStr=JSONUtil.toJsonStr(params);
			    paramsStr=paramsStr.length()>250?paramsStr.substring(0, 250):paramsStr;
			    ResponseBean result=(ResponseBean)returnValue;
			    if(result!=null) {
			    	operLog.setCode(StringUtils.isEmpty(result.getCode())?"ErrCode":String.valueOf(result.getCode()));
					operLog.setResult(result.getMsg());
				    operLog.setParams(paramsStr);
				    operLog.setMethod(target.getClass().getName()+"."+currentMethod.getName());
				    operLog.setUrl(requestUrl);
				    operLog.setTime(new Date());
				    executor.execute(()->{
				    	try {
							sysLoggerOprationService.save(operLog);
						} catch (Exception e) {
							LOGGER.error("ERROR IN process operAspectPoint:",e);
						}
				    });
			    }
			}else {//针对未登录的方法拦截
				operLog.setUserName("anno");
				//入参名称
			    String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
			    Map<String, Object> params = new HashMap<>();
			    //获取所有参数对象
			    for (int i = 0; i < args.length; i++) {
			        if (null != args[i]) {
			            if (args[i] instanceof BindingResult) {
			                params.put(paramNames[i], "bindingResult");
			            } else {
			                params.put(paramNames[i], args[i]);
			            }
			        } else {
			            params.put(paramNames[i], "无");
			        }
			    }
			    Signature sig = joinPoint.getSignature();
			    MethodSignature msig = null;
			    if (!(sig instanceof MethodSignature)) {
			        throw new IllegalArgumentException("该注解只能用于方法");
			    }
			    msig = (MethodSignature) sig;
			    Object target = joinPoint.getTarget();
			    Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
			    
			    String requestUrl=request.getRequestURI().length()>110?request.getRequestURI().substring(0, 110):
			    	request.getRequestURI();
			    String paramsStr=JSONUtil.toJsonStr(params);
			    paramsStr=paramsStr.length()>250?paramsStr.substring(0, 250):paramsStr;
			    ResponseBean result=(ResponseBean)returnValue;
			    if(result!=null) {
			    	operLog.setCode(StringUtils.isEmpty(result.getCode())?"ErrCode":String.valueOf(result.getCode()));
					operLog.setResult(result.getMsg());
				    operLog.setParams(paramsStr);
				    operLog.setMethod(target.getClass().getName()+"."+currentMethod.getName());
				    operLog.setUrl(requestUrl);
				    operLog.setTime(new Date());
				    executor.execute(()->{
				    	try {
							sysLoggerOprationService.save(operLog);
						} catch (Exception e) {
							LOGGER.error("ERROR IN process operAspectPoint:",e);
						}
				    });
			    }
			}
		} catch (Exception e) {
			LOGGER.error("ERROR MSG:{}",e);
		}
		return returnValue;
	}
}
