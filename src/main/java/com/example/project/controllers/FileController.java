package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.project.models.UserApplication;
import com.example.project.message.ResponseFile;
import com.example.project.services.FileStorageService;

@Controller
@CrossOrigin("http://localhost:8080")
public class FileController {
	@Autowired
	private FileStorageService storageService;

	/*----------------------------------------------------------------------------
	Process uploaded files, correct file type and file size < 20 MB combined
	----------------------------------------------------------------------------*/
	@GetMapping("/files")
	public ResponseEntity<List<ResponseFile>> getListFiles() {
		List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(dbFile.getId()).toUriString();
			return new ResponseFile(dbFile.getCvFileName(), fileDownloadUri, dbFile.getCvFileType(),
					dbFile.getCvFileData().length);
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	/*----------------------------------------------------------------------------
	Upload PDF files - POST
	----------------------------------------------------------------------------*/
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			String idLong = (String) session.getAttribute("appId");
			storageService.store(file, file2, idLong);
			redirectAttributes.addFlashAttribute("success", "File uploaded successfully");
			return "redirect:/account";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "File size can not be > 2 MB");
			return "upload.jsp";
		}
	}

	/*----------------------------------------------------------------------------
	Get uploaded user CV
	----------------------------------------------------------------------------*/
	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		UserApplication fileDB = storageService.getFile(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getCvFileName() + "\"")
				.body(fileDB.getCvFileData());
	}

	/*----------------------------------------------------------------------------
	Get uploaded user CERTIFICATE
	----------------------------------------------------------------------------*/
	@GetMapping("/files/cer/{id}")
	public ResponseEntity<byte[]> getCerFile(@PathVariable String id) {
		UserApplication fileDB = storageService.getFile(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getCertFileName() + "\"")
				.body(fileDB.getCertFileData());
	}

	/*----------------------------------------------------------------------------
	Edit uploaded files - PUT
	----------------------------------------------------------------------------*/
	@PostMapping("/editfiles")
	public String editfiles(@RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			String idLong = (String) session.getAttribute("appId");
			storageService.store(file, file2, idLong);
			redirectAttributes.addFlashAttribute("success", "File uploaded successfully");
			return "redirect:/account";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "File size can not be > 2 MB");
			return "editUpload.jsp";
		}
	}

}