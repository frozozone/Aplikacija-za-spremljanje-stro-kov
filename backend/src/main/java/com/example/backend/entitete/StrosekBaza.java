package com.example.backend.entitete;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StrosekBaza extends JpaRepository<Strosek, Long>{
    //V vmesnik bom dodal metode za specifične poizvedbe, če bo potrebno. JPARepository ima metode za delo z bazo.
}
