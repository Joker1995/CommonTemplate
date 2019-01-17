package com.tisson.demo.common.base.expt;
/**  
* @Title: UnauthorizedException.java  
* @Package com.tisson.demo.common.base  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月8日  
* @version V1.0  
*/
public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 4986254581324696215L;

	public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException() {
        super();
    }
}
