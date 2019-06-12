package com.tisson.demo.common.codeGenerate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import com.tisson.demo.common.util.CodeGenerateUtil;
import com.tisson.demo.common.util.NameConverter;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.template.Engine;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.engine.thymeleaf.ThymeleafEngine;

/**  
* @Title: GenerateProjectTask.java  
* @Package com.tisson.demo.common.codeGenerate  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2019年1月18日  
* @version V1.0  
*/
public class ProjectTask extends GenerateTask{

	public ProjectTask(TaskUnit unit) {
		super(unit);
	}

	private void generateApplication(String srcCodeDir) throws Exception {
		String packageName=this.unit.getProjectPackageName();
		Integer index=packageName.lastIndexOf(".")+1;
		String appName=NameConverter.convertNormal2CamelCaseName(packageName.substring(index), false);
		String applicationFilePath = srcCodeDir + File.separator+packageName.replace(".", File.separator)
		+File.separator+ appName+"Application.java";
		String applicationTemplateFilePath=this.getTemplateDirPath()+File.separator
				+"codeFile"+File.separator+"Application.vm";
		Engine engine = new ThymeleafEngine(initTemplateEngine());
		Template template = engine.getTemplate(applicationTemplateFilePath);
		try(BufferedWriter writer = FileUtil.getWriter(applicationFilePath, "UTF-8", false);){
			template.render(Dict.create().set("unit", unit).set("appName",appName), writer);
			writer.flush();
		}
	}
	
	private void generateProjectFrontEnd(String resourceDirPath) {
		String frontEndTemplateZipFilePath=this.getTemplateDirPath()+File.separator+"frontResource"
				+File.separator+"frontEnd-src.zip";
		String frontEndTargetZipFilePath=this.getGenerateDirPath()+File.separator+"frontEnd-src.zip";
		FileUtil.copy(frontEndTemplateZipFilePath, frontEndTargetZipFilePath, true);
	}

	
	private void generateProjectOthFile(String resourceDirPath) throws Exception {
		String packageName=this.unit.getProjectPackageName();
		Integer index=packageName.lastIndexOf(".")+1;
		String appName=NameConverter.convertNormal2CamelCaseName(packageName.substring(index), false);
		String pomTemplateFilePath=this.getTemplateDirPath()+File.separator+"resourceFile"+File.separator+"pom.vm";
		String pomFilePath=this.generateDirPath+File.separator+"pom.xml";
		
		Engine engine = new ThymeleafEngine(initTemplateEngine());
		Template template = engine.getTemplate(pomTemplateFilePath);
		try(BufferedWriter writer = FileUtil.getWriter(pomFilePath, "UTF-8", false);){
			template.render(Dict.create().set("unit", unit).set("appName",appName), writer);
			writer.flush();
		}
		
		String logbackConfigTemplateFilePath=this.getTemplateDirPath()+File.separator+"resourceFile"+File.separator+"logback-spring.xml";
		String logbackConfigFilePath=resourceDirPath+File.separator+"logback-spring.xml";
		FileUtil.copy(logbackConfigTemplateFilePath, logbackConfigFilePath, true);
		
		String globalPropTemplateFilePath=this.getTemplateDirPath()+File.separator+"resourceFile"+File.separator+"props"
				+File.separator+"global.yml";
		String globalPropFilePath=resourceDirPath+File.separator+"props"+File.separator+"global.yml";
		
		FileUtil.copy(globalPropTemplateFilePath, globalPropFilePath, true);
	}
	
	private void generateProjectCode(String srcCodeDir) throws Exception {
		String projectCodeDirPath=this.getTemplateDirPath()+File.separator+"codeFile"+File.separator+"projectCode";
		File[] codeTemplateFileList=FileUtil.file(projectCodeDirPath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				String fileName=file.getName();
				if(fileName.endsWith("vm")) {
					return true;
				}
				return false;
			}
		});
		Engine engine = new ThymeleafEngine(initTemplateEngine());
		for(File codeTemplateFile:codeTemplateFileList) {
			Template template = engine.getTemplate(codeTemplateFile.getAbsolutePath());
			String fileName=codeTemplateFile.getName();
			Integer index=fileName.lastIndexOf(".")+1;
			fileName=fileName.substring(0, index)+"java";
			String generateFilePath=this.generateDirPath+File.separator+fileName;
			try(BufferedWriter writer = FileUtil.getWriter(generateFilePath, "UTF-8", false);){
				template.render(Dict.create().set("unit", unit), writer);
				writer.flush();
			}
			String codePackageStr = null;
			try(BufferedReader reader = FileUtil.getReader(generateFilePath, "UTF-8");){
				codePackageStr= reader.readLine().replace(";", "").replace("package", "").trim().replace(".", File.separator);
			}
			if(codePackageStr!=null) {
				FileUtil.move( FileUtil.file(generateFilePath), 
						FileUtil.file(srcCodeDir+File.separator+codePackageStr+File.separator+fileName), true);
			}
		}
	}
	
	private void generateResource(String resourceDirPath) throws Exception {
		String packageName=this.unit.getProjectPackageName();
		Integer index=packageName.lastIndexOf(".")+1;
		String appName=NameConverter.convertNormal2CamelCaseName(packageName.substring(index), true);
		// 生成application.yml文件
		Engine engine = new ThymeleafEngine(initTemplateEngine());
		String applicationYmlTemplateFile=this.getTemplateDirPath()+File.separator+
				"resourceFile"+File.separator+"application.vm";
		Template template = engine.getTemplate(applicationYmlTemplateFile);
		String applicationYmlFile = resourceDirPath+File.separator+"application.yml";
		try(BufferedWriter writer = FileUtil.getWriter(applicationYmlFile, "UTF-8", false);){
			template.render(Dict.create().set("unit", unit).set("appName",appName), writer);
			writer.flush();
		}
		//复制 templates
		String templatesTemplateDirPath=this.getTemplateDirPath()+File.separator+
				"resourceFile"+File.separator+"templates";
		String templatesDirPath = resourceDirPath+File.separator+"templates";
		FileUtil.mkdir(templatesDirPath);
		CodeGenerateUtil.deepCopyDir(FileUtil.file(templatesTemplateDirPath), FileUtil.file(templatesDirPath));
	
		//复制 templates
		String staticTemplateDirPath=this.getTemplateDirPath()+File.separator+
				"resourceFile"+File.separator+"static";
		String staticDirPath = resourceDirPath+File.separator+"static";
		FileUtil.mkdir(staticDirPath);
		CodeGenerateUtil.deepCopyDir(FileUtil.file(staticTemplateDirPath), FileUtil.file(staticDirPath));
		
		//复制 props
		String propsTemplateDirPath=this.getTemplateDirPath()+File.separator+
				"resourceFile"+File.separator+"props";
		String propsDirPath = resourceDirPath+File.separator+"props";
		FileUtil.mkdir(propsDirPath);
		CodeGenerateUtil.deepCopyDir(FileUtil.file(propsTemplateDirPath), FileUtil.file(propsDirPath));
		
		//复制 mapper
		String mapperTemplateDirPath=this.getTemplateDirPath()+File.separator+
				"resourceFile"+File.separator+"mapper";
		String mapperDirPath=resourceDirPath+File.separator+"mapper";
		List<File> mapperTemplateFileList=CodeGenerateUtil.fileList(FileUtil.file(mapperTemplateDirPath));
		
		for(File mapperTemplateFile:mapperTemplateFileList) {
			if(!mapperTemplateFile.getName().endsWith("vm")) {
				continue;
			}
			String relativeFilePath=CodeGenerateUtil.relativeFilePath(FileUtil.file(mapperTemplateDirPath), 
					mapperTemplateFile);
			Integer idx=relativeFilePath.lastIndexOf(".")+1;
			relativeFilePath=relativeFilePath.substring(0, idx)+"xml";
			template = engine.getTemplate(mapperTemplateFile.getAbsolutePath());
			String mapperTemplatePath = mapperDirPath+File.separator+relativeFilePath;
			try(BufferedWriter writer = FileUtil.getWriter(mapperTemplatePath, "UTF-8", false);){
				template.render(Dict.create().set("unit", unit).set("appName",appName), writer);
				writer.flush();
			}
		}
	}
	
	@Override
	public File generate() throws Exception {
		String srcCodeDir=this.getGenerateDirPath()+File.separator+"src"+File.separator
				+"main"+File.separator+"java";
		String srcResourceDir=this.getGenerateDirPath()+File.separator+"src"+File.separator
				+"main"+File.separator+"resources";
		if(FileUtil.exist(srcCodeDir)) {
			FileUtil.del(srcCodeDir);
		}
		FileUtil.mkdir(srcCodeDir);
		if(FileUtil.exist(srcResourceDir)) {
			FileUtil.del(srcResourceDir);
		}
		FileUtil.mkdir(srcResourceDir);
		generateApplication(srcCodeDir);
		generateResource(srcResourceDir);
		generateProjectCode(srcCodeDir);
		generateProjectFrontEnd(srcCodeDir);
		generateProjectOthFile(srcResourceDir);
		FileUtil.touch(this.getGenerateDirPath()+".zip");
		File codeZip=ZipUtil.zip(this.getGenerateDirPath(), this.getGenerateDirPath()+".zip");
		FileUtil.del(this.getGenerateDirPath());
		return codeZip;
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
