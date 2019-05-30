package com.tisson.demo.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.base.ResultCode;

/**  
* @Title: ExceptionController.java  
* @Package com.tisson.demo.common.controller  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月8日  
* @version V1.0  
*/
@RestController
public class ExceptionController {
	@RequestMapping(path = "/401")
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseBean<String> unauthorized() {
		return new ResponseBean<String>(ResultCode.URL_UNAUTHORIZED.getCode(),
				ResultCode.URL_UNAUTHORIZED.getDesc(), null);
	}
}
