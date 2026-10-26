package com.example.backend.entitete;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StrosekBaza extends JpaRepository<Strosek, Long>{
    //V vmesnik bom dodal metode za specifične poizvedbe, če bo potrebno. JPARepository ima metode za delo z bazo.
    //Mogoče find by naziv, datum, znesek itd
    List<Strosek> findAllByOrderByDatumDesc();
}
