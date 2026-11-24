package com.example.backend.entitete;

import java.math.BigDecimal;

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

//mesecni priliv uporabnika, en zapis na uporabnika, leto in mesec (UNIQUE v bazi)
@Entity
@Table(name = "priliv")
public class Priliv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_priliv")
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tk_uporabnik", nullable = false)
    private Uporabnik uporabnik;

    @Column(nullable = false)
    private int leto;
    @Column(nullable = false)
    private int mesec;
    @Column(nullable = false)
    private BigDecimal znesek;

    public Priliv() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Uporabnik getUporabnik() { return uporabnik; }
    public void setUporabnik(Uporabnik uporabnik) { this.uporabnik = uporabnik; }

    public int getLeto() { return leto; }
    public void setLeto(int leto) { this.leto = leto; }

    public int getMesec() { return mesec; }
    public void setMesec(int mesec) { this.mesec = mesec; }

    public BigDecimal getZnesek() { return znesek; }
    public void setZnesek(BigDecimal znesek) { this.znesek = znesek; }
}