package com.example.backend.entitete;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NacinPlacilaBaza extends JpaRepository<NacinPlacila, Long> {
    List<NacinPlacila> findAllByOrderByNazivAsc();
}
