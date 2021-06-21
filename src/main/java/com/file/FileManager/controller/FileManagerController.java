/**
 * 
 */
package com.file.FileManager.controller;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.FileManager.dto.FileDetailsEntity;
import com.file.FileManager.dto.FileDetailsResponse;
import com.file.FileManager.service.FileManagerService;

@RestController
@RequestMapping("/filemanager")
public class FileManagerController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FileManagerService fileManagerService;

	@PostMapping(value="/save-file")
	public FileDetailsResponse saveFile(@RequestBody FileDetailsEntity fileDetails) {
		FileDetailsResponse fileDetailsResponse = new FileDetailsResponse();
		
		if(fileDetails==null) {
			return prepareFileDetailsResponse(fileDetailsResponse, false, "Invalid input or Null", 203);
		}
		logger.info("Input is valid ");
		fileDetails.setFileData(parseFileToByteArray(fileDetails.getFilePath()+"/"+fileDetails.getFileName()));
		fileManagerService.saveFile(fileDetails);
		return prepareFileDetailsResponse(fileDetailsResponse, true, "File Saved Successfully ", 200);
		
	}

	
	@PutMapping(value="/rename-file")
	public FileDetailsResponse renameFile(@RequestBody FileDetailsEntity fileDetails) {
		FileDetailsResponse fileDetailsResponse = new FileDetailsResponse();
		if(null==fileDetails) {
			return prepareFileDetailsResponse(fileDetailsResponse, false, "Invalid input or Null", 203);
		}
		try {
			if(fileManagerService.renameFile(fileDetails.getFileName(), fileDetails.getId())) {
				prepareFileDetailsResponse(fileDetailsResponse, true, "Renamed Successfully", 200);
			}else {
				prepareFileDetailsResponse(fileDetailsResponse, true, "File Rename Operation Failed", 202);
			}
		} catch (FileSystemException e) {
			prepareFileDetailsResponse(fileDetailsResponse, false, "FileSystemException", 501);
		}
		return fileDetailsResponse;
		
	}
	
	@PutMapping(value="/move-file")
	public FileDetailsResponse moveFile(@RequestBody FileDetailsEntity fileDetails) {
		FileDetailsResponse fileDetailsResponse = new FileDetailsResponse();
		if(null==fileDetails) {
			return prepareFileDetailsResponse(fileDetailsResponse, false, "Invalid input or Null", 203);
		}
		if(fileManagerService.moveFile(fileDetails.getFilePath(), fileDetails.getDestPath())) {
			prepareFileDetailsResponse(fileDetailsResponse, true, "File Moved Successfully", 200);
		}else {
			prepareFileDetailsResponse(fileDetailsResponse, true, "File Move Operation Failed", 202);
		}
		return fileDetailsResponse;
		
	}
	
	@GetMapping(value="/get-all-filedetails}")
	public List<FileDetailsEntity> getAllFileDetails(){
		return fileManagerService.getAllFileDetails();
	}
	
	@GetMapping(value="/get-file/{fileId}")
	public FileDetailsResponse getFile(@PathVariable Long fileId) {
		FileDetailsResponse fileDetailsResponse = new FileDetailsResponse();
		if(fileId==null) {
			return prepareFileDetailsResponse(fileDetailsResponse, false, "Invalid input or Null", 203);
		}
		FileDetailsEntity fileDetails = fileManagerService.getFileDetails(fileId);
		fileDetailsResponse.setFileDetailsBean(fileDetails);
		return prepareFileDetailsResponse(fileDetailsResponse, true, "File Details Retrieved Successfully", 200);
	}
	
	@DeleteMapping("/delete-file/{fileId}")
	public FileDetailsResponse deleteFile(@PathVariable Long fileId) {
		
		FileDetailsResponse fileDetailsResponse = new FileDetailsResponse();
		boolean isFileDeleted = false;
		if(fileId==null) {
			prepareFileDetailsResponse(fileDetailsResponse, false, "Invalid Input or Null", 201);;
		}
		FileDetailsEntity fileDetails = fileManagerService.getFileDetails(fileId);
		if(fileDetails.getFilePath() !=null && fileDetails.getFileName()!=null) {
			isFileDeleted = fileManagerService.deleteFile(fileDetails.getFilePath()+"/"+fileDetails.getFileName(), fileId);
		}
		if(isFileDeleted)
			return prepareFileDetailsResponse(fileDetailsResponse, true, "Deleted File Successfully", 200);
		else
			return prepareFileDetailsResponse(fileDetailsResponse, true, "File Deletion Failed", 201);
		
	}
	
	private FileDetailsResponse prepareFileDetailsResponse(FileDetailsResponse fileDetailsResponse, boolean isSuccess, String message, int responseCode){
		fileDetailsResponse.setSuccess(isSuccess);
		fileDetailsResponse.setMessage(message);
		fileDetailsResponse.setResponseCode(responseCode);
		return fileDetailsResponse;
		
	}
	
	public byte[] parseFileToByteArray(String filepath) {
		byte[] byteArr = null;
		try {

			byteArr = Files.readAllBytes(Paths.get(filepath));

		} catch (IOException e) {
			logger.error("Error Parsing the file::" + e.getMessage());
		}
		return byteArr;
	}
}
