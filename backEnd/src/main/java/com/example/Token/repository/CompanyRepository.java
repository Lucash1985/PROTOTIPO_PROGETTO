package com.example.Token.repository;

import com.example.Token.entities.Company;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	 Optional<Company> findByName(String name);
}