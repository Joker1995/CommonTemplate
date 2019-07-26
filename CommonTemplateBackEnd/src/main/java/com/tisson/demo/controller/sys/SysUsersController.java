package com.tisson.demo.controller.sys;

import java.awt.Font;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

import com.tisson.demo.common.shiro.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.crazycake.shiro.RedisCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.google.gson.Gson;
import com.tisson.demo.common.base.GlobalConstant;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.cahce.RedisCache;
import com.tisson.demo.common.expt.CaptchaValidateException;
import com.tisson.demo.common.expt.RegisterUserException;
import com.tisson.demo.common.expt.UserNameOrPwdException;
import com.tisson.demo.common.shiro.JWTToken;
import com.tisson.demo.common.util.JWTUtil;
import com.tisson.demo.configuration.GlobalProperties;
import com.tisson.demo.entity.sys.SysOrganizations;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;
import com.tisson.demo.service.sys.SysUsersService;
import com.wf.captcha.Captcha;
import com.wf.captcha.GifCaptcha;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.util.StringUtil;

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
@Api("/user")
public class SysUsersController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SysUsersController.class);
	@Autowired
	private SysUsersService sysUsersService;
	@Autowired
	private GlobalProperties globalProperties;
	@Autowired
	private RedisCache cache;
	@Autowired
	private RedisCacheManager redisCacheManager;
	@Autowired
	private AuthRealm realm;

	@PostMapping(value = "/usersList")
	@RequiresPermissions("/user/usersList")
	@ApiOperation(value = "获取用户列表", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "query", value = "列表查询项", required = true, dataType = "ListQuery"), })
	public ResponseBean<PageInfo<SysUsers>> usersList(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token,
			@RequestBody @Validated ListQuery<SysUsers> query) {
		String userName = JWTUtil.getUserName(token);
		String specialUserName = globalProperties.getSpecialUserName();
		List<String> specialUserNameList = null;
		boolean isContainSpecialUserId = false;
		PageInfo<SysUsers> result = null;

		if (!StringUtils.isEmpty(specialUserName)) {
			specialUserNameList = Arrays.asList(specialUserName.split(","));
			for (String name : specialUserNameList) {
				if (userName.equals(name)) {
					isContainSpecialUserId = true;
					break;
				}
			}
		}

		if (query.getLimit() != null && query.getPage() != null) {
			if (!isContainSpecialUserId) {
				result = sysUsersService.queryPage(query, specialUserNameList);
			} else {
				result = sysUsersService.queryPage(query);
			}
		} else {
			if (!isContainSpecialUserId) {
				result = sysUsersService.queryAll(query, specialUserNameList);
			} else {
				result = sysUsersService.queryAll(query);
			}
		}
		List<SysUsers> list = result.getList();
		for (SysUsers item : list) {
			item.setPassword(null);
		}
		return new ResponseBean<PageInfo<SysUsers>>("query usersList success", result);
	}

	@PostMapping(value = "/downloadUserList")
	@ApiOperation(value = "导出用户列表", httpMethod = "POST", response = Void.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "query", value = "列表查询项", required = true, dataType = "ListQuery"), })
	public void exportUserList(HttpServletResponse resp, @RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token,
			@RequestBody @Validated ListQuery<SysUsers> query) {
		String generatePath = globalProperties.getExcelGenerateDirPath() + File.separator + "sysUser" + File.separator
				+ UUID.randomUUID().toString().replace("-", "") + File.separator + "userList.xls";
		String templateFile = globalProperties.getExcelTemplateDirPath() + "SysUsers.xls";
		resp.setHeader("content-type", "application/octet-stream;charset=utf-8");
		if (FileUtil.exist(generatePath)) {
			FileUtil.del(generatePath);
		}

		File exportFile = FileUtil.touch(generatePath);
		FileUtil.copy(templateFile, generatePath, true);
		// 写入xls文件
		String userName = JWTUtil.getUserName(token);
		String specialUserName = globalProperties.getSpecialUserName();
		List<String> specialUserNameList = null;
		boolean isContainSpecialUserId = false;
		PageInfo<SysUsers> result = null;
		if (!StringUtils.isEmpty(specialUserName)) {
			specialUserNameList = Arrays.asList(specialUserName.split(","));
			for (String name : specialUserNameList) {
				if (userName.equals(name)) {
					isContainSpecialUserId = true;
					break;
				}
			}
		}
		if (!isContainSpecialUserId) {
			result = sysUsersService.queryAll(query, specialUserNameList);
		} else {
			result = sysUsersService.queryAll(query);
		}

		List<SysUsers> list = result.getList();
		if (list.size() > 0) {
			try (ExcelWriter writer = ExcelUtil.getWriter(exportFile)) {
				int rowIndex = 1;
				for (SysUsers user : list) {
					Map<String, String> propMap = user.propStrMap();
					String name = propMap.get("name");
					String phone = propMap.get("mobilePhone");
					String label = propMap.get("label");
					String status = (propMap.get("status") != null && propMap.get("status").equals("S0A")) ? "有效"
							: "无效";
					String sex = (propMap.get("sex") != null && propMap.get("sex").equals("0")) ? "男" : "女";
					String effDate = propMap.get("effDate");
					String expDate = propMap.get("expDate");

					writer.writeCellValue(0, rowIndex, name);
					writer.writeCellValue(1, rowIndex, phone);
					writer.writeCellValue(2, rowIndex, label);
					writer.writeCellValue(3, rowIndex, status);
					writer.writeCellValue(4, rowIndex, sex);
					writer.writeCellValue(5, rowIndex, effDate);
					writer.writeCellValue(6, rowIndex, expDate);
					rowIndex++;
				}
				writer.flush();
			}
		}
		// 写入流
		try (OutputStream out = resp.getOutputStream(); FileInputStream fin = IoUtil.toStream(exportFile);) {
			byte[] data = new byte[2014];
			int len = 0;
			while ((len = fin.read(data)) > 0) {
				out.write(data, 0, len);
			}
			out.flush();
		} catch (Exception e) {
			LOGGER.error("ERROR:", e);
		}
	}

	@GetMapping(value = "/{id}")
	@RequiresPermissions("/user/loadUserById")
	@ApiOperation(value = "获取id为{id}的用户", httpMethod = "GET", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String"), })
	public ResponseBean<SysUsers> loadUserById(@PathVariable("id") @NotEmpty String id) {
		SysUsers user = sysUsersService.loadById(id);
		user.setPassword(null);
		return new ResponseBean<SysUsers>("loadUserById:" + id + " success", user);
	}

	@GetMapping(value = "/role/{id}")
	@RequiresPermissions("/user/loadUserRolesById")
	@ApiOperation(value = "获取id为{id}的用户角色列表", httpMethod = "GET", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String"), })
	public ResponseBean<List<SysRoles>> loadUserRolesById(@PathVariable("id") @NotEmpty String id) {
		List<SysRoles> roles = sysUsersService.queryRoles(id);
		return new ResponseBean<List<SysRoles>>("loadUserRolesById:" + id + " success", roles);
	}

	@GetMapping(value = "/accessPage/{id}")
	@RequiresPermissions("/user/loadUserAccessPagesById")
	@ApiOperation(value = "获取id为{id}的用户可授权界面列表", httpMethod = "GET", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String"), })
	public ResponseBean<List<SysPages>> loadUserAccessPagesById(@PathVariable("id") @NotEmpty String id) {
		List<SysRoles> roles = sysUsersService.queryRoles(id);
		List<SysPages> sysPagesList = sysUsersService.queryPagesByUserId(id);
		List<String> roleIds = new ArrayList<String>();
		for (SysRoles item : roles) {
			roleIds.add(item.getId());
		}
		Set<SysPages> distinctSet = new HashSet<SysPages>(sysPagesList);
		distinctSet.addAll(sysUsersService.queryPagesByRoleId(roleIds));
		return new ResponseBean<List<SysPages>>("loadUserAccessPagesById:" + id + " success",
				new ArrayList<SysPages>(distinctSet));
	}

	@GetMapping(value = "/resource/{id}")
	@RequiresPermissions("/user/loadUserResourcesById")
	@ApiOperation(value = "获取id为{id}的用户可授权接口列表", httpMethod = "GET", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String"), })
	public ResponseBean<List<SysResources>> loadUserResourcesById(@PathVariable("id") @NotEmpty String id) {
		List<SysRoles> roles = sysUsersService.queryRoles(id);
		List<SysResources> sysPagesList = sysUsersService.queryResourcesByUserId(id);
		List<String> roleIds = new ArrayList<String>();
		for (SysRoles item : roles) {
			roleIds.add(item.getId());
		}
		Set<SysResources> distinctSet = new HashSet<SysResources>(sysPagesList);
		distinctSet.addAll(sysUsersService.queryResourcesByRoleId(roleIds));
		return new ResponseBean<List<SysResources>>("loadUserResourcesById:" + id + " success",
				new ArrayList<SysResources>(distinctSet));
	}

	@PostMapping(value = "/login")
	@ApiOperation(value = "登录", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String"), })
	public ResponseBean<String> login(@RequestParam("userName") @NotEmpty String userName,
			@RequestParam("password") @NotEmpty String password,
			@RequestParam("captcha") @NotEmpty String captcha,
			@RequestParam("captchaToken") @NotEmpty String captchaToken) throws Exception {
		String captchaTokenKey="captcha:" + captchaToken;
		if(!cache.exists(captchaTokenKey)) {
			throw new CaptchaValidateException();
		}
		String cacheCode = cache.get(captchaTokenKey);
		if(StringUtil.isEmpty(cacheCode) || !cacheCode.equals(captcha)) {
			throw new CaptchaValidateException();
		}
		cache.remove(captchaTokenKey);
		SysUsers sysUsers = sysUsersService.loadByName(userName);
		// TODO 改造token
		LOGGER.info("查询出来的密码:[{}],请求中的密码:[{}]", sysUsers.getPassword(),password);
		if (sysUsers.getPassword().equals(password)) {
			String token = JWTUtil.sign(userName, password);
			Subject subject = SecurityUtils.getSubject();
			subject.login(new JWTToken(token));
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("kickOut", "false");
			cache.setHash("ssoToken:" + userName, token, dataMap);
			cache.expire("ssoToken:" + userName,
					Double.valueOf(JWTUtil.EXPIRE_TIME / 1000 + new Random().nextInt(30)).intValue());
			return new ResponseBean<String>(200, "Login Success", token);
		} else {
			throw new UserNameOrPwdException();
		}
	}

	@PostMapping(value = "/register")
	@ApiOperation(value = "注册", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String"),})
	public ResponseBean<String> register(@RequestParam("userName") @NotEmpty String userName,
			@RequestParam("password") @NotEmpty String password,
			@RequestParam("email") @NotEmpty String email) throws Exception {
		SysUsers sysUsers = sysUsersService.loadByName(userName);
		if (sysUsers != null) {
			throw new RegisterUserException();
		} else {
			sysUsers = new SysUsers();
			sysUsers.setName(userName);
			sysUsers.setPassword(password);
			sysUsers.setEmail(email);
			sysUsers.setStatus(SysUsers.Status.INVALID.getStatus());
			sysUsers.setCreateTime(new Date());
			sysUsers.setCreateUser("admin");
			sysUsers.setUpdateTime(new Date());
			sysUsers.setUpdateUser("admin");
			sysUsersService.save(sysUsers);
			// 开始启动注册流程
			return new ResponseBean<String>("register Success", null);
		}
	}

	@PostMapping(value = "/logout")
	@ApiOperation(value = "退出", httpMethod = "POST", response = ResponseBean.class)
	public ResponseBean<String> logout(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token)
			throws Exception {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		cache.delHash("ssoToken:" + JWTUtil.getUserName(token), token);
		//这边是为了程序认证cache bug清除
		cache.del((redisCacheManager.getKeyPrefix()+realm.getAuthenticationCacheName()+":"+token).getBytes());
		return new ResponseBean<String>("logout Success", null);
	}

	@GetMapping(value = "/userInfo")
	@RequiresPermissions("/user/queryLoginUserInfo")
	@ApiOperation(value = "获取登录用户信息", httpMethod = "GET", response = ResponseBean.class)
	public ResponseBean<SysUsers> queryLoginUserInfo(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token) {
		SysUsers userInfo = null;
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		if (principal instanceof SysUsers) {
			userInfo = (SysUsers) principal;
		} else {
			userInfo = sysUsersService.loadByName(JWTUtil.getUserName(token));
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
	@ApiOperation(value = "获取登录用户可授权界面列表", httpMethod = "GET", response = ResponseBean.class)
	public ResponseBean<List<SysPages>> queryAccessPages(
			@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token) {
		SysUsers sysUsers = null;
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		List<SysPages> pagesList = null;
		if (principal instanceof SysUsers) {
			sysUsers = (SysUsers) principal;
			pagesList = sysUsers.getAccessPageList();
		} else {
			sysUsers = sysUsersService.loadByName(JWTUtil.getUserName(token));
			// 根据user的id查询出所有角色
			List<SysRoles> sysRolesList = sysUsersService.queryRoles(sysUsers.getId());
			Set<String> roleIdSet = new HashSet<String>();
			for (SysRoles item : sysRolesList) {
				roleIdSet.add(item.getId());
			}
			// 根据user的id查询出所有可访问页面
			List<SysPages> userPagesList = sysUsersService.queryPagesByUserId(sysUsers.getId());
			List<SysPages> rolePagesList = sysUsersService.queryPagesByRoleId(new ArrayList<String>(roleIdSet));

			// 将list转换为Set<String>
			Set<SysPages> pagesSet = filterAccessPageList(userPagesList);
			pagesSet.addAll(filterAccessPageList(rolePagesList));
			pagesList = new ArrayList<SysPages>(pagesSet);
		}
		return new ResponseBean<List<SysPages>>(200, "queryAccessPages success", pagesList);
	}

	@GetMapping(value = "/organization")
	@RequiresAuthentication
	@ApiOperation(value = "获取登录用户部门列表", httpMethod = "GET", response = ResponseBean.class)
	public ResponseBean<List<SysOrganizations>> queryOrganizations(
			@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token) {
		SysUsers sysUsers = null;
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		List<SysOrganizations> organizationList = null;
		if (principal instanceof SysUsers) {
			sysUsers = (SysUsers) principal;
			organizationList = sysUsers.getOrganizationList();
		} else {
			sysUsers = sysUsersService.loadByName(JWTUtil.getUserName(token));
			organizationList = sysUsersService.queryOrganizationByUserId(sysUsers.getId());
		}
		return new ResponseBean<List<SysOrganizations>>(200, "queryOrganizations success", organizationList);
	}

	@PostMapping
	@RequiresPermissions("/user/addSysUsers")
	@ApiOperation(value = "添加用户", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sysUsers", value = "用户", required = true, dataType = "SysUsers"), })
	public ResponseBean<String> addSysUsers(@RequestBody @Validated SysUsers sysUsers) {
		sysUsersService.save(sysUsers);
		sysUsersService.saveUserOrganizationRelation(sysUsers);
		return new ResponseBean<String>("addSysUsers success", "addSysUsers success");
	}

	@DeleteMapping
	@RequiresPermissions("/user/deleteSysUsers")
	@ApiOperation(value = "删除用户", httpMethod = "DELETE", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sysUsers", value = "用户", required = true, dataType = "SysUsers"), })
	public ResponseBean<String> deleteSysUsers(@RequestBody @Validated SysUsers sysUsers) {
		sysUsersService.deleteSysUsers(sysUsers);
		return new ResponseBean<String>("deleteSysUsers success", "deleteSysUsers success");
	}

	@PutMapping
	@RequiresPermissions("/user/updateSysUsers")
	@ApiOperation(value = "更新用户", httpMethod = "PUT", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sysUsers", value = "用户", required = true, dataType = "SysUsers"), })
	public ResponseBean<String> updateSysUsers(@RequestBody @Validated SysUsers sysUsers) {
		sysUsersService.update(sysUsers);
		sysUsersService.saveUserOrganizationRelation(sysUsers);
		return new ResponseBean<String>("updateSysUsers success", "updateSysUsers success");
	}

	@PutMapping("/accessPage")
	@RequiresPermissions("/user/updateUserAccessPages")
	@ApiOperation(value = "更新用户可授权界面列表", httpMethod = "PUT", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sysUsers", value = "用户", required = true, dataType = "SysUsers"), })
	public ResponseBean<String> updateUserAccessPages(@RequestBody @Validated SysUsers sysUsers) {
		sysUsersService.updateUserAccessPages(sysUsers);
		return new ResponseBean<String>("updateUserAccessPages success", "updateUserAccessPages success");
	}

	@PutMapping("/resource")
	@RequiresPermissions("/user/updateUserResources")
	@ApiOperation(value = "更新用户可授权接口列表", httpMethod = "PUT", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sysUsers", value = "用户", required = true, dataType = "SysUsers"), })
	public ResponseBean<String> updateUserResources(@RequestBody @Validated SysUsers sysUsers) {
		sysUsersService.updateUserResources(sysUsers);
		return new ResponseBean<String>("updateUserResources success", "updateUserResources success");
	}

	@PutMapping("/role")
	@RequiresPermissions("/user/updateUserRoles")
	@ApiOperation(value = "更新用户角色列表", httpMethod = "PUT", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sysUsers", value = "用户", required = true, dataType = "SysUsers"), })
	public ResponseBean<String> updateUserRoles(@RequestBody @Validated SysUsers sysUsers) throws Exception {
		sysUsersService.updateUserRoles(sysUsers);
		return new ResponseBean<String>("updateUserRoles success", "updateUserRoles success");
	}

	@PostMapping(value = "/ssoToken")
	@RequiresPermissions("/user/ssoToken")
	@ApiOperation(value = "获取用户TOKEN列表", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sysUsers", value = "用户", required = true, dataType = "SysUsers"), })
	public ResponseBean<List> loadTokenList(@RequestBody @Validated SysUsers query) {
		return new ResponseBean<List>("loadTokenList success", sysUsersService.loadTokenList(query));
	}

	@PostMapping(value = "/ssoToken/kickOut")
	@RequiresAuthentication
	@ApiOperation(value = "对用户TOKEN列表进行踢出", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ssoTokens", value = "token列表", required = true, dataType = "List<String>"), })
	public ResponseBean<String> kickOut(@RequestBody @NotEmpty List<String> ssoTokens) {
		sysUsersService.kickOut(ssoTokens);
		return new ResponseBean<String>("kickOut success", "kickOut success");
	}

	@PostMapping(value = "/ssoToken/rollBack")
	@RequiresAuthentication
	@ApiOperation(value = "对用户踢出TOKEN列表进行回滚", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ssoTokens", value = "token列表", required = true, dataType = "List<String>"), })
	public ResponseBean<String> rollBack(@RequestBody @NotEmpty List<String> ssoTokens) {
		sysUsersService.rollBack(ssoTokens);
		return new ResponseBean<String>("rollBack success", "rollBack success");
	}

	@PostMapping(value = "/self/password")
	@RequiresAuthentication
	@ApiOperation(value = "更新登录用户密码", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sysUsers", value = "用户", required = true, dataType = "SysUsers"), })
	public ResponseBean<Boolean> updateSelfPassword(@RequestBody @Validated SysUsers sysUsers) {
		SysUsers loginUser = null;
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		if (principal instanceof SysUsers) {
			loginUser = (SysUsers) principal;
			loginUser.setPassword(sysUsers.getPassword());
			sysUsersService.update(loginUser);
			return new ResponseBean<Boolean>("updateSelfPassword success", Boolean.TRUE);
		}
		return new ResponseBean<Boolean>("updateSelfPassword failed", Boolean.FALSE);
	}

	@PostMapping(value = "/{id}/password")
	@RequiresAuthentication
	@ApiOperation(value = "更新id为{id}的用户密码", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String"),
			@ApiImplicitParam(name = "sysUsers", value = "用户", required = true, dataType = "SysUsers"), })
	public ResponseBean<Boolean> updateUserPassword(@PathVariable("id") @NotEmpty String id,
			@RequestBody @Validated SysUsers sysUsers) {
		SysUsers dbUser = sysUsersService.loadById(id);
		dbUser.setPassword(sysUsers.getPassword());
		sysUsersService.update(dbUser);
		return new ResponseBean<Boolean>("updateUserPassword success", Boolean.TRUE);
	}

	@GetMapping(value = "/captcha")
	@ApiOperation(value = "获取验证码", httpMethod = "GET", response = ResponseBean.class)
	public ResponseBean<Map<String, String>> captcha() throws Exception {
		String tempDirPath = globalProperties.getCaptchaDirPath() + File.separator + "tempCaptcha";
		LOGGER.info("验证码存储文件夹路径:[{}]",tempDirPath);
		Map<String, String> result = new HashMap<String, String>();
		String token = UUID.randomUUID().toString().replace("-", "");
		File tempFile = FileUtil.touch(tempDirPath + File.separator + token + ".gif");
		if (!tempFile.getParentFile().exists()) {
			tempFile.mkdirs();
			tempFile.createNewFile();
		}
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));) {
			// 三个参数分别为宽、高、位数
			GifCaptcha gifCaptcha = new GifCaptcha(130, 45, 4);
			gifCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));
			// 设置类型，纯数字、纯字母、字母数字混合
			gifCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
			// 生成的验证码
			String code = gifCaptcha.text();
			gifCaptcha.out(out);
			String context = Base64.encode(tempFile);
			cache.put("captcha:" + token, code, 30, TimeUnit.SECONDS);// 验证码30s
			LOGGER.info("获取验证码===验证码:[{}],验证token:[{}]", code, token);
			result.put("token", token);
			result.put("image", context);
		}
		FileUtil.del(tempFile);
		return new ResponseBean<Map<String, String>>("生成验证码成功", result);
	}

	private Set<SysPages> filterAccessPageList(List<SysPages> list) {
		return new HashSet<SysPages>(list);
	}
}
