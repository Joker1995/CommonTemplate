package com.tisson.demo.common.trans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME )
public @interface Translate {
	/**
	 * 翻译类型
	 * @return
	 */
	String category();
	
	/**
	 * 翻译标志
	 * @return
	 */
	String dictKey();
	
	/**
	 * 翻译结果保存成员变量名称,类型不支持嵌套类型,只支持基本类型或包装类
	 * @return
	 */
	String setField();
	
	/**
	 * 翻译结果保存执行方法,方法第一个参数必须为转换结果
	 * @return
	 */
	String setMethod();
}
