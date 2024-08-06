package com.example.Token.dto;

import java.util.List;

public record SignUpDto(String login, char[] password, String role,List<Long> companyId) {

}
