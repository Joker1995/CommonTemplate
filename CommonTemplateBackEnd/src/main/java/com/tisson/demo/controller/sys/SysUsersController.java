package com.tisson.demo.controller.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.tisson.demo.common.base.GlobalConstant;
import com.tisson.demo.common.base.IRedisService;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.base.cahce.RedisCache;
import com.tisson.demo.common.base.expt.UnauthorizedException;
import com.tisson.demo.common.base.shiro.JWTToken;
import com.tisson.demo.common.util.JWTUtil;
import com.tisson.demo.configuration.GlobalProperties;
import com.tisson.demo.entity.sys.SysOrganizations;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;
import com.tisson.demo.service.sys.SysUsersService;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/**  
* @Title: SysUsersController.java  
* @Package com.tisson.demo.controller  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月8日  
* @version V1.0  
*/
@RestController
@RequestMapping("/user")
@SuppressWarnings("rawtypes")
public class SysUsersController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SysUsersController.class);
	@Autowired
	private SysUsersService sysUsersService;
	@Autowired
	private GlobalProperties globalProperties;
//	@Autowired
//	private IRedisService<Boolean> userName2TokenService;
	@Autowired
	private RedisCache cache;
	
	@PostMapping(value = "/usersList")
	@RequiresPermissions("/user/usersList")
	public ResponseBean<PageInfo<SysUsers>> usersList(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token,
			@RequestBody ListQuery<SysUsers> query) {
		String userName = JWTUtil.getUserName(token);
		String specialUserName = globalProperties.getSpecialUserName();
		List<String> specialUserNameList = null;
		boolean isContainSpecialUserId=false;
		PageInfo<SysUsers> result=null;
		
		if(!StringUtils.isEmpty(specialUserName)) {
			specialUserNameList=Arrays.asList(specialUserName.split(","));
			for(String name:specialUserNameList) {
				if(userName.equals(name)) {
					isContainSpecialUserId=true;
					break;
				}
			}
		}
		
		if(query.getLimit()!=null && query.getPage()!=null) {
			if(!isContainSpecialUserId) {
				result=sysUsersService.queryPage(query, specialUserNameList);
			}else {
				result=sysUsersService.queryPage(query);
			}
		}else {
			if(!isContainSpecialUserId) {
				result=sysUsersService.queryAll(query, specialUserNameList);
			}else {
				result=sysUsersService.queryAll(query);
			}
		}
		List<SysUsers> list=result.getList();
		for(SysUsers item:list) {
			item.setPassword(null);
		}
		return new ResponseBean<PageInfo<SysUsers>>("query usersList success",result);
	}
	
	@PostMapping(value = "/downloadUserList")
	public void testDownload(HttpServletResponse resp,@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token,
			@RequestBody ListQuery<SysUsers> query) {
		String generatePath=globalProperties.getExcelGenerateDirPath()+File.separator+"sysUser"
				+File.separator+UUID.randomUUID().toString().replace("-", "")
				+File.separator+"userList.xls";
		String templateFile=globalProperties.getExcelTemplateDirPath()+"SysUsers.xls";
		resp.setHeader("content-type", "application/octet-stream;charset=utf-8");
		if(FileUtil.exist(generatePath)) {
			FileUtil.del(generatePath);
		}
		
		File exportFile = FileUtil.touch(generatePath);
		FileUtil.copy(templateFile, generatePath, true);
		// 写入xls文件
		String userName = JWTUtil.getUserName(token);
		String specialUserName = globalProperties.getSpecialUserName();
		List<String> specialUserNameList = null;
		boolean isContainSpecialUserId=false;
		PageInfo<SysUsers> result=null;
		if(!StringUtils.isEmpty(specialUserName)) {
			specialUserNameList=Arrays.asList(specialUserName.split(","));
			for(String name:specialUserNameList) {
				if(userName.equals(name)) {
					isContainSpecialUserId=true;
					break;
				}
			}
		}
		if(!isContainSpecialUserId) {
			result=sysUsersService.queryAll(query, specialUserNameList);
		}else {
			result=sysUsersService.queryAll(query);
		}
		
		List<SysUsers> list=result.getList();
		if(list.size()>0) {
			try(ExcelWriter writer = ExcelUtil.getWriter(exportFile)){
				int rowIndex=1;
				for(SysUsers user:list) {
					Map<String,String> propMap=user.propStrMap();
					String name=propMap.get("name");
					String phone=propMap.get("mobilePhone");
					String label=propMap.get("label");
					String status=(propMap.get("status")!=null && propMap.get("status").equals("S0A"))?"有效":"无效";
					String sex=(propMap.get("sex")!=null && propMap.get("sex").equals("0"))?"男":"女";
					String effDate=propMap.get("effDate");
					String expDate=propMap.get("expDate");
					
					writer.writeCellValue(0,rowIndex, name);
					writer.writeCellValue(1,rowIndex, phone);
					writer.writeCellValue(2,rowIndex, label);
					writer.writeCellValue(3,rowIndex, status);
					writer.writeCellValue(4,rowIndex, sex);
					writer.writeCellValue(5,rowIndex, effDate);
					writer.writeCellValue(6,rowIndex, expDate);
					rowIndex++;
				}
				writer.flush();
			}
		}
		// 写入流
		try(OutputStream out=resp.getOutputStream();FileInputStream fin = IoUtil.toStream(exportFile);){
			byte[] data=new byte[2014];
			int len = 0;
			while ((len = fin.read(data)) > 0) {
				out.write(data,0,len);
			}
			out.flush();
		}catch(Exception e) {
			LOGGER.error("ERROR:",e);
		}
	}

	@GetMapping(value = "/{id}")
	@RequiresPermissions("/user/loadUserById")
	public ResponseBean<SysUsers> loadUserById(@PathVariable("id") String id) {
		SysUsers user =  sysUsersService.loadById(id);
		user.setPassword(null);
		return new ResponseBean<SysUsers>("loadUserById:"+id+" success",user);
	}
	
	@GetMapping(value = "/role/{id}")
	@RequiresPermissions("/user/loadUserRolesById")
	public ResponseBean<List<SysRoles>> loadUserRolesById(@PathVariable("id") String id) {
		List<SysRoles> roles = sysUsersService.queryRoles(id);
		return new ResponseBean<List<SysRoles>>("loadUserRolesById:"+id+" success",roles);
	}
	
	@GetMapping(value = "/accessPage/{id}")
	@RequiresPermissions("/user/loadUserAccessPagesById")
	public ResponseBean<List<SysPages>> loadUserAccessPagesById(@PathVariable("id") String id) {
		List<SysRoles> roles = sysUsersService.queryRoles(id);
		List<SysPages> sysPagesList=sysUsersService.queryPagesByUserId(id);
		List<String> roleIds = new ArrayList<String>();
		for(SysRoles item:roles) {
			roleIds.add(item.getId());
		}
		Set<SysPages> distinctSet=new HashSet<SysPages>(sysPagesList);
		distinctSet.addAll(sysUsersService.queryPagesByRoleId(roleIds));
		return new ResponseBean<List<SysPages>>("loadUserAccessPagesById:"+id+" success",new ArrayList<SysPages>(distinctSet));
	}
	
	@GetMapping(value = "/resource/{id}")
	@RequiresPermissions("/user/loadUserResourcesById")
	public ResponseBean<List<SysResources>> loadUserResourcesById(@PathVariable("id") String id) {
		List<SysRoles> roles = sysUsersService.queryRoles(id);
		List<SysResources> sysPagesList=sysUsersService.queryResourcesByUserId(id);
		List<String> roleIds = new ArrayList<String>();
		for(SysRoles item:roles) {
			roleIds.add(item.getId());
		}
		Set<SysResources> distinctSet=new HashSet<SysResources>(sysPagesList);
		distinctSet.addAll(sysUsersService.queryResourcesByRoleId(roleIds));
		return new ResponseBean<List<SysResources>>("loadUserResourcesById:"+id+" success",new ArrayList<SysResources>(distinctSet));
	}

	@PostMapping(value = "/login")
	public ResponseBean<String> login(@RequestParam("userName") String userName, 
			@RequestParam("password") String password)throws UnauthorizedException {
		SysUsers sysUsers = sysUsersService.loadByName(userName);
		//TODO 改造token
		LOGGER.info("查询出来的密码:{}",sysUsers.getPassword());
		LOGGER.info("请求中的密码:{}",password);
		if (sysUsers.getPassword().equals(password)) {
			String token=JWTUtil.sign(userName, password);
			Subject subject = SecurityUtils.getSubject();
			subject.login(new JWTToken(token));
			return new ResponseBean<String>(200, "Login Success", token);
		} else {
			return new ResponseBean<String>(500, "Login Fail", null);
		}
	}
	
	@PostMapping(value = "/register")
	public ResponseBean<String> register(@RequestParam("userName") String userName, 
			@RequestParam("password") String password)throws UnauthorizedException {
		SysUsers sysUsers = sysUsersService.loadByName(userName);
		//TODO 改造token
		if(sysUsers!=null) {
			return new ResponseBean<String>("register Fail", "the userName "+userName+" is exist");
		}else {
			sysUsers=new SysUsers();
			sysUsers.setName(userName);
			sysUsers.setPassword(password);
			sysUsersService.save(sysUsers);
			// 添加额外逻辑,如权限等
			return new ResponseBean<String>("register Success", null);
		}
	}
	
	@PostMapping(value = "/logout")
	public ResponseBean<String> logout(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token)throws UnauthorizedException {
		Subject subject = SecurityUtils.getSubject();
        subject.logout();
//        userName2TokenService.remove("ssoToken:" + JWTUtil.getUserName(token), token);
        cache.delHash("ssoToken:" + JWTUtil.getUserName(token), token);
        return new ResponseBean<String>("logout Success",null);
	}
	
	@GetMapping(value = "/userInfo")
	@RequiresPermissions("/user/queryLoginUserInfo")
	public ResponseBean<SysUsers> queryLoginUserInfo(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token){
		SysUsers userInfo= null;
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		if(principal instanceof SysUsers) {
			userInfo=(SysUsers)principal;
		}else {
			userInfo=sysUsersService.loadByName(JWTUtil.getUserName(token));
		}
		userInfo.setPassword(null);
		userInfo.setAccessPageList(null);
		userInfo.setOrganizationList(null);
		userInfo.setResourceList(null);
		userInfo.setRoleList(null);
		return new ResponseBean<SysUsers>("登陆用户信息", userInfo);
	}
	
	@GetMapping(value = "/accessPages")
	@RequiresPermissions("/user/queryAccessPages")
	public ResponseBean<List<SysPages>> queryAccessPages(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token){
		SysUsers sysUsers = null;
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		List<SysPages> pagesList = null;
		if(principal instanceof SysUsers) {
			sysUsers=(SysUsers)principal;
			pagesList=sysUsers.getAccessPageList();
		}else {
			sysUsers=sysUsersService.loadByName(JWTUtil.getUserName(token));
			// 根据user的id查询出所有角色
	        List<SysRoles> sysRolesList = sysUsersService.queryRoles(sysUsers.getId());
	        Set<String> roleIdSet = new HashSet<String>();
	        for(SysRoles item : sysRolesList) {
	        	roleIdSet.add(item.getId());
	        }
			// 根据user的id查询出所有可访问页面
	        List<SysPages> userPagesList = sysUsersService.queryPagesByUserId(sysUsers.getId());
	        List<SysPages> rolePagesList = sysUsersService.queryPagesByRoleId(new ArrayList<String>(roleIdSet));
	        
	        // 将list转换为Set<String>
	        Set<SysPages> pagesSet = filterAccessPageList(userPagesList);
	        pagesSet.addAll( filterAccessPageList(rolePagesList));
	        pagesList=new ArrayList<SysPages>(pagesSet);
		}
		return new ResponseBean<List<SysPages>>(200,"queryAccessPages success",pagesList);
	}
	
	@GetMapping(value = "/organization")
	@RequiresAuthentication
	public ResponseBean<List<SysOrganizations>> queryOrganizations(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token){
		SysUsers sysUsers = null;
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		List<SysOrganizations> organizationList = null;
		if(principal instanceof SysUsers) {
			sysUsers=(SysUsers)principal;
			organizationList=sysUsers.getOrganizationList();
		}else {
			sysUsers=sysUsersService.loadByName(JWTUtil.getUserName(token));
			organizationList = sysUsersService.queryOrganizationByUserId(sysUsers.getId());
		}
		return new ResponseBean<List<SysOrganizations>>(200,"queryOrganizations success",organizationList);
	}
	
	@PostMapping
	@RequiresPermissions("/user/addSysUsers")
	public ResponseBean<String> addSysUsers(@RequestBody SysUsers sysUsers){
		sysUsersService.save(sysUsers);
		sysUsersService.saveUserOrganizationRelation(sysUsers);
		return new ResponseBean<String>("addSysUsers success","addSysUsers success");
	}
	
	@DeleteMapping
	@RequiresPermissions("/user/deleteSysUsers")
	public ResponseBean<String> deleteSysUsers(@RequestBody SysUsers sysUsers){
		sysUsersService.deleteSysUsers(sysUsers);
		return new ResponseBean<String>("deleteSysUsers success","deleteSysUsers success");
	}
	
	@PutMapping
	@RequiresPermissions("/user/updateSysUsers")
	public ResponseBean<String> updateSysUsers(@RequestBody SysUsers sysUsers){
		sysUsersService.update(sysUsers);
		sysUsersService.saveUserOrganizationRelation(sysUsers);
		return new ResponseBean<String>("updateSysUsers success","updateSysUsers success");
	}
	
	@PutMapping("/accessPage")
	@RequiresPermissions("/user/updateUserAccessPages")
	public ResponseBean<String> updateUserAccessPages(@RequestBody SysUsers sysUsers){
		sysUsersService.updateUserAccessPages(sysUsers);
		return new ResponseBean<String>("updateUserAccessPages success","updateUserAccessPages success");
	}
	
	@PutMapping("/resource")
	@RequiresPermissions("/user/updateUserResources")
	public ResponseBean<String> updateUserResources(@RequestBody SysUsers sysUsers){
		sysUsersService.updateUserResources(sysUsers);
		return new ResponseBean<String>("updateUserResources success","updateUserResources success");
	}
	
	@PutMapping("/role")
	@RequiresPermissions("/user/updateUserRoles")
	public ResponseBean<String> updateUserRoles(@RequestBody SysUsers sysUsers)throws Exception{
		sysUsersService.updateUserRoles(sysUsers);
		return new ResponseBean<String>("updateUserRoles success","updateUserRoles success");
	}
	
	
	@PostMapping(value = "/ssoToken")
	@RequiresPermissions("/user/ssoToken")
	public ResponseBean<List> loadTokenList(@RequestBody SysUsers query){
		return new ResponseBean<List>("loadTokenList success",sysUsersService.loadTokenList(query));
	}
	
	@PostMapping(value = "/ssoToken/kickOut")
	@RequiresAuthentication
	public ResponseBean<String> kickOut(@RequestBody List<String> ssoTokens){
		sysUsersService.kickOut(ssoTokens);
		return new ResponseBean<String>("kickOut success","kickOut success");
	}
	
	@PostMapping(value = "/ssoToken/rollBack")
	@RequiresAuthentication
	public ResponseBean<String> rollBack(@RequestBody List<String> ssoTokens){
		sysUsersService.rollBack(ssoTokens);
		return new ResponseBean<String>("rollBack success","rollBack success");
	}
	 
	private Set<SysPages> filterAccessPageList(List<SysPages> list){
		HashSet<SysPages> set = new HashSet<SysPages>();
        for (SysPages resource : list ) {
            set.add(resource);
        }
        return set;
	}
}
