package com.example.Token.exceptions;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException{
	private final HttpStatus httpStatus;
	public AppException(String message, HttpStatus httpStrtus) {
		super(message);
		this.httpStatus= httpStrtus;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
