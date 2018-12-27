package com.tisson.demo.common.codeGenerate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**  
* @Title: MySQLMetaData.java  
* @Package com.tisson.demo.common.codeGenerate  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月3日  
* @version V1.0  
*/
public class MySQLMetaData implements DBMetaData{
	private Connection conn;
	
	public MySQLMetaData(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<Map<String, String>> getTables() throws Exception {
		/**  
		* @Title: getTables  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/ 
		List<Map<String, String>> result=new ArrayList<Map<String, String>>();
		String executeSQL="select * from information_schema.tables where table_schema = ( select database() )";
		ResultSet rs = conn.createStatement().executeQuery(executeSQL);
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		while(rs.next()) {
			Map<String, String> rowData =new HashMap<String,String>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), String.valueOf(rs.getObject(i)));//获取键名及值
			}
			result.add(rowData);
		}
		return result;
	}

	@Override
	public List<Map<String, String>> getColumns(String tableName) throws Exception {
		/**  
		* @Title: getColumns  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/ 
		List<Map<String, String>> result=new ArrayList<Map<String, String>>();
		String executeSQL="select * from information_schema.columns where table_schema = ( select database() ) and table_name = ?";
		PreparedStatement statement = conn.prepareStatement(executeSQL);
		statement.setString(1, tableName);
		ResultSet rs = statement.executeQuery();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		while(rs.next()) {
			Map<String, String> rowData =new HashMap<String,String>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), String.valueOf(rs.getObject(i)));//获取键名及值
			}
			result.add(rowData);
		}
		return result;
	}

	@Override
	public List<Map<String, String>> getTableKeys(String tableName) throws Exception {
		/**  
		* @Title: getTableKeys  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		List<Map<String, String>> result=new ArrayList<Map<String, String>>();
		String executeSQL="select " + 
				"	table_info.constraint_name, table_info.constraint_type, column_info.columns," + 
				"	column_info.referenced_table_schema, column_info.referenced_table_name, column_info.referenced_column_name" + 
				" from ( " + 
				"	select " + 
				"		constraint_schema, table_name, constraint_name, group_concat( column_name ) as columns," + 
				"		referenced_table_schema, referenced_table_name, referenced_column_name" + 
				"  from information_schema.key_column_usage " + 
				"  where constraint_schema = ( select database() ) and table_name =? " + 
				"  group by constraint_schema, constraint_name, referenced_table_schema, referenced_table_name, referenced_column_name " + 
				"	) column_info" + 
				"	join" + 
				"	( " + 
				"	select " + 
				"		constraint_schema, table_name, constraint_name, constraint_type " + 
				"	from information_schema.table_constraints" + 
				"  where constraint_schema = ( select database() ) and table_name =? " + 
				"	) table_info" + 
				"	on column_info.constraint_schema = table_info.constraint_schema" + 
				"  and column_info.table_name = table_info.table_name" + 
				"  and column_info.constraint_name = table_info.constraint_name ";
		PreparedStatement statement = conn.prepareStatement(executeSQL);
		statement.setString(1, tableName);
		statement.setString(2, tableName);
		ResultSet rs = statement.executeQuery();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		while(rs.next()) {
			Map<String, String> rowData =new HashMap<String,String>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), String.valueOf(rs.getObject(i)));//获取键名及值
			}
			result.add(rowData);
		}
		return result;
	}

}
