package com.tisson.demo.common.codeGenerate;

import java.io.File;
import java.sql.Connection;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

/**  
* @Title: GenerateTask.java  
* @Package com.tisson.demo.common.codeGenerate  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月3日  
* @version V1.0  
*/
@Slf4j
public abstract class GenerateTask implements Callable<File>{
	protected String generateDirPath;
	protected String templateDirPath;
	protected Connection conn;
	public abstract File generate() throws Exception;
	protected TaskUnit unit;
	public GenerateTask(TaskUnit unit) {
		this.unit=unit;
	}
	@Override
	public File call() {
		/**  
		* @Title: call  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		try {
			return generate();
		} catch (Exception e) {
			log.error("GenerateTask is failed.Exception:{}",e);
			return null;
		}
	}

	public String getGenerateDirPath() {
		return generateDirPath;
	}
	public void setGenerateDirPath(String generateDirPath) {
		this.generateDirPath = generateDirPath;
	}
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public String getTemplateDirPath() {
		return templateDirPath;
	}
	public void setTemplateDirPath(String templateDirPath) {
		this.templateDirPath = templateDirPath;
	}
}
