package com.tisson.demo.common.codeGenerate;

import javax.validation.constraints.NotEmpty;

/**  
* @Title: CodeProperties.java  
* @Package com.tisson.demo.common.codeGenerate  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月3日  
* @version V1.0  
*/
public class TaskUnit {
	@NotEmpty
	private String packageName;
	
	private String prefix;
	
	private String suffix;
	@NotEmpty
	private String projectPackageName;
	
	private DataSourceConfig config;
	
	private Table table;
	
	public TaskUnit() {
		super();
	}

	public TaskUnit(String packageName, String prefix, String suffix) {
		super();
		this.packageName = packageName;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public DataSourceConfig getConfig() {
		return config;
	}

	public void setConfig(DataSourceConfig config) {
		this.config = config;
	}

	public String getProjectPackageName() {
		return projectPackageName;
	}

	public void setProjectPackageName(String projectPackageName) {
		this.projectPackageName = projectPackageName;
	}
}
