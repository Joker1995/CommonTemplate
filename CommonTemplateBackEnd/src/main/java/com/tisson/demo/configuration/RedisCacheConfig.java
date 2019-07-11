package com.tisson.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Title: RedisCacheConfig.java
 * @Package com.tisson.demo.configuration
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年11月12日
 * @version V1.0
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {
	@Bean(name = "jedis.pool")
	@Autowired
	public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
			@Value("${jedis.pool.host}") String host, @Value("${jedis.pool.port}") int port,
			@Value("${jedis.pool.password}")String password,@Value("${jedis.pool.timeout}") int timout) {
		return new JedisPool(config, host, port,timout,password,0);
	}

	@Bean(name = "jedis.pool.config")
	public JedisPoolConfig jedisPoolConfig(@Value("${jedis.pool.config.maxTotal}") int maxTotal,
			@Value("${jedis.pool.config.maxIdle}") int maxIdle,
			@Value("${jedis.pool.config.maxWaitMillis}") int maxWaitMillis) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		return config;
	}
}
