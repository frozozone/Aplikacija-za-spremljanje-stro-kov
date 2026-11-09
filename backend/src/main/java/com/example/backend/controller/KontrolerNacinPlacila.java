package com.example.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entitete.NacinPlacila;
import com.example.backend.entitete.NacinPlacilaBaza;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/nacin-placila")
@Tag(name = "Načini plačila", description = "Načini plačila (so vnaprej vpisani v bazi)")
public class KontrolerNacinPlacila {
    private final NacinPlacilaBaza nacinPlacilaBaza;

    public KontrolerNacinPlacila(NacinPlacilaBaza nacinPlacilaBaza) {
        this.nacinPlacilaBaza = nacinPlacilaBaza;
    }

    @GetMapping
    @Operation(summary = "Prikaži vse načine plačila")
    public List<NacinPlacila> prikaziNacinePlacila() {
        return nacinPlacilaBaza.findAllByOrderByNazivAsc();
    }
}
