package com.file.FileManager.dto;

import javax.annotation.ManagedBean;

@ManagedBean
public class FileDetailsEntity {


	private long id;
	

	private String fileName;
	

	private String filePath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public FileDetailsEntity() {
		
	}
	
	private String destPath;
	
	private byte[] fileData;

	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public FileDetailsEntity(long id, String fileName, String filePath) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.filePath = filePath;
	}
	
	
	
}
