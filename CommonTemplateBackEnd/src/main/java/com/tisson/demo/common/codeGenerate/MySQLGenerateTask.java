package com.tisson.demo.common.codeGenerate;
/**  
* @Title: MySQLGenerateTask.java  
* @Package com.tisson.demo.common.codeGenerate  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年12月3日  
* @version V1.0  
*/

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import com.tisson.demo.common.util.NameConverter;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.template.Engine;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.engine.thymeleaf.ThymeleafEngine;

public class MySQLGenerateTask extends GenerateTask {
	
	public MySQLGenerateTask(TaskUnit unit) {
		super(unit);
	}

	@Override
	public File generate() throws Exception {
		/**
		 * @Title: generate @Description: TODO(这里用一句话描述这个方法的作用) @return 返回类型 @throws
		 */
		// TODO
		MySQLMetaData metaData = new MySQLMetaData(this.getConn());
		List<Column> columnList = new ArrayList<Column>();
		List<String> primaryKeyNames = new ArrayList<String>();

		List<Map<String, String>> primaryKeysInfo = metaData.getTableKeys(this.unit.getTable().getJdbcName());
		for (Map<String, String> item : primaryKeysInfo) {
			String columsValue = item.get("columns");
			String[] columnNames = columsValue.split(",");
			for (String str : columnNames) {
				primaryKeyNames.add(str);
			}
		}
		List<Map<String, String>> columnsInfo = metaData.getColumns(this.unit.getTable().getJdbcName());

		for (Map<String, String> item : columnsInfo) {
			Column column = new Column().setComment(item.get("COLUMN_COMMENT")).setJdbcName(item.get("COLUMN_NAME"))
					.setJavaName(NameConverter.convertNormal2CamelCaseName(item.get("COLUMN_NAME"), true))
					.setJdbcType(item.get("DATA_TYPE"))
					.setNotNull(!NameConverter.checkColumnIsNullable(item.get("IS_NULLABLE")))
					.setJavaType(NameConverter.converJdbcType2JavaType(item.get("DATA_TYPE").toUpperCase()));
			if (column.getJavaType().toLowerCase().equals("boolean")) {
				column = column
						.setGetterName("is" + NameConverter.convertNormal2CamelCaseName(column.getJavaName(), false));
			} else {
				column = column
						.setGetterName("get" + NameConverter.convertNormal2CamelCaseName(column.getJavaName(), false));
			}
			column = column
					.setSetterName("set" + NameConverter.convertNormal2CamelCaseName(column.getJavaName(), false));

			if (primaryKeyNames.contains(item.get("COLUMN_NAME"))) {
				column = column.setPrimaryKey(true);
			}
			columnList.add(column);
		}
		this.unit.getTable().setColumns(columnList);
		generateEntity(this.unit);
		generateMapper(this.unit);
		generateService(this.unit);
		generateController(this.unit);
		FileUtil.touch(this.getGenerateDirPath()+".zip");
		File codeZip=ZipUtil.zip(this.getGenerateDirPath(), this.getGenerateDirPath()+".zip");
		FileUtil.del(this.getGenerateDirPath());
		return codeZip;
	}

	private void generateEntity(TaskUnit unit) throws Exception {
		String generateEntityFilePath = this.getGenerateDirPath() + File.separator
				+unit.getPackageName().replace(".", File.separator)+File.separator
				+"entity"+File.separator+ unit.getTable().getJavaName() + ".java";
		Engine engine = new ThymeleafEngine(initTemplateEngine());
		FileUtil.touch(generateEntityFilePath);
		Template template = engine.getTemplate(this.getTemplateDirPath()+File.separator+"entity.vm");
		try(BufferedWriter writer = FileUtil.getWriter(generateEntityFilePath, "UTF-8", false);){
			template.render(Dict.create().set("unit", unit).set("table", unit.getTable()).set("generateDate",
					new DateTime().toString("yyyy/MM/dd")), writer);
			writer.flush();
		}
	}

	private void generateMapper(TaskUnit unit) throws Exception {
		String generateEntityFilePath = this.getGenerateDirPath() + File.separator 
				+unit.getPackageName().replace(".", File.separator)+File.separator
				+"mapper"+File.separator+ unit.getTable().getJavaName() + "Mapper.java";
		Engine engine = new ThymeleafEngine(initTemplateEngine());
		FileUtil.touch(generateEntityFilePath);
		Template template = engine.getTemplate(this.getTemplateDirPath()+File.separator+"mapper.vm");
		try(BufferedWriter writer = FileUtil.getWriter(generateEntityFilePath, "UTF-8", false);){
			template.render(Dict.create().set("unit", unit).set("table", unit.getTable()).set("generateDate",
					new DateTime().toString("yyyy/MM/dd")), writer);
			writer.flush();
		}
	}

	private void generateService(TaskUnit unit) throws Exception {
		String generateEntityFilePath = this.getGenerateDirPath() + File.separator 
				+unit.getPackageName().replace(".", File.separator)+File.separator
				+"service"+File.separator+ unit.getTable().getJavaName() + "Service.java";
		Engine engine = new ThymeleafEngine(initTemplateEngine());
		FileUtil.touch(generateEntityFilePath);
		Template template = engine.getTemplate(this.getTemplateDirPath()+File.separator+"service.vm");
		try(BufferedWriter writer = FileUtil.getWriter(generateEntityFilePath, "UTF-8", false);){
			template.render(Dict.create().set("unit", unit).set("table", unit.getTable()).set("generateDate",
					new DateTime().toString("yyyy/MM/dd")), writer);
			writer.flush();
		}
	}

	private void generateController(TaskUnit unit) throws Exception {
		String generateEntityFilePath = this.getGenerateDirPath() + File.separator 
				+unit.getPackageName().replace(".", File.separator)+File.separator
				+"controller"+File.separator+ unit.getTable().getJavaName() + "Controller.java";
		Engine engine = new ThymeleafEngine(initTemplateEngine());
		FileUtil.touch(generateEntityFilePath);
		Template template = engine.getTemplate(this.getTemplateDirPath()+File.separator+"controller.vm");
		try(BufferedWriter writer = FileUtil.getWriter(generateEntityFilePath, "UTF-8", false);){
			template.render(Dict.create().set("unit", unit).set("table", unit.getTable()).set("generateDate",
					new DateTime().toString("yyyy/MM/dd")), writer);
			writer.flush();
		}
	}
	
	private TemplateEngine initTemplateEngine() {
		TemplateEngine templateEngine = new TemplateEngine();
		FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding("UTF8");
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
	}
}
