package com.example.Token.repository;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Token.entities.Comune;

@Repository
public interface ComuneRepository extends JpaRepository<Comune, Long> {

	List<Comune> findByCap(String CAP);

}
