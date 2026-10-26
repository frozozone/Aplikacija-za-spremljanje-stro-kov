package com.example.backend.controller;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entitete.Strosek;
import com.example.backend.entitete.StrosekBaza;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "Prikaži vse stroške")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seznam vseh stroškov")
    })
    public List<Strosek> prikaziStroske(){
        return strosekBaza.findAll();
    }
    /*public Page<Strosek> prikaziStroske(
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10") int size,
        @RequestParam(defaultValue="datum, desc") String sort
    ){
        String[] deli = sort.split(",");
        String polje = deli[0].trim();

        Sort.Direction smer = Sort.Direction.DESC;
        if (deli.length > 1 && deli[1].trim().equalsIgnoreCase("asc")) {
            smer = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page,size, Sort.by(smer, polje));
        return strosekBaza.findAll(pageable);
    }*/

    @GetMapping("/strosek/{id}")
    @Operation(summary = "Prikaži strošek po ID-ju")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Strošek najden"),
        @ApiResponse(responseCode = "404", description = "Strošek ne obstaja")
    })
    public Strosek prikaziStrosek(@PathVariable @NonNull @Parameter(description = "ID stroška") Long id){
        Strosek strosek = strosekBaza.findById(id).orElse(null);
        if (strosek != null){
            return strosek;
        }
        else {
            System.out.println("Strosek z ID=" + id + " ne obstaja.");
        }
        return null;
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
    public Strosek posodobiStrosek(@RequestBody Strosek novStrosek, @PathVariable @NonNull @Parameter(description = "ID stroška") Long id){
        Strosek obstojecStrosek = prikaziStrosek(id);

        if(obstojecStrosek==null){
            throw new RuntimeException("Strošek z ID="+id+" ne obstaja");
        }
        if (novStrosek.getNaziv()!=null){
            obstojecStrosek.setNaziv(novStrosek.getNaziv());
        }
        if (novStrosek.getZnesek()!=0.0){
            obstojecStrosek.setZnesek(novStrosek.getZnesek());
        }
        if (novStrosek.getDatum()!=null){
            obstojecStrosek.setDatum(novStrosek.getDatum());
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