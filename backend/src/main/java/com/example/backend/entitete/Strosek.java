package com.example.backend.entitete;
import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "strosek")
public class Strosek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_strosek")
    private Long ID;
    
    @Schema(description = "Naziv stroška", example = "Kava")
    private String naziv;
    @Schema(description = "Znesek stroška v evrih", example = "2.50")
    private double znesek;
    @Column(name = "datum")
    @Schema(description = "Datum stroška", type = "string", format = "date", example = "2026-06-07")
    private LocalDate datum;
    @Column(name = "cas")
    @Schema(description = "Čas stroška", type = "string", format = "time", example = "08:15:00")
    private LocalTime cas;

    //Parametriziran konstruktor
    public Strosek (Long ID, String naziv, double znesek, LocalDate datum, LocalTime cas) {
        this.ID=ID;
        this.naziv=naziv;
        this.znesek=znesek;
        this.datum=datum;
        this.cas=cas;
    }

    //Neparametriziran konstruktor
    public Strosek(){}

    //Getterji
    public Long getID(){
        return ID;
    }

    public String getNaziv(){
        return naziv;
    }

    public double getZnesek(){
        return znesek;
    }

    public LocalDate getDatum(){
        return datum;
    }

    public LocalTime getCas(){
        return cas;
    }

    //Setterji
    public void setID(Long ID){
        this.ID=ID;
    }

    public void setNaziv(String naziv){
        this.naziv=naziv;
    }

    public void setZnesek(double znesek){
        this.znesek=znesek;
    }

    public void setDatum(LocalDate datum){
        this.datum=datum;
    }
    public void setCas(LocalTime cas){
        this.cas=cas;
    }

    
}
