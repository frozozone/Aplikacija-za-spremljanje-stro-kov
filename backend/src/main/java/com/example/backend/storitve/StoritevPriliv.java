package com.example.backend.storitve;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.entitete.Priliv;
import com.example.backend.entitete.PrilivBaza;
import com.example.backend.entitete.Uporabnik;
import com.example.backend.entitete.UporabnikBaza;

/*
Ta razred ima vso logiko za priliv:
    iskanje uporabnika,
    iskanje priliva za tekoči mesec,
    ustvarjanje ali posodabljanje priliva
 */
@Service //pove springu da ustvari objekt tega razreda in ga preda kontrolerju
public class StoritevPriliv {
    private final PrilivBaza prilivBaza;
    private final UporabnikBaza uporabnikBaza;

    //ko bo narejena prijava, uporabi prijavljenega uporabnika namesto privzetega
    @Value("${aplikacija.privzeti-uporabnik}") //uporabnisko ime iz application.properties (ana)
    private String privzetiUporabnik;

    public StoritevPriliv(PrilivBaza prilivBaza, UporabnikBaza uporabnikBaza) {
        this.prilivBaza = prilivBaza;
        this.uporabnikBaza = uporabnikBaza;
    }

    //vrne priliv za tekoci mesec, ce ga ni vrne napako 404 (GET /priliv)
    public Priliv naloziZaTekociMesec() {
        LocalDate danes = LocalDate.now();
        Uporabnik uporabnik = poisciPrivzetegaUporabnika();

        return poisciPriliv(uporabnik.getId(), danes).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Priliv za ta mesec še ni vnesen"));
    }

    //shrani priliv za tekoci mesec, ce ze obstaja ga posodobi, drugace ustvari novega (PUT /priliv)
    public Priliv shrani(BigDecimal znesek) {
        LocalDate danes = LocalDate.now();
        Uporabnik uporabnik = poisciPrivzetegaUporabnika();

        Priliv priliv = poisciPriliv(uporabnik.getId(), danes).orElseGet(() -> novPriliv(uporabnik, danes));
        priliv.setZnesek(znesek);
        return prilivBaza.save(priliv);
    }

    //poisce priliv za leto in mesec danasnjega datuma, Optional je prazen, ce priliv se ni vnesen
    private Optional<Priliv> poisciPriliv(Long uporabnikId, LocalDate danes) {
        return prilivBaza.findByUporabnikIdAndLetoAndMesec(uporabnikId, danes.getYear(), danes.getMonthValue());
    }

    //nov priliv za tekoci mesec brez zneska, znesek nastavi metoda shrani
    private Priliv novPriliv(Uporabnik uporabnik, LocalDate danes) {
        Priliv priliv = new Priliv();
        priliv.setUporabnik(uporabnik);
        priliv.setLeto(danes.getYear());
        priliv.setMesec(danes.getMonthValue());

        return priliv;
    }

    //poisce privzetega uporabnika (ana), napaka 500 ce ga ni v bazi
    private Uporabnik poisciPrivzetegaUporabnika() {
        return uporabnikBaza.findByUporabniskoIme(privzetiUporabnik)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Privzeti uporabnik '" + privzetiUporabnik + "' ne obstaja v bazi (nastavitev aplikacija.privzeti-uporabnik)"));
    }
}
