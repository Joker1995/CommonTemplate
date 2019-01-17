package com.tisson.demo.controller.sys;


import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.base.code.DataSourceConfig;
import com.tisson.demo.common.codeGenerate.GenerateTask;
import com.tisson.demo.common.codeGenerate.MySQLGenerateTask;
import com.tisson.demo.common.codeGenerate.MySQLMetaData;
import com.tisson.demo.common.codeGenerate.Table;
import com.tisson.demo.common.codeGenerate.TaskUnit;
import com.tisson.demo.common.util.ApplicationContextUtil;
import com.tisson.demo.common.util.NameConverter;
import com.tisson.demo.configuration.GlobalProperties;
import com.zaxxer.hikari.HikariDataSource;

import cn.hutool.core.io.FileUtil;
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
@Slf4j
public class SysCodeController {
	@Autowired
	private ApplicationContextUtil contextUtil;
	@Autowired
	private GlobalProperties globalProperties;
	
	@Resource(name = "defaultThreadPool")
	private ThreadPoolTaskExecutor executor;

	@GetMapping(value = "/dataSource")
	@RequiresPermissions("/code/dataSource")
	public ResponseBean<List<Map<String, Object>>> dataBases() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, HikariDataSource> dataBaseList = contextUtil.getApplicationContext()
				.getBeansOfType(HikariDataSource.class);
		for (Map.Entry<String, HikariDataSource> entry : dataBaseList.entrySet()) {
			Map<String, Object> item = new HashMap<String, Object>();
			HikariDataSource dataSource = entry.getValue();
			item.put("dataBaseName", entry.getKey());
			item.put("userName", dataSource.getUsername());
			item.put("password", dataSource.getPassword());
			item.put("jdbcUrl", dataSource.getJdbcUrl());
			result.add(item);
		}
		return new ResponseBean<List<Map<String, Object>>>("query ApplicationContext dataSourceList success", result);
	}

	@PostMapping(value = "/dataSource/test")
	@RequiresPermissions("/code/dataSource/test")
	public ResponseBean<Boolean> testDataSourceConnection(@RequestBody DataSourceConfig config) {
		return new ResponseBean<Boolean>("testDataSourceConnection success", testConnection(config));
	}

	@PostMapping(value = "/dataSource/tables")
	@RequiresPermissions("/code/dataSource/tables")
	public ResponseBean<List<Table>> queryDataBaseTables(@RequestBody DataSourceConfig config) {
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
	public ResponseBean<Boolean> addDataSource(@RequestBody DataSourceConfig config) {
		// 将applicationContext转换为ConfigurableApplicationContext
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) contextUtil
				.getApplicationContext();
		if (configurableApplicationContext.getBean(config.getName()) != null) {
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
	public ResponseBean<Boolean> deleteDataSource(@RequestBody DataSourceConfig config) {
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

	@GetMapping("/code/generate")
	@RequiresPermissions("/code/generateSimpleCode")
	public void generateSimpleCode(HttpServletResponse resp, @RequestBody TaskUnit unit) {
		// TODO
		GenerateTask task = new MySQLGenerateTask(unit);
		unit.setTable(unit.getTable().setJavaName(NameConverter.convertNormal2CamelCaseName(unit.getTable().getJdbcName(), false)));
		String generatePath = globalProperties.getCodeGenerateDirPath() + File.separator
				+ UUID.randomUUID().toString().replace("-", "");
		String codeTemplateDirPath=globalProperties.getCodeTemplateDirPath();
		FileUtil.mkdir(generatePath);
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
			ServletOutputStream out = resp.getOutputStream();
			FileInputStream inputStream = new FileInputStream(codeFile);
			int bitVal = 0;
			byte[] buffer = new byte[1024];
			while (bitVal != -1) {
				bitVal = inputStream.read(buffer);
				out.write(buffer, 0, bitVal);
			}
			inputStream.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("generateSimpleCode Exception:{}", e);
		} 
	}
	
	@PostMapping("/project/generate")
	@RequiresPermissions("/code/generateProject")
	public void generateProject(HttpServletResponse resp, @RequestBody TaskUnit unit) {
		// TODO
	}

	private boolean testConnection(DataSourceConfig config) {
		Connection conn = null;
		boolean isFinished = false;
		try {
			Class.forName(config.getDriverClassName());
			conn = DriverManager.getConnection(config.getJdbcUrl(), config.getUserName(), config.getPassword());
			// TODO 后边改为通过classDriver选择测试SQL
			ResultSet resultset = conn.createStatement().executeQuery("SELECT 1");
			while (resultset.next()) {
				int result = resultset.getInt(1);
				if (result == 1) {
					isFinished = true;
				}
			}
		} catch (Exception e) {
			log.error("Test Connection: JDBC-URL---{};USER-NAME:{};PASSWORD:{}", config.getJdbcUrl(),
					config.getUserName(), config.getPassword());
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				log.error("Other Exception:{}", e);
			}
		}
		return isFinished;
	}
}
