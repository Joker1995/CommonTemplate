package com.tisson.demo.common.filter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.base.ResultCode;
import com.tisson.demo.common.filter.SecurityConfig.SecurityConfigHolder;

@Component
public class SecurityFilter implements Filter{
	private final static Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

	private SecurityConfig securityConfig;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			String securityConfigFilePath = ResourceUtils.getFile("classpath:config/securityConfig.xml").getPath();
			LOGGER.info("securityConfigFilePath:[{}]",securityConfigFilePath);
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder =factory.newDocumentBuilder();
			File configFile = new File(securityConfigFilePath);
			Document document=documentBuilder.parse(configFile);
			Element root=document.getDocumentElement();
			securityConfig= SecurityConfigHolder.getInstance();
			NodeList nodeChilds=root.getChildNodes();
			for(int idx=0;idx<nodeChilds.getLength();idx++) {
				Node node = nodeChilds.item(idx);
				String name=node.getNodeName();
				if("isCheckHeader".equals(name)) {
					Boolean isCheckHeader = Boolean.valueOf(node.getTextContent());
					securityConfig.setCheckHeader(isCheckHeader);
				}else if("isCheckParameter".equals(name)) {
					Boolean isCheckParameter = Boolean.valueOf(node.getTextContent());
					securityConfig.setCheckParams(isCheckParameter);
				}else if("isChain".equals(name)) {
					Boolean isChain = Boolean.valueOf(node.getTextContent());
					securityConfig.setChain(isChain);
				}else if("isReplace".equals(name)) {
					Boolean replace = Boolean.valueOf(node.getTextContent());
					securityConfig.setReplace(replace);
				}else if("sensitiveIP".equals(name)) {
					String sensitiveIPStr = node.getTextContent();
					List<String> sensitiveIPList = Arrays.asList(sensitiveIPStr.split(";"));
					securityConfig.setSensitiveIPList(sensitiveIPList);
				}else if("replaceWord".equals(name)) {
					String replaceWord = node.getTextContent();
					securityConfig.setReplaceWord(replaceWord);
				}else if("regexList".equals(name)) {
					NodeList regexNodeList = node.getChildNodes();
					List<String> regexWordList = new ArrayList<String>(regexNodeList.getLength());
					List<Pattern> regexPatternList = new ArrayList<Pattern>(regexNodeList.getLength());
					for(int index=0;index<regexNodeList.getLength();index++) {
						Node regexNode = regexNodeList.item(index);
						String regexNodeName = regexNode.getNodeName();
						if("regex".equals(regexNodeName)) {
							String regexContent = regexNode.getTextContent();
							regexWordList.add(regexContent.replace("\\\\", "\\"));
						}
					}
					for(String regexWord : regexWordList) {
						Pattern pattern = Pattern.compile(regexWord);
						regexPatternList.add(pattern);
					}
					securityConfig.setRegexWordList(regexWordList);
					securityConfig.setRegexPatternList(regexPatternList);
				}
			}
		} catch (Exception e) {
			LOGGER.info("dirtyWordFilter init fail");
			securityConfig = null;
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		SecurityRequestWrapper securityRequest;
		try {
			securityRequest = new SecurityRequestWrapper(httpRequest,securityConfig);
		} catch (Exception e1) {
			LOGGER.error("error:",e1);
			throw new ServletException(e1);
		}
		try {
			if(securityConfig==null) {
				chain.doFilter(securityRequest, httpResponse);
				return;
			}
			if(securityRequest.validateParameter()) {
				if(securityConfig.isChain()) {
					httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
					httpResponse.setCharacterEncoding("UTF-8");
					httpResponse.setContentType("application/json; charset=utf-8");
					try (PrintWriter out = httpResponse.getWriter()) {
						String data = new Gson().toJson(
								new ResponseBean<String>(ResultCode.SECURITY_ERROR.getCode(),
										ResultCode.SECURITY_ERROR.getDesc(), null));
						out.append(data);
						out.flush();
					} catch (IOException e) {
						LOGGER.error("ERROR IN WRITE RESPONSE:", e);
					}
					return;
				}else {
					chain.doFilter(securityRequest, httpResponse);
					return;
				}
			}
		} catch (Exception e) {
			LOGGER.error("doFilter fail:",e);
		}
		chain.doFilter(securityRequest, httpResponse);
	}

	@Override
	public void destroy() {
		LOGGER.info("dirtyWordFilter destroy");
	}

}
