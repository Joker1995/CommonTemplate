package com.tisson.demo.entity.sys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**  
* @Title: SysCommonResources.java  
* @Package com.tisson.demo.entity  
* @Description: TODO(${table.javaName}的实体类)
* @author System-Auto-Generate
* @date 2019/06/11
* @version V1.0  
*/
@Table(name = "sys_common_resources")
public class  SysCommonResources implements Serializable{
	private static final long serialVersionUID = 1L;

    //  
    @Id
    @GeneratedValue(generator="UUID")
    private String id;
    
    // 文件名 
    @Column(name="name")
    private String name;
    
    
    // 文件路径 
    @Column(name="path")
    private String path;
    
    
    // 文件类型 
    @Column(name="type")
    private Integer type;
    
    
    // 备注 
    @Column(name="desc")
    private String desc;
    
    
    public String getId(){
        return id;
    };
    	
    public void setId ( String id ){
        this.id=id;
    }
    	
    public String getName(){
        return name;
    };
    	
    public void setName ( String name ){
        this.name=name;
    }
    	
    public String getPath(){
        return path;
    };
    	
    public void setPath ( String path ){
        this.path=path;
    }
    	
    public Integer getType(){
        return type;
    };
    	
    public void setType ( Integer type ){
        this.type=type;
    }
    	
    public String getDesc(){
        return desc;
    };
    	
    public void setDesc ( String desc ){
        this.desc=desc;
    }
    	
    
}