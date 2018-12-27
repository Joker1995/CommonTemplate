package com.tisson.demo.common.base.expt;
/**  
* @Title: PrincipalIdNullException.java  
* @Package com.tisson.demo.common.base.expt  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月25日  
* @version V1.0  
*/
@SuppressWarnings("rawtypes")
public class PrincipalIdNullException extends RuntimeException  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Principal Id shouldn't be null!";
    
	public PrincipalIdNullException(Class clazz, String idMethodName) {
        super(clazz + " id field: " +  idMethodName + ", value is null\n" + MESSAGE);
    }
}