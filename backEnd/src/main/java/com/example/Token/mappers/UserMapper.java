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
        
        // Mappa i ruoli dell'utente
        List<String> roleNames = userEntity.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());
        
        // Ottieni l'ID dell'azienda associata all'utente
        Long companyId = (userEntity.getCompany() != null) ? userEntity.getCompany().getId() : null;

        return UserDto.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .roles(roleNames) // Assicurati che UserDto abbia un campo per i ruoli
                .token(null) // Supponendo che il token non sia impostato qui, potrebbe essere impostato in un altro servizio
                .companyId(companyId) // Aggiungi l'ID dell'azienda
                .build();
    }

    public UserEntity signUpToUser(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        }

        // Costruisci l'entità UserEntity senza l'azienda e i ruoli
        return UserEntity.builder()
                .login(signUpDto.login())
                .password(null) // La password sarà impostata nel servizio
                .roles(List.of()) // Imposta un elenco vuoto di ruoli
                .company(null) // L'azienda sarà impostata nel servizio
                .build();
    }
}
