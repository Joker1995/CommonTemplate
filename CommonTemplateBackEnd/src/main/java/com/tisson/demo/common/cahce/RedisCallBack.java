package com.tisson.demo.common.cahce;

import java.lang.reflect.Type;
import java.util.List;

/**  
* @Title: RedisCallBack.java  
* @Package com.tisson.crawler.common.cache  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2019年1月7日  
* @version V1.0  
*/
@SuppressWarnings("hiding")
public interface RedisCallBack<T>{
	public <T> T callbackForSingleObject(byte[] data,Type type);
	public <T> List<T> callbackForList(byte[] data,Type type);
}
