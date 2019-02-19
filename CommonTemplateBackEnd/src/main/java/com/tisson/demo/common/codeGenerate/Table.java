package com.tisson.demo.common.codeGenerate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

/**  
* @Title: Table.java  
* @Package com.tisson.demo.common.base  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月3日  
* @version V1.0  
*/
public class Table {
	// 数据库表的名称
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String jdbcName;

    // java 实体的名称(首字母大写)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String javaName;
	
	// java 实体的名称(首字母小写)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String entityName;

    // 数据库表的描述
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String comment;

    // 数据库表所有的行
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Column> columns =new ArrayList<Column>();

	public String getJdbcName() {
		return jdbcName;
	}

	public Table setJdbcName(String jdbcName) {
		this.jdbcName = jdbcName;
		return this;
	}

	public String getJavaName() {
		return javaName;
	}

	public Table setJavaName(String javaName) {
		this.javaName = javaName;
		return this;
	}

	public String getComment() {
		return comment;
	}

	public Table setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public Table setColumns(List<Column> columns) {
		this.columns = columns;
		return this;
	}
    
	public List<String> getColumnNames() {
        List<String> names = new ArrayList<>();
        for ( Column item : columns ) {
            names.add( item.getJavaName() );
        }
        return names;
    }
	
	public String getEntityName() {
		return entityName;
	}

	public Table setEntityName(String entityName) {
		this.entityName = entityName;
		return this;
	}

	@Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !( o instanceof Table ) ) return false;
        Table that = ( Table ) o;
        return Objects.equals( getJdbcName(), that.getJdbcName() ) &&
                Objects.equals( getJavaName(), that.getJavaName() ) &&
                Objects.equals( getComment(), that.getComment() ) &&
                Objects.equals( getEntityName(), that.getEntityName() ) &&
                Objects.equals( getColumns(), that.getColumns() ) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash( getJdbcName(), getJavaName(), getComment(), getColumns(), getEntityName());
    }

	@Override
	public String toString() {
		return "Table [jdbcName=" + jdbcName + ", javaName=" + javaName + ", entityName=" + entityName + ", comment="
				+ comment + ", columns=" + columns + "]";
	}
}
