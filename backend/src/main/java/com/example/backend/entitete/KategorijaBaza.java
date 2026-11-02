package com.example.backend.entitete;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KategorijaBaza extends JpaRepository<Kategorija, Long> {
    List<Kategorija> findAllByOrderByNazivAsc();
}
