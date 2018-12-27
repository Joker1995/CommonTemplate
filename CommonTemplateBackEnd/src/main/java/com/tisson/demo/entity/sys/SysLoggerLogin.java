package com.tisson.demo.entity.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**  
* @Title: SysLoggerLogin.java  
* @Package com.tisson.demo.entity  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月2日  
* @version V1.0  
*/
@Table(name = "sys_logger_login")
public class SysLoggerLogin{
	@Id
	@GeneratedValue(generator="UUID")
	private String id;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="time")
	private Date time;
	
	@Column(name="code")
	private String code;
	
	@Column(name="result")
	private String result;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "{id:" + id + ", userName:" + userName + ", time:" + time + ", code:" + code + ", result:"
				+ result + "}";
	}
}
