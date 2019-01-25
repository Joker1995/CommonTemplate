package com.tisson.demo.common.base.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tisson.demo.common.base.IRedisService;
import com.tisson.demo.common.base.JsonSerializer;
import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.base.cahce.RedisCache;
import com.tisson.demo.common.base.cahce.RedisCallBack;
import com.tisson.demo.common.util.JWTUtil;
import com.tisson.demo.dao.sys.SysUsersMapper;
import com.tisson.demo.entity.sys.SysUsers;

/**
 * @Title: JWTFilter.java
 * @Package com.tisson.demo.auth
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年11月8日
 * @version V1.0
 */
@Component("jwtFilter")
public class JWTFilter extends BasicHttpAuthenticationFilter {
	/**
	 * 距离失效前5分钟重发签名
	 */
	private static final int tokenRefreshInterval = 300;
//	private static final int tokenRefreshInterval = 30;
	private final static Logger LOGGER = LoggerFactory.getLogger(JWTFilter.class);
	@Autowired
	private SysUsersMapper sysUsersMapper;
//	@Autowired
//	private IRedisService<Boolean> userName2TokenService;
	@Autowired
	private RedisCache cache;

	/**
	 * 判断用户是否想要登入。 检测header里面是否包含Authorization字段即可
	 */
	@Override
	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
		HttpServletRequest req = (HttpServletRequest) request;
		String authorization = req.getHeader("Authorization");
		return authorization != null;
	}

	/**
	 * @Title: executeLogin @Description: TODO(这里用一句话描述这个方法的作用) @return 返回类型 @throws
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String authorization = httpServletRequest.getHeader("Authorization");
		JWTToken token = new JWTToken(authorization);
		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
		Subject subject = getSubject(request, response);
		subject.login(token);
		// 如果没有抛出异常则代表登入成功，返回true
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		Object principal = subject.getPrincipal();
		String newToken = null;
		if (principal instanceof SysUsers) {
			SysUsers loginUser = (SysUsers) principal;
			String userName = JWTUtil.getUserName(authorization);
			if (!JWTUtil.verify(authorization, userName, loginUser.getPassword())) {
				throw new AuthenticationException("token expired");
			}
			String key = "ssoToken:" + userName;
			// 判断是否踢出
			Type type=new TypeToken<Map<String,String>>(){}.getType();
			Map<String,String> resultMap = cache.getHash("ssoToken:"+userName, authorization, type, new RedisCallBack<Map<String,String>>() {
				@Override
				public <T> T callbackForSingleObject(byte[] data, Type type) {
					return JsonSerializer.deserialize(data, type);
				}
				@Override
				public <T> List<T> callbackForList(byte[] data, Type type) {
					return null;
				}
			});
			if(resultMap!=null) {
	        	String kickOutResult=resultMap.get("kickOut");
	    	    if(kickOutResult!=null && kickOutResult.toLowerCase().equals("true")) {
	    	    	throw new AuthenticationException("token was kicked out");
	    	    }
	        }
			
			Integer maxOnlineCount = loginUser.getMaxOnlineCount();
			// hash结构expire会使全部hashkey都执行
			List<String> hashKeyList=new ArrayList<String>(cache.getAllHashKey(key));
	        Integer cacheMaxCount=hashKeyList.size();
	        Date now = new Date();
	        for(String hashKey:hashKeyList) {
	        	Date expireTime = JWTUtil.getExpiresAt(hashKey);
	        	if(expireTime.getTime()<=now.getTime()) {
	        		cache.delHash(key, hashKey);
	        		cacheMaxCount--;
	        	}
	        }
	        if (maxOnlineCount > 1 && cacheMaxCount >= maxOnlineCount 
	        		&& !cache.hashKeyExists(key, authorization)) {
				throw new AuthenticationException(userName + " has too many token online");
			}
			boolean shouldRefresh = shouldTokenRefresh(JWTUtil.getIssuedAt(authorization));
			if (shouldRefresh) {
				SysUsers sysUsers = sysUsersMapper.loadByName(userName);
				newToken = JWTUtil.sign(userName, sysUsers.getPassword());
				if (!cache.hashKeyExists("ssoToken:" + userName, newToken)) {
					Map<String,String> dataMap=new HashMap<String,String>();
					dataMap.put("kickOut", "false");
					cache.setHash("ssoToken:" + userName, newToken,dataMap);
					cache.expire("ssoToken:" + userName, 
							Double.valueOf(JWTUtil.EXPIRE_TIME / 1000 + Math.random() * 30).intValue());
				}
				cache.delHash("ssoToken:" + userName,authorization);
			}
		}
		if (!StringUtils.isEmpty(newToken)) {
			httpResponse.setHeader("Authorization", newToken);
			httpResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
		}
		return true;
	}

	/**
	 * 这里我们详细说明下为什么最终返回的都是true，即允许访问 例如我们提供一个地址 GET /article 登入用户和游客看到的内容是不同的
	 * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西 所以我们在这里返回true，Controller中可以通过
	 * subject.isAuthenticated() 来判断用户是否登入
	 * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
	 * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (isLoginAttempt(request, response)) {
			try {
				executeLogin(request, response);
			} catch (Exception e) {
				LOGGER.error("ERROR:", e);
				response401(request, response, e.getMessage());
			}
		}
		return true;
	}

	/**
	 * 对跨域提供支持
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers",
				httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(HttpStatus.OK.value());
			return false;
		}
		return super.preHandle(request, response);
	}

	/**
	 * 将非法请求跳转到 /401
	 */
	private void response401(ServletRequest req, ServletResponse resp, String msg) {
		HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
		httpServletResponse.setStatus(401);
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json; charset=utf-8");
		try (PrintWriter out = httpServletResponse.getWriter()) {
			String data = new Gson().toJson(new ResponseBean<String>(401, msg, null));
			out.append(data);
		} catch (IOException e) {
			LOGGER.error("ERROR response401:", e);
		}
	}

	protected boolean shouldTokenRefresh(Date issueDate) {
		LocalDateTime issueTime = LocalDateTime.ofInstant(issueDate.toInstant(), ZoneId.systemDefault());
		return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
	}
}
