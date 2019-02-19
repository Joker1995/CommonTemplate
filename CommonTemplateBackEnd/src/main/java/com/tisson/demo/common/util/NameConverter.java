package com.tisson.demo.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Joiner;

/**  
* @Title: NameConverter.java  
* @Package com.tisson.demo.common.util  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月6日  
* @version V1.0  
*/
public class NameConverter {
	private static Map<String,String> typeMappingMap;
	static {
		typeMappingMap=new HashMap<String,String>();
		typeMappingMap.put("CHAR", "String");
		typeMappingMap.put("VARCHAR", "String");
		typeMappingMap.put("LONGVARCHAR", "String");
		typeMappingMap.put("NUMERIC", "java.math.BigDecimal");
		typeMappingMap.put("DECIMAL", "java.math.BigDecimal");
		typeMappingMap.put("BIT", "Boolean");
		typeMappingMap.put("BOOLEAN", "Boolean");
		typeMappingMap.put("TINYINT", "Integer");
		typeMappingMap.put("SMALLINT", "Integer");
		typeMappingMap.put("INT", "Integer");
		typeMappingMap.put("INTEGER", "Integer");
		typeMappingMap.put("BIGINT", "Long");
		typeMappingMap.put("FLOAT", "Double");
		typeMappingMap.put("DOUBLE", "Double");
		typeMappingMap.put("BINARY", "Byte[]");
		typeMappingMap.put("VARBINARY", "Byte[]");
		typeMappingMap.put("LONGVARBINARY", "Byte[]");
		typeMappingMap.put("DATE", "java.util.Date");
		typeMappingMap.put("TIME", "java.util.Date");
		typeMappingMap.put("DATETIME", "java.util.Date");
		typeMappingMap.put("TIMESTAMP", "java.util.Date");
		typeMappingMap.put("TEXT", "String");
	}
	
	private static Map<String,String>  getTypeMappingMap(){
		return Collections.unmodifiableMap(typeMappingMap);
	}
	
	public static String convertNormal2CamelCaseName(String name,boolean isFirstCharLower) {
		String[] names = name.replaceAll( "^_", "" ).split( "_" );
        for ( int index = names.length - 1, end = isFirstCharLower ? 1 : 0; index >= end; index -= 1 ) {
            names[ index ] = names[ index ].replaceAll( "^\\w", Character.toString( names[ index ].charAt( 0 ) ).toUpperCase() );
        }
        return Joiner.on( "" ).join( names );
	}
	
	public static Boolean checkColumnIsNullable(String columnPerperty) {
		return "YES".equals(columnPerperty.toUpperCase())?true:false;
	}
	
	public static String converJdbcType2JavaType(String columnPerperty) {
		return getTypeMappingMap().get(columnPerperty);
	}
}
