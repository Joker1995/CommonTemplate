package com.tisson.demo.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**  
* @Title: ApplicationContextUtil.java  
* @Package com.tisson.demo.common.util  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月3日  
* @version V1.0  
*/
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext context;
    
    public static ApplicationContext getApplicationContext() {
        return context;
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		/**  
		* @Title: setApplicationContext  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		context=applicationContext;
	}
}
