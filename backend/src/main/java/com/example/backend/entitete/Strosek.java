package com.example.backend.entitete;
import java.time.LocalDate;
import java.time.LocalTime;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "strosek")
public class Strosek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_strosek")
    private Long ID;
    
    @NotBlank
    @Schema(description = "Naziv stroška", example = "Kava")
    @Column(nullable = false)
    private String naziv;
    
    @PositiveOrZero
    @Schema(description = "Znesek stroška v evrih", example = "2.50")
    @Column(nullable = false)
    private double znesek;
    
    @Column(name = "datum")
    @Schema(description = "Datum stroška", type = "string", format = "date", example = "2026-06-07")
    private LocalDate datum;
    
    @Column(name = "cas")
    @Schema(description = "Čas stroška", type = "string", format = "time", example = "08:15:00")
    private LocalTime cas;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_kategorija", nullable = false)
    @Schema(description = "Kategorija stroška")
    private Kategorija kategorija;

    public Strosek(Long ID, String naziv, double znesek, LocalDate datum, LocalTime cas, Kategorija kategorija) {
        this.ID = ID;
        this.naziv = naziv;
        this.znesek = znesek;
        this.datum = datum;
        this.cas = cas;
        this.kategorija = kategorija;
    }

    public Strosek() {}

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getZnesek(){
        return znesek;
    }

    public void setZnesek(double znesek) {
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

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }
}