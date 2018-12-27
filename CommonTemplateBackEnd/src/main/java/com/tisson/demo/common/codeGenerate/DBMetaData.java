package com.tisson.demo.common.codeGenerate;

import java.util.List;
import java.util.Map;

/**  
* @Title: DBMetaData.java  
* @Package com.tisson.demo.common.codeGenerate  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月3日  
* @version V1.0  
*/
public interface DBMetaData {
	List<Map<String, String>> getTables()throws Exception;

    List<Map<String, String>> getColumns( String tableName ) throws Exception;

    List<Map<String, String>> getTableKeys( String tableName ) throws Exception;
}
