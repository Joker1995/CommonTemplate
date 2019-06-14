package com.tisson.demo.service.sys;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tisson.demo.common.base.BaseService;
import com.tisson.demo.common.base.JsonSerializer;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.common.base.MailService;
import com.tisson.demo.common.cahce.RedisCache;
import com.tisson.demo.common.cahce.RedisCallBack;
import com.tisson.demo.common.util.IdWorker;
import com.tisson.demo.common.util.JWTUtil;
import com.tisson.demo.entity.sys.SysOrganizations;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;
import com.tisson.demo.mapper.sys.SysUsersMapper;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * @Title: SysUsersService.java
 * @Package com.tisson.demo.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年10月24日
 * @version V1.0
 */
@Service
public class SysUsersService extends BaseService<SysUsers> {
	private final static Logger LOGGER = LoggerFactory.getLogger(SysUsersService.class);
	private final static ThreadPoolTaskScheduler EXECUTOR = new ThreadPoolTaskScheduler();
	{
        //核心线程数目
        EXECUTOR.setPoolSize(20);
        //线程名称前缀
        EXECUTOR.setThreadNamePrefix("userRegister_");
        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //加载
        EXECUTOR.initialize();
	}
	private final static Lock LOCK = new ReentrantLock();
	@Autowired
    private SysUsersMapper sysUsersMapper;
	@Autowired
	private RedisCache cache;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private MailService mailService;
	
	public SysUsers loadByName(String name) {
		return sysUsersMapper.loadByName(name);
	}

	public List<SysRoles> queryRoles(String id) {
		return sysUsersMapper.queryRoles(id);
	}

	public List<SysResources> queryResourcesByUserId(String id) {
		return sysUsersMapper.queryResourcesByUserId(id);
	}
	
	public List<SysResources> queryResourcesByRoleId(List<String> roleIds) {
		List<SysResources> result=new ArrayList<SysResources>();
		for(String roleId:roleIds) {
			result.addAll(sysUsersMapper.queryResourcesByRoleId(roleId));
		}
		return result;
	}
	
	public List<SysPages> queryPagesByUserId(String id) {
		return sysUsersMapper.queryPagesByUserId(id);
	}
	
	public List<SysPages> queryPagesByRoleId(List<String> roleIds) {
		List<SysPages> result=new ArrayList<SysPages>();
		for(String roleId:roleIds) {
			result.addAll(sysUsersMapper.queryPagesByRoleId(roleId));
		}
		return result;
	}

	public PageInfo<SysUsers> queryPage(ListQuery<SysUsers> query) {
		if((query.limit!=null && query.limit>-1)&&(query.page!=null && query.page>-1)) {
			PageHelper.startPage(query.page, query.limit);
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", null);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		initUserRelations(result);
		return new PageInfo<SysUsers>(result);
	}
	
	public PageInfo<SysUsers> queryPage(ListQuery<SysUsers> query,List<String> specialUserName) {
		if((query.limit!=null && query.limit>-1)&&(query.page!=null && query.page>-1)) {
			PageHelper.startPage(query.page, query.limit);
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", specialUserName);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		initUserRelations(result);
		return new PageInfo<SysUsers>(result);
	}
	
	public PageInfo<SysUsers> queryAll(ListQuery<SysUsers> query) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", null);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		initUserRelations(result);
		return new PageInfo<SysUsers>(result);
	}
	public PageInfo<SysUsers> queryAll(ListQuery<SysUsers> query,List<String> specialUserName) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", specialUserName);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		initUserRelations(result);
		return new PageInfo<SysUsers>(result);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void deleteSysUsers(SysUsers sysUsers) {
		sysUsersMapper.deleteSysUsers(sysUsers);
	}
	@Transactional(rollbackFor=Exception.class)
	public void updateUserRoles(SysUsers sysUsers) throws Exception{
		if(null!=sysUsers.getRoleIds() && sysUsers.getRoleIds().size()>0) {
			IdWorker idWorker=IdWorker.getFlowIdWorkerInstance();
			List<Map<String,String>> insertList=new ArrayList<Map<String,String>>
				(sysUsers.getRoleIds().size());
			sysUsers.getRoleIds().stream().forEach(roleId->{
				try {
					Map<String,String> item=new HashMap<String,String>();
					item.put("id", String.valueOf(idWorker.nextId()));
					item.put("roleId", roleId);
					insertList.add(item);
				} catch (Exception e) {
					LOGGER.error("ERROR IN generate queryList:",e);
				}
			});
			sysUsersMapper.deleteUserRoles(sysUsers);
			sysUsersMapper.addUserRoles(insertList,sysUsers.getId());
		}
	}
	@Transactional(rollbackFor=Exception.class)
	public void updateUserResources(SysUsers sysUsers) {
		if(null!=sysUsers.getResourceIds() && sysUsers.getResourceIds().size()>0) {
			IdWorker idWorker=IdWorker.getFlowIdWorkerInstance();
			List<Map<String,String>> insertList=new ArrayList<Map<String,String>>
				(sysUsers.getResourceIds().size());
			sysUsers.getResourceIds().stream().forEach(resourceId->{
				try {
					Map<String,String> item=new HashMap<String,String>();
					item.put("id", String.valueOf(idWorker.nextId()));
					item.put("resourceId", resourceId);
					insertList.add(item);
				} catch (Exception e) {
					LOGGER.error("ERROR IN generate queryList:",e);
				}
			});
			sysUsersMapper.deleteUserResources(sysUsers);
			sysUsersMapper.addUserResources(insertList,sysUsers.getId());
		}
		
	}
	@Transactional(rollbackFor=Exception.class)
	public void updateUserAccessPages(SysUsers sysUsers) {
		if(sysUsers.getAccessPageIds()!=null && sysUsers.getAccessPageIds().size()>0) {
			IdWorker idWorker=IdWorker.getFlowIdWorkerInstance();
			List<Map<String,String>> insertList=new ArrayList<Map<String,String>>
				(sysUsers.getAccessPageIds().size());
			sysUsers.getAccessPageIds().stream().forEach(pageId->{
				try {
					Map<String,String> item=new HashMap<String,String>();
					item.put("id", String.valueOf(idWorker.nextId()));
					item.put("pageId", pageId);
					insertList.add(item);
				} catch (Exception e) {
					LOGGER.error("ERROR IN generate queryList:",e);
				}
			});
			sysUsersMapper.deleteUserAccessPages(sysUsers);
			sysUsersMapper.addUserAccessPages(insertList,sysUsers.getId());
		}
		
	}
	@Transactional(rollbackFor=Exception.class)
	public void saveUserOrganizationRelation(SysUsers sysUsers) {
		if(sysUsers.getOrganizationId()!=null) {
			IdWorker idWorker=IdWorker.getFlowIdWorkerInstance();
			List<Map<String,String>> insertList=new ArrayList<Map<String,String>>(1);
			try {
				Map<String,String> item=new HashMap<String,String>();
				item.put("id", String.valueOf(idWorker.nextId()));
				item.put("organizationId", sysUsers.getOrganizationId());
				insertList.add(item);
			} catch (Exception e) {
				LOGGER.error("ERROR IN generate queryList:",e);
			}
			sysUsersMapper.deleteUserOrganizationRelation(sysUsers);
			sysUsersMapper.insertUserOrganizationRelation(insertList,sysUsers.getId());
		}
	}

	public List<SysOrganizations> queryOrganizationByUserId(String id) {
		return sysUsersMapper.queryOrganizationByUserId(id);
	}


	public List<Map<String,String>> loadTokenList(SysUsers user) {
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		List<String> tokenList=cache.getAllHashKey("ssoToken:"+user.getName());
		for(String token:tokenList) {
			Map<String,String> item=new HashMap<String,String>();
			item.put("expireTime", DateUtil.format(JWTUtil.getExpiresAt(token), "yyyy-MM-dd HH:mm:ss"));
			item.put("authCode", token);
			result.add(item);
		}
		Collections.sort(result, new Comparator<Map<String,String>>(){
			@Override
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				if(o1.containsKey("expireTime") && o2.containsKey("expireTime")) {
					DateTime o1Time=DateUtil.parse(o1.get("expireTime"),"yyyy-MM-dd HH:mm:ss");
					DateTime o2Time=DateUtil.parse(o2.get("expireTime"),"yyyy-MM-dd HH:mm:ss");
					if(o1Time.isAfter(o2Time)) {
						return -1;
					}else if(o1Time.isBefore(o2Time)) {
						return 1;
					}
				}
				return 0;
			}
		});
		return result;
	}
	public void kickOut(List<String> ssoTokens) {
		Type type=new TypeToken<Map<String,String>>(){}.getType();
		for(String token : ssoTokens) {
			String userName=JWTUtil.getUserName(token);
			Map<String,String> resultMap = cache.getHash("ssoToken:"+userName, token, type, new RedisCallBack<Map<String,String>>() {
				@Override
				public <T> T callbackForSingleObject(byte[] data, Type type) {
					return JsonSerializer.deserialize(data, type);
				}
				@Override
				public <T> List<T> callbackForList(byte[] data, Type type) {
					return null;
				}
			});
			resultMap.put("kickOut", "true");
			cache.setHash("ssoToken:"+userName, token, resultMap);
		}
	}


	public void rollBack(List<String> ssoTokens) {
		Type type=new TypeToken<Map<String,String>>(){}.getType();
		for(String token : ssoTokens) {
			String userName=JWTUtil.getUserName(token);
			Map<String,String> resultMap = cache.getHash("ssoToken:"+userName, token, type, new RedisCallBack<Map<String,String>>() {
				@Override
				public <T> T callbackForSingleObject(byte[] data, Type type) {
					return JsonSerializer.deserialize(data, type);
				}
				@Override
				public <T> List<T> callbackForList(byte[] data, Type type) {
					return null;
				}
			});
			resultMap.put("kickOut", "false");
			cache.setHash("ssoToken:"+userName, token, resultMap);
		}
	}
	
	public void startRegisterProcess(SysUsers sysUsers) {
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName("userRegister").orderByDeploymentId().desc().list();
		if(processDefinitionList.size()>0) {
			ProcessDefinition processDefinition=processDefinitionList.get(0);
			Map<String,Object> variables=new HashMap<String,Object>();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		    dateFormat.setLenient(false);
		    Date executeTime=DateUtil.offset(sysUsers.getCreateTime(), DateField.DAY_OF_YEAR, 1).toJdkDate();
			Gson gson = new Gson();
			variables.put("registerEntity", gson.toJson(sysUsers));
			variables.put("executeTime",dateFormat.format(executeTime));
			ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);
			Task task=taskService.createTaskQuery().processInstanceId(processInstance.getId())
					.singleResult();
			taskService.complete(task.getId(),variables);
			LOGGER.info("register user:[{}],executeTime:[{}],processDefinitionId:[{}],processInstanceId:[{}],taskId:[{}]",
					 gson.toJson(sysUsers),dateFormat.format(executeTime),
					 processDefinition.getId(),processInstance.getId(),task.getId());
		}
	}
	
	public List<Task> userTask(String userRoleName,String processDefinitionName) {
		return taskService.createTaskQuery().taskCandidateGroup(userRoleName)
				.processDefinitionName(processDefinitionName).list();
	}
	
	public void registerEmailNotify(DelegateExecution execution) {
		try {
			LOCK.lock();//避免activiti乐观锁发生
			String currentProcessInstanceId=execution.getProcessInstanceId();
			Task task = taskService.createTaskQuery()
					.processInstanceId(currentProcessInstanceId).singleResult();
			Object approvalResult = taskService.getVariable(task.getId(), "approvalResult");
			Object registerUserEntity = taskService.getVariable(task.getId(), "registerEntity");
			if(approvalResult!=null && approvalResult instanceof Boolean) {
				Gson gson=new Gson();
				SysUsers user = gson.fromJson(registerUserEntity.toString(), new TypeToken<SysUsers>() {}.getType());
				String email=user.getEmail();
				Boolean result = (Boolean)approvalResult;
				if(result) {
					String content=String.format("亲爱的用户%s:<br/>您注册的用户已经通过审批!", user.getName());
					mailService.sendHtmlMail(email, "用户注册通过", content);
				}else {
					String content=String.format("亲爱的用户%s:<br/>您注册的用户未能通过审批,请和管理员联系!", user.getName());
					mailService.sendHtmlMail(email, "用户注册未通过", content);
				}
				Date startTime = DateUtil.date().offset(DateField.SECOND, 10);//延迟10s执行
				EXECUTOR.schedule(()->{//当前事件是最后一个节点,通过异步执行完成,不能直接complete
					try {
						Task currentTask = taskService.createTaskQuery()
								.processInstanceId(currentProcessInstanceId).singleResult();
						taskService.complete(currentTask.getId());
					} catch (Exception e) {
						LOGGER.error("ERROR IN registerEmailNotify:",e);;
					}
				},startTime);
			}
		} catch (Exception e) {
			LOGGER.error("ERROR IN registerEmailNotify:",e);;
		}finally {
			LOCK.unlock();
		}
	}
	
	public void registerOverTime(DelegateExecution execution) {
		try {
			String currentProcessInstanceId=execution.getProcessInstanceId();
			Task task = taskService.createTaskQuery()
					.processInstanceId(currentProcessInstanceId).singleResult();
			taskService.setVariable(task.getId(), "approvalResult", Boolean.FALSE);
			LOGGER.info("task id:[{}],currentProcessInstanceId:[{}]",task.getId(),currentProcessInstanceId);
		} catch (Exception e) {
			LOGGER.error("ERROR IN registerOverTime:",e);;
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void approvalRegisterTask(SysUsers loginUser,String taskId,String approvalResult) {
		SysUsersService sysUsersService = ((SysUsersService)AopContext.currentProxy());
		Set<Task> taskSet=new HashSet<Task>();
		List<SysRoles> roleList = loginUser.getRoleList();
		roleList.stream().forEach(role->{
			taskSet.addAll(userTask(role.getName(), "userRegister"));
		});
		taskSet.stream().forEach(task->{
			if(task.getId().equals(taskId)) {
				Object entityJsonStr = taskService.getVariable(taskId,"registerEntity");
				SysUsers registerUser = null;
				Map<String,Object> variables=new HashMap<String,Object>();
				if(entityJsonStr!=null) {
					Gson gson = new Gson();
					registerUser = gson.fromJson(Optional.of(entityJsonStr).map(Object::toString).get(),
							SysUsers.class);
				}else {
					return;
				}
				if(approvalResult.equals("0")) {
					registerUser.setStatus(SysUsers.Status.EFFECTIVE.getStatus());
					sysUsersService.update(registerUser);
					LOGGER.info("registerUser:[{}]",new Gson().toJson(registerUser));
					variables.put("approvalResult", Boolean.TRUE);
					taskService.complete(taskId,variables);
				}else {
					sysUsersService.deleteById(registerUser.getId());
					variables.put("approvalResult", Boolean.FALSE);
					taskService.complete(taskId,variables);
				}
				return;
			}
		});
	}
	
	private void initUserRelations(List<SysUsers> userList) {
		for(SysUsers user : userList) {
			List<SysRoles> roleList = sysUsersMapper.queryRoles(user.getId());
			List<String> roleIds=new ArrayList<String>();
			for(SysRoles role : roleList) {
				roleIds.add(role.getId());
			}
			user.setRoleIds(roleIds);
			
			List<SysOrganizations> organizationList = queryOrganizationByUserId(user.getId());
			String organizationId=null;
			if(organizationList.size()>0) {
				organizationId=organizationList.get(0).getId();
			}
			user.setOrganizationId(organizationId);
			
			Set<SysPages> pageSet=new HashSet<SysPages>();
			List<String> pageIds=new ArrayList<String>();
			List<SysPages> userAccessPageList = queryPagesByUserId(user.getId());
			List<SysPages> roleAccessPageList = queryPagesByRoleId(roleIds);
			pageSet.addAll(roleAccessPageList);
			pageSet.addAll(userAccessPageList);
			for(SysPages page : new ArrayList<SysPages>(pageSet)) {
				pageIds.add(page.getId());
			}
			user.setAccessPageIds(pageIds);
			
			Set<SysResources> resourceSet=new HashSet<SysResources>();
			List<String> resourceIds=new ArrayList<String>();
			List<SysResources> userResourceList = queryResourcesByUserId(user.getId());
			List<SysResources> roleResourceList = queryResourcesByRoleId(roleIds);
			resourceSet.addAll(roleResourceList);
			resourceSet.addAll(userResourceList);
			for(SysResources resource : new ArrayList<SysResources>(resourceSet)) {
				resourceIds.add(resource.getId());
			}
			user.setResourceIds(resourceIds);
		}
	}
}
