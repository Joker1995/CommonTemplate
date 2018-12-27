package com.tisson.demo.common.codeGenerate;

import java.util.Objects;

/**  
* @Title: Column.java  
* @Package com.tisson.demo.common.base  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月3日  
* @version V1.0  
*/
public class Column {
	// 字段描述
	private String comment;
	// 表字段名
	private String jdbcName;
	// java 实体属性名
	private String javaName;
	// 表字段类型
	private String jdbcType;
	// java 属性类型名
	private String javaType;
	// 属性能否为空
	private boolean notNull;
	// 是否主键
	private boolean primaryKey;
	// java 属性 getter 名
	private String getterName;
	// java 属性 setter 名
	private String setterName;
	
	public String getJdbcName() {
        return jdbcName;
    }
	
	public Column setJdbcName( String jdbcName ) {
        this.jdbcName = jdbcName;
        return this;
    }

    public String getJavaName() {
        return javaName;
    }

    public Column setJavaName( String javaName ) {
        this.javaName = javaName;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Column setComment( String comment ) {
        this.comment = comment;
        return this;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public Column setJdbcType( String jdbcType ) {
        this.jdbcType = jdbcType;
        return this;
    }

    public String getJavaType() {
        return javaType;
    }

    public Column setJavaType( String javaType ) {
        this.javaType = javaType;
        return this;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public Column setNotNull( boolean notNull ) {
        this.notNull = notNull;
        return this;
    }

    public String getGetterName() {
        return getterName;
    }

    public Column setGetterName( String getterName ) {
        this.getterName = getterName;
        return this;
    }

    public String getSetterName() {
        return setterName;
    }

    public Column setSetterName( String setterName ) {
        this.setterName = setterName;
        return this;
    }

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public Column setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash( getComment(), getJdbcName(), getJavaName(), getJdbcType(), 
				getJavaType(), isNotNull(), isPrimaryKey(), getGetterName(), getSetterName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Column other = (Column) obj;
		return isPrimaryKey() == other.isPrimaryKey() &&
				Objects.equals( getComment(), other.getComment() ) &&
                Objects.equals( getJdbcName(), other.getJdbcName() ) &&
                Objects.equals( getJavaName(), other.getJavaName() ) &&
                Objects.equals( getJdbcType(), other.getJdbcType() ) &&
                Objects.equals( getJavaType(), other.getJavaType() ) &&
                Objects.equals( getGetterName(), other.getGetterName() ) &&
                Objects.equals( getSetterName(), other.getSetterName() );
	}

	@Override
    public String toString() {
        return "ColumnEntity{" +
                "comment='" + comment + '\'' +
                ", jdbcName='" + jdbcName + '\'' +
                ", javaName='" + javaName + '\'' +
                ", jdbcType='" + jdbcType + '\'' +
                ", javaType='" + javaType + '\'' +
                ", notNull=" + notNull +
                ", primaryKey=" + primaryKey +
                ", getterName='" + getterName + '\'' +
                ", setterName='" + setterName + '\'' +
                '}';
    }
}
