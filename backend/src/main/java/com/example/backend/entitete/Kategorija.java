package com.example.backend.entitete;

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
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "kategorija")
public class Kategorija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kategorija")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Naziv kategorije", example = "Trgovina")
    @Column(nullable = false)
    private String naziv;

    //null pomeni, da je to glavna kategorija, drugace je podkategorija
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_nadrejena")
    @Schema(description = "Nadrejena kategorija (null pri glavnih kategorijah)")
    private Kategorija nadrejena;

    public Kategorija(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public Kategorija() {} //prazen konstruktor rabi JPA

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

    public Kategorija getNadrejena() {
        return nadrejena;
    }

    public void setNadrejena(Kategorija nadrejena) {
        this.nadrejena = nadrejena;
    }
}
