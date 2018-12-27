package com.tisson.demo.common.base;
/**  
* @Title: ListQuery.java  
* @Package com.tisson.demo.common.base  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月2日  
* @version V1.0  
*/
public class ListQuery<T> {
	public Integer page;
	
	public Integer limit;
	
	public T data;
	
	public ListQuery() {
		super();
	}

	public ListQuery(T data) {
		super();
		this.limit = 20;
		this.page = 1;
		this.data = data;
	}

	public ListQuery(Integer page, Integer limit, T data) {
		super();
		this.page = page;
		this.limit = limit;
		this.data = data;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
