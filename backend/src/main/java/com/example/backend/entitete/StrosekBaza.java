package com.example.backend.entitete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StrosekBaza extends JpaRepository<Strosek, Long> {
    //vrne stroske izbrane kategorije in vseh njenih podkategorij (npr. Hrana -> tudi Trgovina in Restavracije)
    @Query("""
        SELECT s FROM Strosek s
        JOIN s.kategorija k
        LEFT JOIN k.nadrejena n
        WHERE k.id = :id OR n.id = :id
        """)
    Page<Strosek> findByKategorija(@Param("id") Long id, Pageable pageable);
}
