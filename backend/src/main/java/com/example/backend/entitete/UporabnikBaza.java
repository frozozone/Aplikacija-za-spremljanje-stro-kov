package com.example.backend.entitete;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UporabnikBaza extends JpaRepository<Uporabnik, Long> {
    Optional<Uporabnik> findByUporabniskoIme(String uporabniskoIme);
}
