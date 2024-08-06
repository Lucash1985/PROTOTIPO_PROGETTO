package com.example.Token.controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Token.config.UserAuthProvider;
import com.example.Token.entities.Destinatario;
import com.example.Token.services.ComuneService;
import com.example.Token.services.CustomUserDetailsService;
import com.opencsv.exceptions.CsvException;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
public class MessagesController {
	
	private final CustomUserDetailsService customUserDetailsService;
	private final UserAuthProvider userAuthProvider;
	 @GetMapping("/messages")
	    public ResponseEntity<ArrayList<String>> messages(@RequestHeader("Authorization") String token) throws IOException, CsvException {
	     ArrayList<String> roleList = new ArrayList<String>();  
		 String login = userAuthProvider.getLoginFromJwt(token.replace("Bearer ", ""));
	        UserDetails userDetails = customUserDetailsService.loadUserByUsername(login);
	        userDetails.getAuthorities().forEach(authority -> 
           roleList.add(authority.getAuthority()));
	        return ResponseEntity.ok(roleList);
	    }
	 }
