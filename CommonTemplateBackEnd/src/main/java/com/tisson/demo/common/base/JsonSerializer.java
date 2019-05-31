package com.tisson.demo.common.base;

import java.lang.reflect.Type;

import com.google.gson.Gson;

import cn.hutool.core.util.CharsetUtil;

/**
 * @Title: JsonSerializer.java
 * @Package com.tisson.crawler.common.cache
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2019年1月7日
 * @version V1.0
 */
public class JsonSerializer {
	public static final String UTF_8 = "UTF-8";
	private static Gson gson = new Gson();

	/**
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> byte[] serialize(T obj) {
		return gson.toJson(obj).getBytes(CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * @param data
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T deserialize(byte[] data,Type type) {
		return gson.fromJson(new String(data), type);
	}
}
