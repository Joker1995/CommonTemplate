package com.tisson.demo.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tisson.demo.common.cahce.RedisCache;
import com.tisson.demo.common.shiro.AuthRealm;
import com.tisson.demo.common.shiro.JWTFilter;


/**  
* @Title: ShiroConfig.java  
* @Package com.tisson.demo.configuration  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月8日  
* @version V1.0  
*/
@Configuration
public class ShiroConfig {
	@Bean
	public SessionsSecurityManager securityManager(AuthRealm authRealm,RedisCacheManager redisCacheManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		authRealm.setCachingEnabled(true);
		authRealm.setAuthenticationCachingEnabled(true);
		authRealm.setAuthorizationCachingEnabled(true);
		authRealm.setAuthenticationCacheName("authenticationCache");
		authRealm.setAuthorizationCacheName("authorizationCache");
		// 自定义缓存实现 使用redis
		securityManager.setCacheManager(redisCacheManager);
		securityManager.setRealm(authRealm);
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}
	
	@Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
    		JWTFilter jwtFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 没有登陆的用户只能访问登陆页面
//        shiroFilterFactoryBean.setLoginUrl("/auth/login");
        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/auth/index");
        // 未授权界面; ----这个配置了没卵用，具体原因想深入了解的可以自行百度
        //shiroFilterFactoryBean.setUnauthorizedUrl("/auth/403");
        //自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        // 添加jwt过滤器
        filtersMap.put("jwt", jwtFilter);
        shiroFilterFactoryBean.setFilters(filtersMap);
        // 权限控制map.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/user/logout", "anon");
        filterChainDefinitionMap.put("/401", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
	
	@Bean
	public RedisSessionDAO redisSessionDAO(RedisCache cache) {
		RedisSessionDAO redisSessionDAO=new RedisSessionDAO();
		redisSessionDAO.setRedisManager(cache);
		redisSessionDAO.setKeyPrefix("shiro:session:");
		return redisSessionDAO;
	}
	
	@Bean
	public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
		DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSessionDAO);
		return  sessionManager;
	}

	/**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
	public RedisCacheManager redisCacheManager(RedisCache cache) {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(cache);
        return redisCacheManager;
	}
	
	/**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
//	private RedisManager redisManager() {
//		RedisManager redisManager = new RedisManager();
//        redisManager.setHost("127.0.0.1");
//        redisManager.setPort(6379);
//        redisManager.setExpire(1800);// 配置缓存过期时间
//        redisManager.setTimeout(0);
//        return redisManager;
//	}
    /***
     * 授权所用配置
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /***
     * 使授权注解起作用不如不想配置可以在pom文件中加入
     * <dependency>
     *<groupId>org.springframework.boot</groupId>
     *<artifactId>spring-boot-starter-aop</artifactId>
     *</dependency>
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro生命周期处理器
     *
     */
    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
