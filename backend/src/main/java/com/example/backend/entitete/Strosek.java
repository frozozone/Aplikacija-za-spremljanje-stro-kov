package com.example.backend.entitete;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Strosek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@NonNull 
    private Long ID;
    
    private String naziv;
    private double znesek;
    private LocalDate datum;

    //Parametriziran konstruktor
    public Strosek (Long ID, String naziv, double znesek, LocalDate datum) {
        this.ID=ID;
        this.naziv=naziv;
        this.znesek=znesek;
        this.datum=datum;
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

    
}
