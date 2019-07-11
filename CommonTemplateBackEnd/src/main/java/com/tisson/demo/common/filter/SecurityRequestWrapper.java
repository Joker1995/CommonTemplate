package com.tisson.demo.common.filter;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;

public class SecurityRequestWrapper extends HttpServletRequestWrapper {
	private final static Logger LOGGER = LoggerFactory.getLogger(SecurityRequestWrapper.class);
	private SecurityConfig config;

	public SecurityRequestWrapper(HttpServletRequest request, SecurityConfig config) {
		super(request);
		this.config = config;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (config.isCheckHeader()) {
			if (!StringUtils.isEmpty(value)) {
				List<Pattern> patternList = config.getRegexPatternList();
				for (Pattern pattern : patternList) {
					Matcher matcher = pattern.matcher(value);
					while (matcher.find()) {
						String matcherContent = matcher.group();
						LOGGER.info("match sensitive content:[{}],pattern:[{}]", 
								matcherContent, pattern.pattern());
						if (config.isReplace()) {
							return value.replace(matcherContent, config.getReplaceWord());
						}
					}
				}
			}
		}
		return value;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (config.isCheckParams()) {
			if (!StringUtils.isEmpty(value)) {
				List<Pattern> patternList = config.getRegexPatternList();
				for (Pattern pattern : patternList) {
					Matcher matcher = pattern.matcher(value);
					while (matcher.find()) {
						String matcherContent = matcher.group();
						LOGGER.info("match sensitive content:[{}],pattern:[{}]", 
								matcherContent, pattern.pattern());
						if (config.isReplace()) {
							return value.replace(matcherContent, config.getReplaceWord());
						}
					}
				}
			}
		}
		return value;
	}

	private boolean checkHeader() {
		Enumeration<String> headerParams = this.getHeaderNames();
		while (headerParams.hasMoreElements()) {
			String headerName = headerParams.nextElement();
			String headerValue = this.getHeader(headerName);
			if (config.isCheckHeader()) {
				List<Pattern> patternList = config.getRegexPatternList();
				for (Pattern pattern : patternList) {
					Matcher matcher = pattern.matcher(headerValue);
					while (matcher.find()) {
						String matcherContent = matcher.group();
						LOGGER.info("match sensitive content:[{}],pattern:[{}]",
								matcherContent, pattern.pattern());
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean checkParameter() {
		// 对于form表单提交的数据
		Map<String,String[]> submitParams = this.getParameterMap();  
        Set<String> submitNames = submitParams.keySet(); 
        List<Pattern> patternList = config.getRegexPatternList();
        for(String submitName : submitNames){  
            Object submitValues = submitParams.get(submitName);  
            for(String submitValue : (String[])submitValues){  
        		for (Pattern pattern : patternList) {
        			Matcher matcher = pattern.matcher(submitValue);
					while (matcher.find()) {
						String matcherContent = matcher.group();
						LOGGER.info("match sensitive content:[{}],pattern:[{}]",
								matcherContent, pattern.pattern());
						return true;
					}
        		} 
            }
        } 
        // TODO 对于json格式的数据待处理
        return false;  
	}

	public boolean validateParameter() {
		if (config.isCheckHeader()) {
			if (checkHeader()) {
				return true;
			}
		}
		if (config.isCheckParams()) {
			if (checkParameter()) {
				return true;
			}
		}
		return false;
	}

	public String getRemoteAddr() {
		return ((HttpServletRequest) this.getRequest()).getRemoteAddr();
	}

	public String getRequestURI() {
		return ((HttpServletRequest) this.getRequest()).getRequestURI();
	}

	public String getMethod() {
		return ((HttpServletRequest) this.getRequest()).getMethod();
	}

	public HttpSession getSession() {
		return ((HttpServletRequest) this.getRequest()).getSession();
	}

	public Enumeration<String> getParameterNames() {
		return ((HttpServletRequest) this.getRequest()).getParameterNames();
	}

	public RequestDispatcher getRequestDispatcher(String arg) {
		return ((HttpServletRequest) this.getRequest()).getRequestDispatcher(arg);
	}
}
