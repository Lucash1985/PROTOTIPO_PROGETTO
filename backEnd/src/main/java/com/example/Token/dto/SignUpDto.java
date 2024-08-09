package com.example.Token.dto;

public record SignUpDto(String login, char[] password, String role, Long companyId) {
}