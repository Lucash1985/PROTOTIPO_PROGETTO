package com.example.Token.config;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.Token.dto.UserDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserDto dto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600_000); 
        return JWT.create()
                .withIssuer(dto.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decoded = verifier.verify(token);
            UserDto user = UserDto.builder()
                    .login(decoded.getIssuer())
                    .build();
            return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        } catch (JWTVerificationException e) {
           throw new RuntimeException("Token JWT non valido o scaduto", e);
        }
    }

//	public String createAdminToken(UserDto dto) {
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + 3600_000);
//        return JWT.create()
//                .withIssuer(dto.getLogin())
//                .withIssuedAt(now)
//                .withExpiresAt(validity)
//                .sign(Algorithm.HMAC512(secretKey));
//    }

//public Authentication validateAdminToken(String token) {
//    try {
//        Algorithm algorithm = Algorithm.HMAC512(secretKey);
//        JWTVerifier verifier = JWT.require(algorithm).build();
//        DecodedJWT decoded = verifier.verify(token);
//        UserDto user = UserDto.builder()
//                .login(decoded.getIssuer())
//                .build();
//        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
//    } catch (JWTVerificationException e) {
//      throw new RuntimeException("Token Admin JWT non valido o scaduto", e);
//    }
//}
    
    public String getLoginFromJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decoded = verifier.verify(token);
            return decoded.getIssuer();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token JWT non valido o scaduto", e);
        }
    }

}