package com.tisson.demo.controller.sys;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotEmpty;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.base.ResultCode;
import com.tisson.demo.configuration.GlobalProperties;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/process")
@RequiresPermissions("/process")
@Api("/process")
public class SysProcessController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SysProcessController.class);
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private GlobalProperties globalProperties;
	
	@PostMapping(value = "/processList")
	@ApiOperation(value = "获取部署流程列表信息", httpMethod = "GET", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "query", value = "查询项", required = true, dataType = "ListQuery")})
	public ResponseBean<List<Map<String,String>>> processList(
			@RequestBody ListQuery<Map<String,String>> query){
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		List<Deployment> deploymentList = repositoryService.createDeploymentQuery()
				.listPage(query.getPage()*query.getLimit(), (query.getPage()+1)*query.getLimit());
		for(Deployment deployment:deploymentList) {
			ProcessDefinition processDef = repositoryService.createProcessDefinitionQuery()
					.deploymentId(deployment.getId()).singleResult();
			String processDefName = deployment.getName().split("-")[0];
			HashMap<String,String> item = new HashMap<String,String>();
			item.put("deployId", deployment.getId());
			item.put("processName", deployment.getName());
			item.put("processResourcePath", globalProperties.getProcessPreviewDirPath()+File.separator
					+ processDefName+File.separator+deployment.getName()+".bpmn");
			item.put("version", String.valueOf(processDef.getVersion()));
			result.add(item);
		}
		return new ResponseBean<List<Map<String,String>>>("获取部署流程列表信息成功", result);
	}
	
	
	@PostMapping(value = "/resources")
	@ApiOperation(value = "上传部署流程压缩包(zip)", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "上传文件(zip)", required = true, dataType = "MultipartFile")})
	public ResponseBean<String> uploadProcessResource(@RequestParam("file")@Nonnull MultipartFile file) throws Exception{
		String fileName = file.getOriginalFilename().replace(".zip", "");
		String savePath = globalProperties.getProcessPreviewDirPath()+File.separator+fileName;
		if(FileUtil.exist(savePath+"-1.zip")) {//上传但是没部署时处理,文件将按照上传压缩文件-数字的格式保存,默认保存为上传压缩文件-1
			Pattern pattern=Pattern.compile(fileName+"-\\d");
			Integer maxIndex=0;
			List<String> subFileNames = FileUtil.listFileNames(globalProperties.getProcessPreviewDirPath());
			for(String subFileName:subFileNames) {
				Matcher matcher=pattern.matcher(subFileName);
				if(matcher.find()) {
					Integer index=Integer.valueOf(subFileName.replace(".zip", "").replace(fileName+"-", ""));
					if(index>maxIndex) {
						maxIndex=index;
					}
				}
			}
			maxIndex++;
			savePath+="-"+maxIndex+".zip";
		}else {
			savePath+="-1.zip";
		}
		file.transferTo(FileUtil.touch(savePath));
		return new ResponseBean<String>("上传文件成功", Base64.encode(savePath));
	}
	
	@PostMapping(value = "/deploy")
	@RequiresPermissions("/process/deploy")
	@ApiOperation(value = "部署流程压缩包(zip)", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "processResourcePath", value = "加密文件资源路径(base64加密)", required = true, dataType = "String")})
	public ResponseBean<String> deployProcess(@RequestParam("processResourcePath") @NotEmpty String processResourcePath) throws Exception{
		String resourcePath=Base64.decodeStr(processResourcePath);
		File zipFile = FileUtil.file(resourcePath);
		String fileName = zipFile.getName();
		Pattern pattern=Pattern.compile("\\S+-\\d");
		Matcher matcher = pattern.matcher(fileName);
		if(matcher.find()) {
			String deployName = fileName.split("-")[0];
			String saveResourceDirPath=globalProperties.getProcessPreviewDirPath()+File.separator+deployName;
			Integer maxIndex=0;
			String zipPath=globalProperties.getProcessPreviewDirPath()+File.separator+"temp"+File.separator+
					UUID.randomUUID().toString().replace("-", "")+File.separator+fileName;
			File tempZipFile = FileUtil.file(zipPath);
			if(!FileUtil.exist(tempZipFile.getParentFile())) {
				tempZipFile.getParentFile().mkdirs();
				tempZipFile.createNewFile();
				FileUtil.copy(zipFile, tempZipFile, true);
				ZipUtil.unzip(zipPath,tempZipFile.getParentFile().getAbsolutePath());
			}
			if(FileUtil.exist(saveResourceDirPath)) {//已经有不同版本的bpmn文件
				pattern=Pattern.compile(deployName+"-\\d.bpmn");
				List<String> subFileNames = FileUtil.listFileNames(saveResourceDirPath);
				for(String subFileName:subFileNames) {
					matcher=pattern.matcher(subFileName);
					if(matcher.find()) {
						Integer index=Integer.valueOf(subFileName.replace(".bpmn", "").replace(deployName+"-", ""));
						if(index>maxIndex) {
							maxIndex=index;
						}
					}
				}
				maxIndex++;
				File bmpnFile = FileUtil.touch(saveResourceDirPath+File.separator+deployName+"-"+maxIndex+".bpmn");
				File pngFile = FileUtil.touch(saveResourceDirPath+File.separator+deployName+"-"+maxIndex+".png");
				subFileNames=FileUtil.listFileNames(tempZipFile.getParentFile().getAbsolutePath());
				for(String subFileName:subFileNames) {
					if(subFileName.endsWith("bpmn")) {
						FileUtil.copy(FileUtil.file(tempZipFile.getParentFile().getAbsolutePath()+File.separator+subFileName),
								bmpnFile, true);
					}
					if(subFileName.endsWith("png")) {
						FileUtil.copy(FileUtil.file(tempZipFile.getParentFile().getAbsolutePath()+File.separator+subFileName),
								pngFile, true);
					}
				}
				FileUtil.del(tempZipFile.getParentFile().getAbsolutePath());
			}else {
				maxIndex++;
				FileUtil.mkdir(saveResourceDirPath);
				File bmpnFile = FileUtil.touch(saveResourceDirPath+File.separator+deployName+"-1.bpmn");
				File pngFile = FileUtil.touch(saveResourceDirPath+File.separator+deployName+"-1.png");
				List<String> subFileNames=FileUtil.listFileNames(tempZipFile.getParentFile().getAbsolutePath());
				for(String subFileName:subFileNames) {
					if(subFileName.endsWith("bpmn")) {
						FileUtil.copy(FileUtil.file(tempZipFile.getParentFile().getAbsolutePath()+File.separator+subFileName),
								bmpnFile, true);
					}
					if(subFileName.endsWith("png")) {
						FileUtil.copy(FileUtil.file(tempZipFile.getParentFile().getAbsolutePath()+File.separator+subFileName),
								pngFile, true);
					}
				}
				FileUtil.del(tempZipFile.getParentFile().getAbsolutePath());
			}
			try(ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));){
				repositoryService.createDeployment().name(deployName+"-"+maxIndex)
					.addZipInputStream(in).deploy();
			}catch (Exception e) {
				LOGGER.error("error in deployProcess:",e);
				return new ResponseBean<String>(ResultCode.PROCESS_RESOURCE_FILE_ERROR.getCode(),
						ResultCode.PROCESS_RESOURCE_FILE_ERROR.getDesc(),null);
			}
			zipFile.delete();
			return new ResponseBean<String>("部署成功","部署资源名称为"+fileName);
		}
		return new ResponseBean<String>(ResultCode.PROCESS_RESOURCE_FILE_ERROR.getCode(),
				ResultCode.PROCESS_RESOURCE_FILE_ERROR.getDesc(),null);
	}
	
	@PostMapping(value = "/preview")
	@ApiOperation(value = "预览流程图", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "processName", value = "部署资源名称", required = true, dataType = "String")})
	public ResponseBean<String> previewProcessImage(@RequestParam("processName")@NotEmpty String processName){
		String resourceName=processName.split("-")[0];
		String imageFilePath=globalProperties.getProcessPreviewDirPath()+File.separator+resourceName
				+File.separator+processName+".png";
		File imageFile = FileUtil.file(imageFilePath);
		if(imageFile.exists()) {
			return new ResponseBean<String>("获取流程图成功", Base64.encode(imageFile));
		}
		return new ResponseBean<String>("获取流程图失败","");
	}
	
	@PostMapping(value = "/undeploy")
	@RequiresPermissions("/process/undeploy")
	@ApiOperation(value = "取消部署流程", httpMethod = "POST", response = ResponseBean.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "deploymentId", value = "部署ID", required = true, dataType = "String")})
	public ResponseBean<String> undeployProcess(String deploymentId){
		List<HistoricProcessInstance> hpiList = historyService.createHistoricProcessInstanceQuery()
				.deploymentId(deploymentId).list();
		for(HistoricProcessInstance hpi:hpiList) {
			String processInstanceId=hpi.getId();
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
					.singleResult();
			if(pi==null){//该流程实例已经完成了
				historyService.deleteHistoricProcessInstance(processInstanceId);
			}else {//该流程实例未结束的
				runtimeService.deleteProcessInstance(processInstanceId,"undeploy by user");
				historyService.deleteHistoricProcessInstance(processInstanceId);
			}
		}
		repositoryService.deleteDeployment(deploymentId);
		return new ResponseBean<String>("取消部署流程", "取消部署流程成功");
	}
}
