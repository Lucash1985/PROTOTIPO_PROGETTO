package com.example.Token.services;

import java.nio.CharBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Token.dto.CredentialsDto;
import com.example.Token.dto.SignUpDto;
import com.example.Token.dto.UserDto;
import com.example.Token.entities.Company;
import com.example.Token.entities.Role;
import com.example.Token.entities.UserEntity;
import com.example.Token.exceptions.AppException;
import com.example.Token.mappers.UserMapper;
import com.example.Token.repository.CompanyRepository;
import com.example.Token.repository.RoleRepository;
import com.example.Token.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public UserDto login(CredentialsDto credentialsDto) {
        UserEntity user = userRepository.findByLogin(credentialsDto.login())
            .orElseThrow(() -> new AppException("Unknown User", HttpStatus.NOT_FOUND));
        
        System.out.println(user.getPassword());
        System.out.println(credentialsDto.password());
        System.out.println("pass codificata : " + passwordEncoder.encode(CharBuffer.wrap(credentialsDto.password())));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            System.out.println("ok");
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid Password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
    	if (signUpDto.companyId() == null) {
            throw new IllegalArgumentException("Company ID must not be null");
        }
        Optional<UserEntity> oUser = userRepository.findByLogin(signUpDto.login());
        if (oUser.isPresent()) {
            throw new AppException("Login Already Exists", HttpStatus.BAD_REQUEST);
        }
        
        UserEntity user = userMapper.signUpToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        
        Role role = roleRepository.findByName(signUpDto.role())
            .orElseThrow(() -> new AppException("Role not found", HttpStatus.INTERNAL_SERVER_ERROR));
        user.setRoles(Collections.singletonList(role));

        List<Company> companies = companyRepository.findAllById(signUpDto.companyId());
        if (companies.size() != signUpDto.companyId().size()) {
            throw new AppException("One or more companies not found", HttpStatus.BAD_REQUEST);
        }
        
        user.setCompanies(companies);
        companies.forEach(company -> company.getUsers().add(user));
        
        // Salva le entità aggiornate
        UserEntity savedUser = userRepository.save(user);
        companyRepository.saveAll(companies);

        return userMapper.toUserDto(savedUser);
    }
}
