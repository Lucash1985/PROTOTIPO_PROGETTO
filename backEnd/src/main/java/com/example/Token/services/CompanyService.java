package com.example.Token.services;

import com.example.Token.entities.Company;
import com.example.Token.repository.CompanyRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService {

    
    private  CompanyRepository companyRepository;
    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        System.out.println("CompanyRepository: " + this.companyRepository);
    }
    public  List<Company> findAllCompanies() {
    	 List<Company> companies = companyRepository.findAll();
         // Stampa su console il contenuto di companies
         System.out.println("Companies found: " + companies);
         return companies;
    }
}
