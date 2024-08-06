package com.example.Token.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Token.dto.ErrorDto;
import com.example.Token.exceptions.AppException;

@ControllerAdvice
public class RestExceptionHandler {
@ExceptionHandler(value = { AppException.class })
@ResponseBody
public ResponseEntity<ErrorDto> handleException(AppException ex){
	return ResponseEntity.status(ex.getHttpStatus())
			.body(new ErrorDto(ex.getMessage()));
}
}
