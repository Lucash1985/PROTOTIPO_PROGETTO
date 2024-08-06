package com.example.Token.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.Token.dto.SignUpDto;
import com.example.Token.dto.UserDto;
import com.example.Token.entities.Company;
import com.example.Token.entities.Role;
import com.example.Token.entities.UserEntity;

@Component
public class UserMapper {

    public UserDto toUserDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        
        // Mappa anche i ruoli dell'utente
        List<String> roleNames = userEntity.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());
        
        // Mappa anche le aziende dell'utente
        List<Long> companyIds = userEntity.getCompanies().stream()
            .map(Company::getId)
            .collect(Collectors.toList());

        return UserDto.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .roles(roleNames) // Assicurati che UserDto abbia un campo per i ruoli
                .token(null) // Assuming token is not set here, you might want to set it in a different service
                .companyIds(companyIds) // Aggiungi gli ID delle aziende
                .build();
    }

    public UserEntity signUpToUser(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        }

        // Costruisci l'entità UserEntity senza aziende e ruoli
        return UserEntity.builder()
                .login(signUpDto.login())
                .password(null) // La password sarà impostata nel servizio
                .roles(List.of()) // Imposta un elenco vuoto di ruoli
                .companies(List.of()) // Imposta un elenco vuoto di aziende (se necessario)
                .build();
    }
}
