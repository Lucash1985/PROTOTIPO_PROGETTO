package com.example.Token.services;

import com.example.Token.entities.Comune;
import com.example.Token.exceptions.AppException;
import com.example.Token.repository.ComuneRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ComuneService {

    private final ComuneRepository comuneRepository;

    @Autowired
    public ComuneService(ComuneRepository comuneRepository) {
        this.comuneRepository = comuneRepository;
    }

}

