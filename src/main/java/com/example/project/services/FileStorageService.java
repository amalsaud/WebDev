package com.example.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.example.project.models.UserApplication;
import com.example.project.repositories.FileDBRepository;

@Service
public class FileStorageService {
	@Autowired
	private FileDBRepository fileDBRepository;
	
	@Autowired
	private UserApplicationService uerApplicationService;

	public UserApplication store(MultipartFile file,MultipartFile file2,String appId) throws IOException {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String fileName2 = StringUtils.cleanPath(file2.getOriginalFilename());
		// fetch current application
		UserApplication currApplication = uerApplicationService.findById(appId);
		currApplication.setCertFileName(fileName);
		currApplication.setCertFileType(file.getContentType());
		currApplication.setCertFileData(file.getBytes());
		
		currApplication.setCvFileName(fileName2);
		currApplication.setCvFileType(file2.getContentType());
		currApplication.setCvFileData(file2.getBytes());
	
		return fileDBRepository.save(currApplication);
	}

	public UserApplication getFile(String id) {
		return fileDBRepository.findById(id).get();
	}

	public Stream<UserApplication> getAllFiles() {
		return fileDBRepository.findAll().stream();
	}
}
