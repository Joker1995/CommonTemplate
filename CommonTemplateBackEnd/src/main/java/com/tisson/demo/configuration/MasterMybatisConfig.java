package com.tisson.demo.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import tk.mybatis.spring.annotation.MapperScan;

/**  
* @Title: MasterMybatisConfig.java  
* @Package com.tisson.demo.configuration  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月3日  
* @version V1.0  
*/
@Configuration
@MapperScan(basePackages= {"com.tisson.demo.dao"},
	sqlSessionTemplateRef ="masterSqlSessionTemplate")
public class MasterMybatisConfig {
	@Bean(name = "masterDatasource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
	
	@Bean
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource); 
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            //mapper的xml文件存储路径
        	bean.setMapperLocations(resolver.getResources("classpath*:mapper/master/*.xml"));
            //pojo的别名包
        	bean.setTypeAliasesPackage("com.tisson.demo.entity");
            org.apache.ibatis.session.Configuration config=new org.apache.ibatis.session.Configuration();
            //驼峰命名
            config.setMapUnderscoreToCamelCase(true);
            bean.setConfiguration(config);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    @Bean
    public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory); // 使用上面配置的Factory
        return template;
    }
}
