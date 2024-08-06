package com.example.Token.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
	
	@PostMapping("/Upload")
	public ResponseEntity<String> uploadFile(@RequestParam MultipartFile  file){
		 
		if(file.isEmpty()) {
          return new ResponseEntity<>("nessun file", HttpStatus.BAD_REQUEST)	;		
		}
		if(!file.getContentType().equals("application/pdf")) {
			return new ResponseEntity<>("accetto solo pdf",HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity("File arrivato con sccesso",HttpStatus.OK);
	}

}
