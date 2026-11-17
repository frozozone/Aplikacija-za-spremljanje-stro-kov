package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.storitve.DnevniLimitOdgovor;
import com.example.backend.storitve.StoritevDnevniLimit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

//sprejme zahtevek in ga preda storitvi, tukaj ni logike
//napake (npr. 404) se pretvorijo v {"message": "..."} v ObravnavaNapak.java
@RestController
@Tag(name = "Dnevni limit", description = "Priporočeni dnevni limit porabe glede na mesečni priliv")
public class KontrolerDnevniLimit {
    private final StoritevDnevniLimit storitevDnevniLimit;

    public KontrolerDnevniLimit(StoritevDnevniLimit storitevDnevniLimit) {
        this.storitevDnevniLimit = storitevDnevniLimit;
    }

    //vedno za danes in za privzetega uporabnika, zato endpoint nima parametrov
    @GetMapping("/dnevni-limit")
    @Operation(summary = "Prikaži dnevni limit za danes",
            description = "dnevniLimit = (priliv - porabljenoDoVceraj) / preostaliDnevi, zaokroženo navzdol na 2 decimalki")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dnevni limit izračunan"),
            @ApiResponse(responseCode = "404", description = "Priliv za tekoči mesec še ni vnesen")
    })
    public DnevniLimitOdgovor prikaziDnevniLimit() {
        return storitevDnevniLimit.izracunajZaDanes();
    }
}