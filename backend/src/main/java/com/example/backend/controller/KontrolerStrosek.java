package com.example.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
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

import com.example.backend.entitete.Kategorija;
import com.example.backend.entitete.KategorijaBaza;
import com.example.backend.entitete.NacinPlacila;
import com.example.backend.entitete.NacinPlacilaBaza;
import com.example.backend.entitete.Strosek;
import com.example.backend.entitete.StrosekBaza;
import com.example.backend.entitete.Uporabnik;
import com.example.backend.entitete.UporabnikBaza;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Stroški", description = "CRUD operacije za spremljanje stroškov")
public class KontrolerStrosek {
    private final StrosekBaza strosekBaza;
    private final KategorijaBaza kategorijaBaza;
    private final NacinPlacilaBaza nacinPlacilaBaza;
    private final UporabnikBaza uporabnikBaza;

    //TODO: ko bo narejena prijava, nov strosek shrani prijavljenemu uporabniku namesto privzetemu
    @Value("${aplikacija.privzeti-uporabnik}")
    private String privzetiUporabnik;

    public KontrolerStrosek(
        StrosekBaza strosekBaza,
        KategorijaBaza kategorijaBaza,
        NacinPlacilaBaza nacinPlacilaBaza,
        UporabnikBaza uporabnikBaza
    ) {
        this.strosekBaza = strosekBaza;
        this.kategorijaBaza = kategorijaBaza;
        this.nacinPlacilaBaza = nacinPlacilaBaza;
        this.uporabnikBaza = uporabnikBaza;
    }

    @GetMapping("/strosek")
    @Operation(summary = "Prikaži vse stroške", description = "Vrne stran stroškov s paginacijo. Filter po glavni kategoriji vrne tudi stroške njenih podkategorij.")
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
        @RequestParam(defaultValue="0") int page, //ce manjka je 0
        @RequestParam(defaultValue="10") int size, //ce manjka je 10
        @RequestParam(required = false) Long kategorijaId
    ) {
        //najnovejsi stroski so vedno na vrhu, pri istem datumu pa nazadnje dodani
        Pageable nastavitveStrani =PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datum", "id"));

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
        @ApiResponse(responseCode = "200", description = "Strošek shranjen"),
        @ApiResponse(responseCode = "400", description = "Manjkajoči ali neveljavni podatki")
    })
    public Strosek dodajStrosek(@RequestBody @Valid @NonNull Strosek strosek){
        strosek.setId(null); //ce bi frontend poslal id, bi save() prepisal obstojec strosek
        strosek.setUporabnik(poisciPrivzetegaUporabnika());
        nastaviSifrante(strosek, strosek);
        return strosekBaza.save(strosek);
    }

    @PutMapping("/strosek/{id}")
    @Operation(summary = "Posodobi obstoječi strošek", description = "Pošlji vse podatke stroška, ne samo spremenjenih")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Strošek posodobljen"),
        @ApiResponse(responseCode = "400", description = "Manjkajoči ali neveljavni podatki"),
        @ApiResponse(responseCode = "404", description = "Strošek ne obstaja")
    })
    public Strosek posodobiStrosek(@RequestBody @Valid Strosek novStrosek, @PathVariable @NonNull @Parameter(description = "ID stroška") Long id) {
        Strosek obstojecStrosek = prikaziStrosek(id); //ce ne obstaja, ze prikaziStrosek vrne 404

        obstojecStrosek.setNaziv(novStrosek.getNaziv());
        obstojecStrosek.setZnesek(novStrosek.getZnesek());
        obstojecStrosek.setDatum(novStrosek.getDatum());
        obstojecStrosek.setCas(novStrosek.getCas());
        obstojecStrosek.setOpis(novStrosek.getOpis());
        nastaviSifrante(obstojecStrosek, novStrosek);

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

    //frontend poslje samo id kategorije in nacina placila, tu ju zamenjamo s pravima zapisoma iz baze
    private void nastaviSifrante(Strosek cilj, Strosek poslaniPodatki) {
        cilj.setKategorija(poisciKategorijo(poslaniPodatki.getKategorija().getId()));
        cilj.setNacinPlacila(poisciNacinPlacila(poslaniPodatki.getNacinPlacila().getId()));
    }

    private Kategorija poisciKategorijo(Long id) {
        return Optional.ofNullable(id) //findById ne sprejme null, zato id najprej zavijemo v Optional
            .flatMap(kategorijaBaza::findById)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Izbrana kategorija ne obstaja"));
    }

    private NacinPlacila poisciNacinPlacila(Long id) {
        return Optional.ofNullable(id)
            .flatMap(nacinPlacilaBaza::findById)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Izbrani način plačila ne obstaja"));
    }

    private Uporabnik poisciPrivzetegaUporabnika() {
        return uporabnikBaza.findByUporabniskoIme(privzetiUporabnik)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Privzeti uporabnik '" + privzetiUporabnik + "' ne obstaja v bazi (nastavitev aplikacija.privzeti-uporabnik)"));
    }
}
