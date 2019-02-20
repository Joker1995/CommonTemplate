package com.tisson.demo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "global")
public class GlobalProperties {
	
	private String encyptSalt;

	private String specialUserName;

	private String sessionRedisKey;

	private String excelGenerateDirPath;

	private String excelTemplateDirPath;
	
	private String codeGenerateDirPath;
	
	private String codeTemplateDirPath;

	public String getEncyptSalt() {
		return encyptSalt;
	}

	public void setEncyptSalt(final String encyptSalt) {
		this.encyptSalt = encyptSalt;
	}

	public String getSpecialUserName() {
		return specialUserName;
	}

	public void setSpecialUserName(String specialUserName) {
		this.specialUserName = specialUserName;
	}

	public String getSessionRedisKey() {
		return sessionRedisKey;
	}

	public void setSessionRedisKey(String sessionRedisKey) {
		this.sessionRedisKey = sessionRedisKey;
	}

	public String getExcelGenerateDirPath() {
		return excelGenerateDirPath;
	}

	public void setExcelGenerateDirPath(String excelGenerateDirPath) {
		this.excelGenerateDirPath = excelGenerateDirPath;
	}

	public String getExcelTemplateDirPath() {
		return excelTemplateDirPath;
	}

	public void setExcelTemplateDirPath(String excelTemplateDirPath) {
		this.excelTemplateDirPath = excelTemplateDirPath;
	}

	public String getCodeGenerateDirPath() {
		return codeGenerateDirPath;
	}

	public void setCodeGenerateDirPath(String codeGenerateDirPath) {
		this.codeGenerateDirPath = codeGenerateDirPath;
	}

	public String getCodeTemplateDirPath() {
		return codeTemplateDirPath;
	}

	public void setCodeTemplateDirPath(String codeTemplateDirPath) {
		this.codeTemplateDirPath = codeTemplateDirPath;
	}
}
