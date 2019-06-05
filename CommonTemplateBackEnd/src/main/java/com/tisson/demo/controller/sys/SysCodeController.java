package com.tisson.demo.controller.sys;


import java.io.BufferedInputStream;
import java.io.File;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.codeGenerate.DataSourceConfig;
import com.tisson.demo.common.codeGenerate.GenerateTask;
import com.tisson.demo.common.codeGenerate.MySQLGenerateTask;
import com.tisson.demo.common.codeGenerate.MySQLMetaData;
import com.tisson.demo.common.codeGenerate.ProjectTask;
import com.tisson.demo.common.codeGenerate.Table;
import com.tisson.demo.common.codeGenerate.TaskUnit;
import com.tisson.demo.common.util.ApplicationContextUtil;
import com.tisson.demo.common.util.NameConverter;
import com.tisson.demo.configuration.GlobalProperties;
import com.zaxxer.hikari.HikariDataSource;

import cn.hutool.core.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: SystemCodeController.java
 * @Package com.tisson.demo.controller.sys
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年12月3日
 * @version V1.0
 */
@RestController
@RequestMapping("/code")
@SuppressWarnings("static-access")
@Api("/code")
@Slf4j
public class SysCodeController {
	@Autowired
	private ApplicationContextUtil contextUtil;
	@Autowired
	private GlobalProperties globalProperties;
	
	@Resource(name = "defaultThreadPool")
	private ThreadPoolTaskExecutor executor;
	
	private final static String DEFAULT_DRIVER_CLASS="com.mysql.jdbc.Driver";

	@GetMapping(value = "/dataSource")
	@RequiresPermissions("/code/dataSource")
	@ApiOperation(value="获取当前应用context中的datasource",httpMethod="GET",  response=ResponseBean.class)
	public ResponseBean<List<Map<String, Object>>> dataBases() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// 将applicationContext转换为ConfigurableApplicationContext
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) contextUtil
						.getApplicationContext();
		// 获取bean工厂并转换为DefaultListableBeanFactory
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext
						.getBeanFactory();
		//这里不用getBeansOfType()因为需要刷新context,
		//但是GenericApplicationContext只能被刷新一次,因此只能通过beanDefinitionNames获取到相应的beanDefinition
		String[] beanNames = defaultListableBeanFactory.getBeanNamesForType(ResolvableType.forRawClass(HikariDataSource.class));
		for(String beanName:beanNames) {
			Map<String, Object> item = new HashMap<String, Object>();
			HikariDataSource dataSource = (HikariDataSource)defaultListableBeanFactory.getBean(beanName);
			item.put("dataBaseName", beanName);
			item.put("userName", dataSource.getUsername());
			item.put("password", dataSource.getPassword());
			item.put("jdbcUrl", dataSource.getJdbcUrl());
			result.add(item);
		}
		return new ResponseBean<List<Map<String, Object>>>("query ApplicationContext dataSourceList success", result);
	}

	@PostMapping(value = "/dataSource/test")
	@RequiresPermissions("/code/dataSource/test")
	@ApiOperation(value="测试当前datasource连接情况",httpMethod="POST",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "config", value = "数据库配置项", required = true, dataType = "DataSourceConfig"),})
	public ResponseBean<Boolean> testDataSourceConnection(@RequestBody @Validated DataSourceConfig config) {
		return new ResponseBean<Boolean>("testDataSourceConnection success", testConnection(config));
	}

	@PostMapping(value = "/dataSource/tables")
	@RequiresPermissions("/code/dataSource/tables")
	@ApiOperation(value="获取当前datasource中的所有表",httpMethod="POST",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "config", value = "数据库配置项", required = true, dataType = "DataSourceConfig"),})
	public ResponseBean<List<Table>> queryDataBaseTables(@RequestBody @Validated DataSourceConfig config) {
		// 将applicationContext转换为ConfigurableApplicationContext
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) contextUtil
				.getApplicationContext();
		if (configurableApplicationContext.getBean(config.getName()) == null ) {
			return new ResponseBean<List<Table>>("queryDataBaseTables failure,bean:" + config.getName() + " isn't exists", null);
		}
		HikariDataSource dataSource = (HikariDataSource) configurableApplicationContext.getBean(config.getName());
		config.setDriverClassName(dataSource.getDriverClassName());
		config.setJdbcUrl(dataSource.getJdbcUrl());
		config.setPassword(dataSource.getPassword());
		config.setUserName(dataSource.getUsername());
		if (!testConnection(config)) {
			return new ResponseBean<List<Table>>("queryDataBaseTables failure,connection was error", null);
		}
		
		List<Table> result=new ArrayList<Table>(); 
		try(Connection conn=dataSource.getConnection()){
			MySQLMetaData metaData=new MySQLMetaData(conn);
			List<Map<String, String>> tablesInfo=metaData.getTables();
			for(Map<String,String> item:tablesInfo) {
				Table table=new Table().setJdbcName(item.get("TABLE_NAME"))
						.setComment(item.get("TABLE_COMMENT"));
				result.add(table);
			}
			return new ResponseBean<List<Table>>("queryDataBaseTables success",result);
		} catch (Exception e) {
			log.error("queryDataBaseTables failure,error:{}",e);
		}
		return new ResponseBean<List<Table>>("queryDataBaseTables failure", null);
	}

	@PostMapping(value = "/dataSource")
	@RequiresPermissions("/code/addDataSource")
	@ApiOperation(value="添加datasource到context",httpMethod="POST",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "config", value = "数据库配置项", required = true, dataType = "DataSourceConfig"),})
	public ResponseBean<Boolean> addDataSource(@RequestBody @Validated DataSourceConfig config) {
		// 将applicationContext转换为ConfigurableApplicationContext
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) contextUtil
				.getApplicationContext();
		if (configurableApplicationContext.containsBean(config.getName())) {
			return new ResponseBean<Boolean>("addDataSource failure,bean:" + config.getName() + " is exists", true);
		}
		
		if (!testConnection(config)) {
			return new ResponseBean<Boolean>("addDataSource failure,connection was error", false);
		}
		// 获取bean工厂并转换为DefaultListableBeanFactory
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext
				.getBeanFactory();
		// 通过BeanDefinitionBuilder创建bean定义
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(HikariDataSource.class);
		// 设置属性
		beanDefinitionBuilder.addPropertyValue("driverClassName", config.getDriverClassName());
		beanDefinitionBuilder.addPropertyValue("jdbcUrl", config.getJdbcUrl());
		beanDefinitionBuilder.addPropertyValue("password", config.getPassword());
		beanDefinitionBuilder.addPropertyValue("username", config.getUserName());
		// 注册bean
		defaultListableBeanFactory.registerBeanDefinition(config.getName(),
				beanDefinitionBuilder.getRawBeanDefinition());
		return new ResponseBean<Boolean>("addDataSource success", true);
	}

	@DeleteMapping(value = "/dataSource")
	@RequiresPermissions("/code/deleteDataSource")
	@ApiOperation(value="删除当前datasource",httpMethod="DELETE",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "config", value = "数据库配置项", required = true, dataType = "DataSourceConfig"),})
	public ResponseBean<Boolean> deleteDataSource(@RequestBody @Validated DataSourceConfig config) {
		// 将applicationContext转换为ConfigurableApplicationContext
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) contextUtil
				.getApplicationContext();
		if (configurableApplicationContext.containsBean(config.getName())
				&& configurableApplicationContext.getBean(config.getName()) instanceof HikariDataSource) {
			// 获取bean工厂并转换为DefaultListableBeanFactory
			DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext
					.getBeanFactory();

			defaultListableBeanFactory.removeBeanDefinition(config.getName());
			return new ResponseBean<Boolean>("deleteDataSource success", true);
		} else {
			return new ResponseBean<Boolean>(
					"deleteDataSource failure,bean isn't exists or bean isn't instance of HikariDataSource", true);
		}
	}

	@PostMapping("/generateCode")
	@RequiresPermissions("/code/generateSimpleCode")
	@ApiOperation(value="生成简易MVC代码",httpMethod="POST",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "unit", value = "生成任务配置项", required = true, dataType = "TaskUnit"),})
	public void generateSimpleCode(HttpServletResponse resp, @RequestBody @Validated TaskUnit unit) throws Exception{
		// TODO
		GenerateTask task = new MySQLGenerateTask(unit);
		unit.setTable(unit.getTable().setJavaName(NameConverter.convertNormal2CamelCaseName(unit.getTable().getJdbcName(), false))
				.setEntityName(NameConverter.convertNormal2CamelCaseName(unit.getTable().getJdbcName(), true)));
		String generatePath = globalProperties.getCodeGenerateDirPath() + File.separator
				+ UUID.randomUUID().toString().replace("-", "");
		String codeTemplateDirPath=globalProperties.getCodeTemplateDirPath();
		FileUtil.mkdir(generatePath);
		log.info("generatePath:{},codeTemplateDirPath:{}",generatePath,codeTemplateDirPath);
		task.setGenerateDirPath(generatePath);
		task.setTemplateDirPath(codeTemplateDirPath);
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) contextUtil
				.getApplicationContext();
		HikariDataSource dataSource=(HikariDataSource) configurableApplicationContext.getBean(unit.getConfig().getName());
		try (Connection conn=dataSource.getConnection();){
			task.setConn(conn);
			File codeFile = task.generate();
			String downLoadFileName = unit.getTable().getJdbcName() + ".zip";
			resp.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(downLoadFileName, "utf-8"));
			try(ServletOutputStream out = resp.getOutputStream();BufferedInputStream inputStream = FileUtil.getInputStream(codeFile);){
				int bitVal = 0;
				byte[] buffer = new byte[1024];
				while (bitVal != -1) {
					bitVal = inputStream.read(buffer);
					out.write(buffer, 0, bitVal);
				}
				out.flush();
			}
			FileUtil.del(codeFile);
		} catch (Exception e) {
			log.error("generateSimpleCode Exception:{}", e);
			throw new Exception("generateSimpleCode error");
		} 
	}
	
	@PostMapping("/generateProject")
	@RequiresPermissions("/code/generateProject")
	@ApiOperation(value="生成简易项目代码",httpMethod="POST",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "unit", value = "生成任务配置项", required = true, dataType = "TaskUnit"),})
	public void generateProject(HttpServletResponse resp, @RequestBody @Validated TaskUnit unit) throws Exception{
		ProjectTask task=new ProjectTask(unit);
		String generatePath = globalProperties.getCodeGenerateDirPath() + File.separator
				+ UUID.randomUUID().toString().replace("-", "");
		FileUtil.mkdir(generatePath);
		String codeTemplateDirPath=globalProperties.getCodeTemplateDirPath();
		task.setGenerateDirPath(generatePath);
		task.setTemplateDirPath(codeTemplateDirPath);
		File codeFile = task.generate();
		try(ServletOutputStream out = resp.getOutputStream();BufferedInputStream inputStream = FileUtil.getInputStream(codeFile);){
			int bitVal = 0;
			byte[] buffer = new byte[1024];
			while (bitVal != -1) {
				bitVal = inputStream.read(buffer);
				out.write(buffer, 0, bitVal);
			}
			out.flush();
		}
		FileUtil.del(codeFile);
	}

	private boolean testConnection(DataSourceConfig config) {
		boolean isFinished = false;
		try {
			if(config.getDriverClassName()==null) {
				config.setDriverClassName(DEFAULT_DRIVER_CLASS);
			}
			Class.forName(config.getDriverClassName());
			// TODO 后边改为通过classDriver选择测试SQL
			try(Connection conn =DriverManager.getConnection(config.getJdbcUrl(), config.getUserName(), config.getPassword());
					ResultSet resultset = conn.createStatement().executeQuery("SELECT 1")){
				while (resultset.next()) {
					int result = resultset.getInt(1);
					if (result == 1) {
						isFinished = true;
					}
				}
			}
		} catch (Exception e) {
			log.error("Test Connection failed: JDBC-URL---{};USER-NAME:{};PASSWORD:{}", config.getJdbcUrl(),
					config.getUserName(), config.getPassword());
			log.error("error:",e);
		}
		return isFinished;
	}
}
