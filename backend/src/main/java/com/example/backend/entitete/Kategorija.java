package com.example.backend.entitete;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "kategorija")
public class Kategorija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kategorija")
    private Long ID;

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Naziv kategorije", example = "Hrana in pijača")
    @Column(nullable = false)
    private String naziv;

    public Kategorija(Long ID, String naziv) {
        this.ID = ID;
        this.naziv = naziv;
    }

    public Kategorija() {} //prazen konstruktor rabi JPA

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
}