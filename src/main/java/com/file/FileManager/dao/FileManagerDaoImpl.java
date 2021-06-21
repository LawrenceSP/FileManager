package com.file.FileManager.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.file.FileManager.dto.FileDetailsEntity;

@Repository
public class FileManagerDaoImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void saveFile(FileDetailsEntity fileDetails) {
		jdbcTemplate.update("insert into file_details(id, file_name, file_path) values((select max(id) from file_details)+1, ?, ?)", new Object[] {fileDetails.getFileName(), fileDetails.getFilePath()});
	}
	
	public void updateFilePath(String existingPath, String newPath) {
		String updateFilePath = "update file_details set file_path=? where file_path=?";
		jdbcTemplate.update(updateFilePath, new Object[] {newPath, existingPath});
	}
	
	public void updateFileName(String fileName, Long id) {
		String updateFilePath = "update file_details set file_name=? where id=?";
		jdbcTemplate.update(updateFilePath, new Object[] {id});
	}
	
	public void deleteFile(Long id) {
		String updateFilePath = "delete from file_details where id=?";
		jdbcTemplate.update(updateFilePath, new Object[] {id});
	}
	
	public List<FileDetailsEntity> getAllFileDetails() {
		List<FileDetailsEntity> fileDetailsEntityList = jdbcTemplate.query(String.format("select id,file_name, file_path from file_details"), 
			      (rs, rowNum) -> new FileDetailsEntity(rs.getInt("id"),rs.getString("file_name"), rs.getString("file_path")));
		return fileDetailsEntityList;
		
	}
	@SuppressWarnings("deprecation")
	public List<FileDetailsEntity> getFileDetails(Long id) {
    return jdbcTemplate.query(String.format("select id,file_name, file_path from file_details"), new Object[]{id}, (rs, rowNum) ->
            new FileDetailsEntity(rs.getInt("id"),rs.getString("file_name"), rs.getString("file_path")));
	}
}
