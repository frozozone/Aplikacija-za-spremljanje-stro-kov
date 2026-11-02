package com.example.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entitete.Kategorija;
import com.example.backend.entitete.KategorijaBaza;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/kategorija")
@Tag(name = "Kategorije", description = "Kategorije stroškov (so vnaprej vpisane v bazi)")
public class KontrolerKategorija {
    private final KategorijaBaza kategorijaBaza;

    public KontrolerKategorija(KategorijaBaza kategorijaBaza){
        this.kategorijaBaza = kategorijaBaza;
    }

    @GetMapping
    @Operation(summary = "Prikaži vse kategorije")
    public List<Kategorija> prikaziKategorije(){
        return kategorijaBaza.findAllByOrderByNazivAsc(); //vrne kategorije po abecedi
    }
}