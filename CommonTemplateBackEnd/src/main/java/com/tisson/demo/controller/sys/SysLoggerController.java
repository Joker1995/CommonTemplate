package com.tisson.demo.controller.sys;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tisson.demo.service.sys.SysLoggerLoginService;
import com.tisson.demo.service.sys.SysLoggerOprationService;

/**  
* @Title: SysLoggerController.java  
* @Package com.tisson.demo.controller.sys  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月15日  
* @version V1.0  
*/
@RestController
@RequestMapping("logger")
@SuppressWarnings("unused")
@RequiresPermissions("logger")
public class SysLoggerController {
	@Autowired
	private SysLoggerOprationService sysLoggerOprationService;
	
	@Autowired
	private SysLoggerLoginService sysLoggerLoginService;
}
