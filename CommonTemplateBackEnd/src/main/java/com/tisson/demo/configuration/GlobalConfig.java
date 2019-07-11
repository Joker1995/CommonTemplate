package com.tisson.demo.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.Filter;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.github.pagehelper.PageHelper;
import com.tisson.demo.common.filter.LimitingFilter;
import com.tisson.demo.common.filter.SecurityFilter;

/**
 * @Title: GlobalConfig.java
 * @Package com.tisson.demo.configuration
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年11月9日
 * @version V1.0
 */
@Configuration
public class GlobalConfig {
	
	@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		p.setProperty("dialect", "mysql"); // 配置mysql数据库
		pageHelper.setProperties(p);
		return pageHelper;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties(Environment env) {
	    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
	    YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
	    String[] actvieProfiles = env.getActiveProfiles();
	    if(actvieProfiles.length>0) {
	    	for(String actvieProfile:actvieProfiles) {
		    	yaml.setResources(new ClassPathResource("props/global-"+actvieProfile+".yml"));//class引入
		    }
	    }else {
	    	 yaml.setResources(new ClassPathResource("props/global.yml"));//class引入
	    }
	   
	    configurer.setProperties(yaml.getObject());
	    return configurer;
	}
	
	@Bean
    public ThreadPoolTaskExecutor defaultThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数目
        executor.setCorePoolSize(16);
        //指定最大线程数
        executor.setMaxPoolSize(64);
        //队列中最大的数目
        executor.setQueueCapacity(48);
        //线程名称前缀
        executor.setThreadNamePrefix("defaultThreadPool_");
        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(60);
        //加载
        executor.initialize();
        return executor;
    }
	
	@Bean("securityFilterRegistration")
    public FilterRegistrationBean<Filter> securityFilterRegistration(SecurityFilter securityFilter) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<Filter>();
        registrationBean.setFilter(securityFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(2);
        return registrationBean;
    }
	
	@Bean("limitingFilterRegistration")
    public FilterRegistrationBean<Filter> limitingFilterRegistration(LimitingFilter limitingFilter) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<Filter>();
        registrationBean.setFilter(limitingFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
