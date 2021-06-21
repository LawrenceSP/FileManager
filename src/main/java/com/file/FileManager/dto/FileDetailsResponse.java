package com.file.FileManager.dto;

public class FileDetailsResponse {

	private FileDetailsEntity fileDetails;
	private String message;
	private int responseCode;
	private boolean isSuccess;
	
	
	public FileDetailsResponse(FileDetailsEntity fileDetails, String message, int responseCode, boolean isSuccess) {
		super();
		this.fileDetails = fileDetails;
		this.message = message;
		this.responseCode = responseCode;
		this.isSuccess = isSuccess;
	}
	
	public FileDetailsResponse() {
		super();
	}

	public FileDetailsEntity getFileDetailsBean() {
		return fileDetails;
	}
	public void setFileDetailsBean(FileDetailsEntity fileDetails) {
		this.fileDetails = fileDetails;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	@Override
	public String toString() {
		return "FileDetailsResponse [fileDetailsBean=" + fileDetails + ", message=" + message + ", responseCode="
				+ responseCode + ", isSuccess=" + isSuccess + "]";
	}
}
