package com.tisson.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**  
* @Title: GlobalConfig.java  
* @Package com.tisson.demo.configuration  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月5日  
* @version V1.0  
*/
@Component
@PropertySource(value = "classpath:props/global.properties",encoding="utf-8")
public class GlobalProperties {
	@Value("${global.encypt_salt}")
	private String encyptSalt;

	public String getEncyptSalt() {
		return encyptSalt;
	}
	
	@Value("${global.special_username}")
	private String specialUserName;

	public String getSpecialUserName() {
		return specialUserName;
	}
	
	@Value("${global.session_key}")
	private String sessionRedisKey;

	public String getSessionRedisKey() {
		return sessionRedisKey;
	}
	
	@Value("${global.session_key}")
	private String templatePath;

	public String getTemplatePath() {
		return templatePath;
	}
	@Value("${code.generateDirPath}")
	private String codeGenerateDirPath;

	public String getCodeGenerateDirPath() {
		return codeGenerateDirPath;
	}
	
	@Value("${code.codeTemplateDirPath}")
	private String codeTemplateDirPath;

	public String getCodeTemplateDirPath() {
		return codeTemplateDirPath;
	}
}
