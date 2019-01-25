package com.tisson.demo.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
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
//	@Bean
//	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//		 RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//	                .entryTtl(Duration.ofHours(1)); // 设置缓存有效期一小时
//	        return RedisCacheManager
//	                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
//	                .cacheDefaults(redisCacheConfiguration).build();
//	}
//	
//	/**
//     * 设置数据存入 redis 的序列化方式
//     *
//     * @param redisTemplate
//     * @param factory
//     */
//	@Bean
//	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
//		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
//		redisTemplate.setConnectionFactory(factory);
//		// key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
//		// 所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
//		// 或者JdkSerializationRedisSerializer序列化方式;
//		RedisSerializer<String> redisSerializer = new StringRedisSerializer();// Long类型不可以会出现异常信息;
//		redisTemplate.setKeySerializer(redisSerializer);
//		redisTemplate.setHashKeySerializer(redisSerializer);
//		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
//		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//		return redisTemplate;
//	}
//	
//	/**
//     * 实例化 HashOperations 对象,可以使用 Hash 类型操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForHash();
//    }
//    
//    /**
//     * 实例化 ValueOperations 对象,可以使用 String 操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForValue();
//    }
//    
//    /**
//     * 实例化 ListOperations 对象,可以使用 List 操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public ListOperations<String, String> listOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForList();
//    }
//
//    /**
//     * 实例化 SetOperations 对象,可以使用 Set 操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public SetOperations<String, String> setOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForSet();
//    }
//
//    /**
//     * 实例化 ZSetOperations 对象,可以使用 ZSet 操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public ZSetOperations<String, String> zSetOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForZSet();
//    }
	
	@Value("${spring.redis.password}")
	private String password;
	@Value("${spring.redis.timeout}")
	private int timeout;
	@Value("${spring.redis.cluster.nodes}")
	private String clusterNodes;

	@Bean(name = "jedisPoolConfig")
	@ConfigurationProperties(prefix = "spring.redis.jedis.pool")
	public JedisPoolConfig getRedisConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		return config;
	}
	
	@Bean
	public JedisCluster redisCluster(JedisPoolConfig jedisPoolConfig) {
		// 分割集群节点
	    String[] cNodes = clusterNodes.split(",");
	    Set<HostAndPort> nodes = new HashSet<>();
	    for (String node : cNodes) {
	        // 循环分割为IP,port.
	        String[] hp = node.split(":");
	        // 将ip地址，端口号添加到set集合中
	        nodes.add(new HostAndPort(hp[0], Integer.parseInt(hp[1])));
	    }
	    JedisCluster jedisCluster = new JedisCluster(nodes,timeout,timeout,1000,password,jedisPoolConfig);
	    List<String> nodeList = new ArrayList<>();
	    for (String node : cNodes) {
	        // 将ip地址，端口号添加到set集合中
	        nodeList.add(node);
	    }
	    return jedisCluster;
	}
}
