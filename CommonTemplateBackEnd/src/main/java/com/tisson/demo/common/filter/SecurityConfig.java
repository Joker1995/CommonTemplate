package com.tisson.demo.common.filter;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class SecurityConfig {
	/**
	 * 是否开启header校验
	 */
	private boolean isCheckHeader;
	
	/**
	 * 是否开启parameter校验
	 */
	private boolean isCheckParams;
	
	/**
	 * 是否开启替换
	 */
	private boolean isReplace;
	
	/**
	 * 是否中断操作
	 */
	private boolean isChain;
	
	/**
	 * 替换用语
	 */
	private String replaceWord;
	
	private List<String> sensitiveIPList;
	
	private List<String> regexWordList;
	
	private List<Pattern> regexPatternList;
	
	private SecurityConfig() {}
	
	public static class SecurityConfigHolder{
		private static SecurityConfig config = new SecurityConfig();
		
		public static SecurityConfig getInstance() {
			return config;
		}
	}

	public boolean isCheckHeader() {
		return isCheckHeader;
	}

	public void setCheckHeader(boolean isCheckHeader) {
		this.isCheckHeader = isCheckHeader;
	}

	public boolean isCheckParams() {
		return isCheckParams;
	}

	public void setCheckParams(boolean isCheckParams) {
		this.isCheckParams = isCheckParams;
	}

	public boolean isReplace() {
		return isReplace;
	}

	public boolean isChain() {
		return isChain;
	}

	public void setChain(boolean isChain) {
		this.isChain = isChain;
	}

	public List<String> getSensitiveIPList() {
		return sensitiveIPList;
	}

	public void setSensitiveIPList(List<String> sensitiveIPList) {
		this.sensitiveIPList = sensitiveIPList;
	}

	public List<String> getRegexWordList() {
		return regexWordList;
	}

	public void setRegexWordList(List<String> regexWordList) {
		this.regexWordList = regexWordList;
	}

	public void setReplace(boolean isReplace) {
		this.isReplace = isReplace;
	}

	public List<Pattern> getRegexPatternList() {
		return regexPatternList;
	}

	public void setRegexPatternList(List<Pattern> regexPatternList) {
		this.regexPatternList = regexPatternList;
	}

	public String getReplaceWord() {
		return replaceWord;
	}

	public void setReplaceWord(String replaceWord) {
		this.replaceWord = replaceWord;
	}
}
