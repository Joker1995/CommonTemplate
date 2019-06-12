package com.tisson.demo.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tisson.demo.common.base.BaseService;
import com.tisson.demo.entity.sys.SysCommonResources;
/**  
* @Title: SysCommonResources.java  
* @Package com.tisson.demo.entity  
* @Description: TODO(SysCommonResources的service)
* @author System-Auto-Generate
* @date 2019/06/11
* @version V1.0  
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class  SysCommonResourcesService extends BaseService<SysCommonResources> {
}