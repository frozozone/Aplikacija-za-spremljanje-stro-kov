package com.example.backend.entitete;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//iskanje priliva za uporabnika in mesec, Optional je prazen, ce priliv se ni vnesen
public interface PrilivBaza extends JpaRepository<Priliv, Long> {
    Optional<Priliv> findByUporabnikIdAndLetoAndMesec(Long uporabnikId, int leto, int mesec);
}