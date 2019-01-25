package com.tisson.demo.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

/**  
* @Title: CodeGenerateUtil.java  
* @Package com.tisson.demo.common.util  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2019年1月18日  
* @version V1.0  
*/
@Slf4j
public class CodeGenerateUtil {
	public static void deepCopyDir(File src,File dest){
		File[] files = src.listFiles();//获取文件夹下面的所有文件
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {//文件
				File file = new File(dest + File.separator + files[i].getName());
				try(FileInputStream fis = new FileInputStream(files[i]);
						FileOutputStream out = new FileOutputStream(file);){
                    int count = fis.read();//从源文件中读取单个字节数据
                    if(count >= 0){
                        out.write(count);//将字节数据些人目标文件中
                    }
                } catch (Exception e) {
                    log.error("error in copy File:{}",files[i].getAbsolutePath());
                }
			}else if (files[i].isDirectory()) {//文件夹
                File dir = new File(dest.getPath() + File.separator + files[i].getName());
                dir.mkdir();//创建目标文件夹
                deepCopyDir(files[i], dir);
            }
		}
	}
	
	public static List<File> fileList(File parentDir){
		List<File> fileList=new ArrayList<File>();
		File[] files = parentDir.listFiles();//获取文件夹下面的所有文件
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {//文件
				fileList.add(files[i]);
			}else if (files[i].isDirectory()) {//文件夹
				List<File> childFileList = fileList(files[i]);
				fileList.addAll(childFileList);
            }
		}
		return fileList;
	}
	
	
	public static String relativeFilePath(File parentDir,File childFile) {
		if(!FileUtil.isDirectory(parentDir)) {
			return null;
		}
		String parentDirPath=parentDir.getAbsolutePath();
		String childFilePath=childFile.getAbsolutePath();
		return childFilePath.replace(parentDirPath+File.separator, "");
	}
}
