package com.tisson.demo.common.base;

import java.util.Map;

/**
 * @Title: ResponseBean.java
 * @Package com.tisson.demo.common.base
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年10月24日
 * @version V1.0
 */
public class ResponseBean<T> {
	// http 状态码
	private int code;

	// 返回信息
	private String msg;

	// 返回的数据
	private T data;

	/**
	 * json数据map 额外数据
	 */
	private Map<String, Object> jsonMap;
	
	public ResponseBean(String msg,T data) {
        this.code=200;
        this.msg=msg;
        this.data=data;
    }
	
	public ResponseBean(int code, String msg, T data, Map<String, Object> jsonMap) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.jsonMap = jsonMap;
    }
	
	public ResponseBean(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	public void setJsonMap(Map<String, Object> jsonMap) {
		this.jsonMap = jsonMap;
	}
}
