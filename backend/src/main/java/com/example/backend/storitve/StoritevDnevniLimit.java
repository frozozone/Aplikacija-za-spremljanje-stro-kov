package com.example.backend.storitve;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.entitete.Priliv;
import com.example.backend.entitete.PrilivBaza;
import com.example.backend.entitete.StrosekBaza;
import com.example.backend.entitete.Uporabnik;
import com.example.backend.entitete.UporabnikBaza;

//zbere podatke iz baze (priliv in poraba) in jih preda izracunu
//tukaj ni racunanja, da ostane izracun locen in testabilen
@Service
public class StoritevDnevniLimit {
    private final StrosekBaza strosekBaza;
    private final PrilivBaza prilivBaza;
    private final UporabnikBaza uporabnikBaza;

    //ko bo narejena prijava, uporabi prijavljenega uporabnika namesto privzetega
    @Value("${aplikacija.privzeti-uporabnik}") //uporabnisko ime iz application.properties (ana)
    private String privzetiUporabnik;

    public StoritevDnevniLimit(StrosekBaza strosekBaza, PrilivBaza prilivBaza, UporabnikBaza uporabnikBaza) {
        this.strosekBaza = strosekBaza;
        this.prilivBaza = prilivBaza;
        this.uporabnikBaza = uporabnikBaza;
    }

    //vrne dnevni limit za danes, ce priliv za tekoci mesec se ni vnesen, vrne napako 404
    public DnevniLimitOdgovor izracunajZaDanes() {
        LocalDate danes = LocalDate.now();
        Uporabnik uporabnik = poisciPrivzetegaUporabnika();
        Priliv priliv = poisciPriliv(uporabnik.getId(), danes);

        LocalDate prviDanVMesecu = danes.withDayOfMonth(1);
        //na prvega v mesecu je "do vceraj" prazno obdobje (od > do), zato je vsota 0
        BigDecimal doVceraj = vsota(uporabnik.getId(), prviDanVMesecu, danes.minusDays(1));
        BigDecimal dane = vsota(uporabnik.getId(), danes, danes);

        return IzracunDnevnegaLimita.izracunaj(priliv.getZnesek(), doVceraj, dane, danes);
    }

    private BigDecimal vsota(Long uporabnikId, LocalDate od, LocalDate doDatuma) {
        BigDecimal vsota = strosekBaza.vsotaStroskov(uporabnikId, od, doDatuma);
        return vsota == null ? BigDecimal.ZERO : vsota;
    }

    //vsota stroskov v obdobju, SUM vrne null, ce stroskov ni, zato vrnemo 0
    private Priliv poisciPriliv(Long uporabnikId, LocalDate danes) {
        return prilivBaza.findByUporabnikIdAndLetoAndMesec(uporabnikId, danes.getYear(), danes.getMonthValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Za prikaz dnevnega limita najprej vnesite mesečni priliv"));
    }

    //poisce priliv za leto in mesec danasnjega datuma, sporocilo 404 prikaze frontend na kartici
    private Uporabnik poisciPrivzetegaUporabnika() {
        return uporabnikBaza.findByUporabniskoIme(privzetiUporabnik)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Privzeti uporabnik '" + privzetiUporabnik + "' ne obstaja v bazi (nastavitev aplikacija.privzeti-uporabnik)"));
    }
}
