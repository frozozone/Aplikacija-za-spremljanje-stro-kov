package com.example.backend.entitete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StrosekBaza extends JpaRepository<Strosek, Long> {
    //V vmesnik bom dodal metode za specifične poizvedbe, če bo potrebno
    //Mogoče find by naziv, datum, znesek itd

    @Query("SELECT s FROM Strosek s WHERE s.kategorija.ID = :id")
    Page<Strosek> findByKategorija(@Param("id") Long id, Pageable pageable); //vrne samo stroske izbrane kategorije
}