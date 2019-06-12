package com.tisson.demo.mapper.sys;

import java.util.List;
import java.util.Map;

import com.tisson.demo.common.base.Mapper;
import com.tisson.demo.entity.sys.SysPages;

/**  
* @Title: SysPagesMapper.java  
* @Package com.tisson.demo.dao.sys  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月12日  
* @version V1.0  
*/
public interface SysPagesMapper extends Mapper<SysPages>{

	List<SysPages> queryAccessPagesChildList(String id);

	void deleteAccessPages(Map<String,Object> params);

	List<SysPages> queryRootPages();

}
