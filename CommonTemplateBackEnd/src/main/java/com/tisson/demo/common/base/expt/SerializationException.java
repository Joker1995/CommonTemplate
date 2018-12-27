package com.tisson.demo.common.base.expt;
/**  
* @Title: SerializationException.java  
* @Package com.tisson.demo.common.base.expt  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月25日  
* @version V1.0  
*/
public class SerializationException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1757763958094566264L;
	public SerializationException(String msg) {
        super(msg);
    }
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}