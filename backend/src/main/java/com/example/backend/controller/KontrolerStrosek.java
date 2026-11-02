package com.example.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.entitete.Strosek;
import com.example.backend.entitete.StrosekBaza;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Stroški", description = "CRUD operacije za spremljanje stroškov")
public class KontrolerStrosek {
    private final StrosekBaza strosekBaza;

    public KontrolerStrosek(StrosekBaza strosekBaza) {
        this.strosekBaza = strosekBaza;
    }

    @GetMapping("/strosek")
    @Operation(summary = "Prikaži vse stroške", description = "Vrne stran stroškov s paginacijo")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stran s stroški",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class)
            )
        )
    })
    public Page<Strosek> prikaziStroske(
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10") int size,
        @RequestParam(required = false) Long kategorijaId
    ) {
        Pageable nastavitveStrani =PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datum")); //najnovejsi stroski so vedno na vrhu

        if (kategorijaId == null){ //brez kategorije vrnemo vse stroske
            return strosekBaza.findAll(nastavitveStrani);
        }
        return strosekBaza.findByKategorija(kategorijaId, nastavitveStrani);
    }

    @GetMapping("/strosek/{id}")
    @Operation(summary = "Prikaži strošek po ID-ju")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Strošek najden"),
        @ApiResponse(responseCode = "404", description = "Strošek ne obstaja")
    })
    public Strosek prikaziStrosek(@PathVariable @NonNull @Parameter(description = "ID stroška") Long id){
        return strosekBaza.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Strosek ne obstaja")); //ce ga ni vrnemo napako 404
    }
    
    @PostMapping("/strosek")
    @Operation(summary = "Dodaj nov strošek")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Strošek shranjen")
    })
    public Strosek dodajStrosek(@RequestBody @NonNull Strosek strosek){
        return strosekBaza.save(strosek);
    }

    
    @PutMapping("/strosek/{id}")
    @Operation(summary = "Posodobi obstoječi strošek")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Strošek posodobljen"),
        @ApiResponse(responseCode = "404", description = "Strošek ne obstaja")
    })
    public Strosek posodobiStrosek(@RequestBody Strosek novStrosek, @PathVariable @NonNull @Parameter(description = "ID stroška") Long id) {
        Strosek obstojecStrosek = prikaziStrosek(id); //ce ne obstaja, ze prikaziStrosek vrne 404

        if (novStrosek.getNaziv() != null) {
            obstojecStrosek.setNaziv(novStrosek.getNaziv());
        }
        if (novStrosek.getZnesek() != 0.0){ //0.0 pomeni, da znesek ni bil poslan
            obstojecStrosek.setZnesek(novStrosek.getZnesek());
        }
        if (novStrosek.getDatum() != null) {
            obstojecStrosek.setDatum(novStrosek.getDatum());
        }
        if (novStrosek.getCas() != null) {
            obstojecStrosek.setCas(novStrosek.getCas());
        }
        if (novStrosek.getKategorija() != null) {
            obstojecStrosek.setKategorija(novStrosek.getKategorija());
        }

        return strosekBaza.save(obstojecStrosek);
    }

    @DeleteMapping("/strosek/{id}")
    @Operation(summary = "Izbriši strošek")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Strošek izbrisan")
    })
    public void izbrisiStrosek(@PathVariable @NonNull @Parameter(description = "ID stroška") Long id) {
        strosekBaza.deleteById(id);
    }
}
