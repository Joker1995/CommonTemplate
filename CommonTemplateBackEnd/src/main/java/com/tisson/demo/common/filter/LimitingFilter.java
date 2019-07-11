package com.tisson.demo.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.base.ResultCode;
import com.tisson.demo.common.cahce.RedisCache;

@Component
public class LimitingFilter implements Filter {
	private final static Logger LOGGER = LoggerFactory.getLogger(LimitingFilter.class);
	@Autowired
	private RedisCache cache;

	private final String limitKey = "rateLimit:";
	
	private final Integer rateLimit = 10;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("limitingFilter startUp");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String clientIP = req.getHeader("x-forwarded-for");
		if (clientIP == null || clientIP.length() == 0 || "unknown".equalsIgnoreCase(clientIP) || "null".equalsIgnoreCase(clientIP)) {
			clientIP = req.getHeader("Proxy-Client-IP");
		}
		if (clientIP == null || clientIP.length() == 0 || "unknown".equalsIgnoreCase(clientIP) || "null".equalsIgnoreCase(clientIP)) {
			clientIP = req.getHeader("WL-Proxy-Client-IP");
		}
		if (clientIP == null || clientIP.length() == 0 || "unknown".equalsIgnoreCase(clientIP) || "null".equalsIgnoreCase(clientIP)) {
			clientIP = req.getRemoteAddr();
		}
		LOGGER.info("limiting clientIP:[{}]",clientIP);
		if (!StringUtils.isEmpty(clientIP)) {
			if(cache.exists(limitKey+clientIP)) {
				Integer limitVal = Integer.valueOf(cache.get(limitKey+clientIP));
				if(limitVal>=rateLimit) {
					LOGGER.info("limiting clientIP:[{}],rateValue[{}] over rateLimt",clientIP,limitVal);
					try (PrintWriter out = resp.getWriter()) {
						String data = new Gson().toJson(
								new ResponseBean<String>(ResultCode.RATE_LIMIT_ERROR.getCode(), ResultCode.RATE_LIMIT_ERROR.getDesc(), null));
						out.append(data);
						out.flush();
					} catch (IOException e) {
						LOGGER.error("ERROR IN WRITE RESPONSE:", e);
					}
					return;
				}else {
					cache.incr(limitKey+clientIP);
				}
			}else {
				cache.put(limitKey+clientIP, 0,1,TimeUnit.SECONDS);
			}
		}else {
			try (PrintWriter out = resp.getWriter()) {
				String data = new Gson().toJson(
						new ResponseBean<String>(ResultCode.RATE_LIMIT_ERROR.getCode(), ResultCode.RATE_LIMIT_ERROR.getDesc(), null));
				out.append(data);
				out.flush();
			} catch (IOException e) {
				LOGGER.error("ERROR IN WRITE RESPONSE:", e);
			}
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		LOGGER.info("limitingFilter destroy");
	}

}
