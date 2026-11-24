package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entitete.Priliv;
import com.example.backend.storitve.PrilivZahteva;
import com.example.backend.storitve.StoritevPriliv;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//sprejme zahtevek in ga preda stotrivi
@RestController
@Tag(name = "Priliv", description = "Vnos in prikaz mesečnega priliva")
public class KontrolerPriliv {
    private final StoritevPriliv storitevPriliv;

    public KontrolerPriliv(StoritevPriliv storitevPriliv) {
        this.storitevPriliv = storitevPriliv;
    }

    @GetMapping("/priliv")
    @Operation(summary = "Prikaži priliv za tekoči mesec")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Priliv najden"),
        @ApiResponse(responseCode = "404", description = "Priliv za ta mesec še ni vnesen")
    })
    public Priliv prikaziPriliv() {
        return storitevPriliv.naloziZaTekociMesec();
    }

    @PutMapping("/priliv")
    @Operation(summary ="Shrani priliv za tekoči mesec", description = "Če priliv za tekoči mesec že obstaja, ga posodobi")
    @ApiResponses(value = {
        @ApiResponse (responseCode = "200", description = "Priliv shranjen"),
        @ApiResponse (responseCode = "400", description = "Priliv mora biti večji od 0")
    })
    public Priliv shraniPriliv(@RequestBody @Valid PrilivZahteva zahteva) {
        return storitevPriliv.shrani(zahteva.znesek());
    }
}
