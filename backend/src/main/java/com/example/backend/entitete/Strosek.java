package com.example.backend.entitete;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "strosek")
public class Strosek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_strosek")
    private Long id;

    @NotBlank(message = "Naziv stroška je obvezen")
    @Size(max = 100, message = "Naziv je lahko dolg največ 100 znakov")
    @Schema(description = "Naziv stroška", example = "Kava")
    @Column(nullable = false)
    private String naziv;

    @NotNull(message = "Znesek je obvezen")
    @Positive(message = "Znesek mora biti večji od 0")
    @Digits(integer = 8, fraction = 2, message = "Znesek ima lahko največ 2 decimalki")
    @Schema(description = "Znesek stroška v evrih", example = "2.50")
    @Column(nullable = false)
    private BigDecimal znesek;

    @NotNull(message = "Datum je obvezen")
    @Schema(description = "Datum stroška", type = "string", format = "date", example = "2026-06-07")
    @Column(nullable = false)
    private LocalDate datum;

    @Schema(description = "Čas stroška", type = "string", format = "time", example = "08:15:00")
    private LocalTime cas;

    @Size(max = 500, message = "Opis je lahko dolg največ 500 znakov")
    @Schema(description = "Daljši opis stroška (ni obvezen)", example = "Kava s sošolci po predavanjih")
    private String opis;

    @NotNull(message = "Kategorija je obvezna")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_kategorija", nullable = false)
    @Schema(description = "Kategorija stroška, dovolj je poslati id")
    private Kategorija kategorija;

    @NotNull(message = "Način plačila je obvezen")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_nacin_placila", nullable = false)
    @Schema(description = "Način plačila, dovolj je poslati id")
    private NacinPlacila nacinPlacila;

    //lastnika nastavi backend, zato ga frontend ne posilja in ne dobi nazaj
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uporabnik", nullable = false)
    private Uporabnik uporabnik;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(insertable = false, updatable = false)
    private LocalDateTime ustvarjeno;

    public Strosek() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public BigDecimal getZnesek(){
        return znesek;
    }

    public void setZnesek(BigDecimal znesek) {
        this.znesek = znesek;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public LocalTime getCas() {
        return cas;
    }

    public void setCas(LocalTime cas) {
        this.cas = cas;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    public NacinPlacila getNacinPlacila() {
        return nacinPlacila;
    }

    public void setNacinPlacila(NacinPlacila nacinPlacila) {
        this.nacinPlacila = nacinPlacila;
    }

    public Uporabnik getUporabnik() {
        return uporabnik;
    }

    public void setUporabnik(Uporabnik uporabnik) {
        this.uporabnik = uporabnik;
    }

    public LocalDateTime getUstvarjeno() {
        return ustvarjeno;
    }
}
