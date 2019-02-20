package com.tisson.demo.common.expt;
/**  
* @Title: SessionKickoutException.java  
* @Package com.tisson.demo.common.expt  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2019年2月20日  
* @version V1.0  
*/
public class SessionKickoutException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SessionKickoutException(String msg) {
        super(msg);
    }

    public SessionKickoutException() {
        super();
    }
}
