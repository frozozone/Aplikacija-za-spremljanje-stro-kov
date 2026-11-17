package com.example.backend.entitete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface StrosekBaza extends JpaRepository<Strosek, Long> {
    //vrne stroske izbrane kategorije in vseh njenih podkategorij (npr. Hrana -> tudi Trgovina in Restavracije)
    @Query("""
        SELECT s FROM Strosek s
        JOIN s.kategorija k
        LEFT JOIN k.nadrejena n
        WHERE k.id = :id OR n.id = :id
        """)
    Page<Strosek> findByKategorija(@Param("id") Long id, Pageable pageable);
    //vsota stroskov uporabnika med dvema datumoma (vkljucno), vrne null, ce stroskov ni
    @Query("""
        SELECT SUM(s.znesek) FROM Strosek s
        WHERE s.uporabnik.id = :uporabnikId AND s.datum BETWEEN :od AND :do
        """)
    BigDecimal vsotaStroskov(@Param("uporabnikId") Long uporabnikId, @Param("od") LocalDate od, @Param("do") LocalDate doDatuma);
}