package com.example.backend.entitete;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "uporabnik")
public class Uporabnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uporabnik")
    private Long id;

    @Column(name = "uporabnisko_ime", nullable = false, unique = true)
    private String uporabniskoIme;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore //hash gesla nikoli ne posljemo na frontend
    @Column(name = "geslo_hash", nullable = false)
    private String gesloHash;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String priimek;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vloga", nullable = false)
    private Vloga vloga;

    //cas ustvarjanja nastavi baza sama (DEFAULT CURRENT_TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private LocalDateTime ustvarjen;

    public Uporabnik() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUporabniskoIme() {
        return uporabniskoIme;
    }

    public void setUporabniskoIme(String uporabniskoIme) {
        this.uporabniskoIme = uporabniskoIme;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGesloHash() {
        return gesloHash;
    }

    public void setGesloHash(String gesloHash) {
        this.gesloHash = gesloHash;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public Vloga getVloga() {
        return vloga;
    }

    public void setVloga(Vloga vloga) {
        this.vloga = vloga;
    }

    public LocalDateTime getUstvarjen() {
        return ustvarjen;
    }
}
