package com.tisson.demo.common.base;
/**  
* @Title: ResultCode.java  
* @Package com.tisson.demo.common.base  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2019年2月20日  
* @version V1.0  
*/
public enum ResultCode {
	URL_UNAUTHORIZED(401,"URL未授权"),
	INTERNAL_SERVER_ERROR(1000,"系统内部错误"),
	TOKEN_INVALIDATE_ERROR(1001,"Token失效"),
	USERNAME_OR_PWD_ERROR(1002,"用户名和密码校验失败"),
	UNAUTHORIZED_ERROR(1003,"接口未授权或无授权码"),
	SESSION_KICKOUT_ERROR(1004,"会话无效"),
	PARAMS_ERROR(1005,"请求参数错误"),
	REQUEST_METHOD_UNSUPPORTED_ERROR(1006,"请求方法错误"),
	SESSION_ONLINE_LIMIT_ERROR(1007,"在线会话超出限制"),
	PARAMS_VALIDATE_FAILURE(1008,"参数校验失败"),
	CAPTCHA_INVALIDATE_ERROR(1009,"验证码校验失败"),
	PROCESS_RESOURCE_FILE_ERROR(1010,"流程文件异常"),
	REGISTER_USER_ERROR(1011,"注册用户名已存在或注册参数格式错误"),
	RATE_LIMIT_ERROR(1012,"请求次数超出限制"),
	SECURITY_ERROR(1013,"请求安全过滤失败");
	
	
	private int code;
	private String desc;
	private ResultCode(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
