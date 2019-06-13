package com.tisson.demo.common.base;

public enum FileType {
	WAV("wav",1),MAP3("mp3",2),WMA("wma",3),APE("ape",4),AAC("aac",5),FLAC("flac",6),
	JPG("jpg",7),JPEG("jpeg",8),PNG("png",9),ZIP("zip",10);
	
	private String name;
	private Integer code;
	FileType(String name,Integer code) {
		this.name=name;
		this.code=code;
	}
	public String getName() {
		return name;
	}
	public Integer getCode() {
		return code;
	}
}
