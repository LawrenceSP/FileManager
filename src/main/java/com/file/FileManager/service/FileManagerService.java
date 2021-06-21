package com.file.FileManager.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.file.FileManager.dao.FileManagerDaoImpl;
import com.file.FileManager.dto.FileDetailsEntity;

@Service
public class FileManagerService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FileManagerDaoImpl fileManagerDaoImpl;

	/**
	 *  This method to save the file in File system and make entry of meta-data into file_details table
	 * @param fileDetails
	 */
	public void saveFile(FileDetailsEntity fileDetails) {
		try {
			Path path = Paths.get(fileDetails.getDestPath() + "/" + fileDetails.getFileName());
			Files.write(path, fileDetails.getFileData());
			fileManagerDaoImpl.saveFile(fileDetails);
		} catch (FileNotFoundException e) {
			logger.error("Error while saving file " + e.getMessage());
		} catch (IOException e) {
			logger.error("FileNotFound " + e.getMessage());
		}
	}

	/**
	 *  Get the FileDetails from the table for the input id
	 * @param id
	 * @return
	 */
	public FileDetailsEntity getFileDetails(Long id) {
		return fileManagerDaoImpl.getFileDetails(id).get(0);
	}

	/**
	 * To move the file file from one path to another and also update the filepath into the table
	 * @param existingPath
	 * @param newPath
	 * @return
	 */
	public boolean moveFile(String existingPath, String newPath) {
		boolean isFileMoved = false;

		File fileToMove = new File(existingPath);
		File targetFile = new File(newPath);

		try {
			if (targetFile.canWrite()) {
				com.google.common.io.Files.move(fileToMove, targetFile);
				isFileMoved = true;
				fileManagerDaoImpl.updateFilePath(existingPath, newPath);
			}
		} catch (IOException e1) {
			logger.error("IOException in moveFile " + e1.getMessage());
		}
		return isFileMoved;
	}

	/**
	 * Rename the file and update the newly updated name into table
	 * @param filePath
	 * @param fileName
	 * @param id
	 * @return
	 * @throws FileSystemException
	 */
	public boolean renameFile(String fileName, Long id) throws FileSystemException {
		FileDetailsEntity fileDetailsEntity = fileManagerDaoImpl.getFileDetails(id).get(0);
		File filePathDB = new File(fileDetailsEntity.getFilePath());
		boolean isRenamed = filePathDB.renameTo(new File(fileName));
		fileManagerDaoImpl.updateFileName(fileName, id);
		if (!isRenamed) {
			throw new FileSystemException(fileName);
		}
		return isRenamed;

	}

	/**
	 * Get all the File details from the table 
	 * (This needs to be cautious when we have huge data in the table)
	 * @return
	 */
	public List<FileDetailsEntity> getAllFileDetails() {
		return fileManagerDaoImpl.getAllFileDetails();
	}

	/**
	 *  Delete the file from file system and also delete the record from table for the deleted file
	 * @param filePath
	 * @param id
	 * @return
	 */
	public boolean deleteFile(@NonNull String filePath, Long id) {
		fileManagerDaoImpl.deleteFile(id);
		File file = new File(filePath);
		return file.delete();
	}
}
