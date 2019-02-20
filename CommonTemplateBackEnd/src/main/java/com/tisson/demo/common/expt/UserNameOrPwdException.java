package com.tisson.demo.common.expt;
/**  
* @Title: UserNameOrPwdException.java  
* @Package com.tisson.demo.common.expt  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2019年2月20日  
* @version V1.0  
*/
public class UserNameOrPwdException  extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNameOrPwdException(String msg) {
        super(msg);
    }

    public UserNameOrPwdException() {
        super();
    }
}
