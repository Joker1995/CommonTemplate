package com.tisson.demo.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tisson.demo.common.base.BaseService;
import com.tisson.demo.entity.sys.SysOrganizations;

/**  
* @Title: SysRolesService.java  
* @Package com.tisson.demo.service  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年10月24日  
* @version V1.0  
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class SysOrganizationService extends BaseService<SysOrganizations>{

}
