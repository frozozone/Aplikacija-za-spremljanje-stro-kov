package com.example.backend.controller;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entitete.Strosek;
import com.example.backend.entitete.StrosekBaza;

@RestController //Povemo browseru da je to kontroler za obdelavo zahtevkov in da bo vrnil json objekt
public class KontrolerStrosek {
    private final StrosekBaza strosekBaza;


    public KontrolerStrosek(StrosekBaza strosekBaza) {
        this.strosekBaza = strosekBaza;
    }

    @GetMapping("/strosek") //endpoint za prikaz vshe stroškov
    public List<Strosek> prikaziStroske(){
        return strosekBaza.findAll();
    }

    @GetMapping("/strosek/{id}") //endpoint za prikaz specificnega stroska glede na id
    public Strosek prikaziStrosek(@PathVariable @NonNull Long id){
        Strosek strosek = strosekBaza.findById(id).orElse(null);
        if (strosek != null){
            return strosek; //Vrnem objekt stroška, dobim pa v json objekt ven
        }
        else {
            System.out.println("Strosek z ID=" + id + " ne obstaja.");
        }
        return null;
    }
    @PostMapping("/strosek")//Dodaj nov strosek
    public Strosek dodajStrosek(@RequestBody @NonNull Strosek strosek){
        return strosekBaza.save(strosek); //save() shrani objekt v bazo.
    }

    @PutMapping("/strosek/{id}") //Posodobimo atribut stroška ali celoten strošek deluje kot patch in put naenkrat
    public Strosek posodobiStrosek(@RequestBody Strosek novStrosek, @PathVariable @NonNull Long id){
        Strosek obstojecStrosek = prikaziStrosek(id);

        if(obstojecStrosek==null){
            throw new RuntimeException("Strošek z ID="+id+" ne obstaja");
        }
        if (novStrosek.getNaziv()!=null){
            obstojecStrosek.setNaziv(novStrosek.getNaziv());
        }
        if (novStrosek.getZnesek()!=0.0){ //Če je znesek 0.0 potem to ni strošek.
            obstojecStrosek.setZnesek(novStrosek.getZnesek());
        }
        if (novStrosek.getDatum()!=null){
            obstojecStrosek.setDatum(novStrosek.getDatum());
        }

        return strosekBaza.save(obstojecStrosek); //Vrnemo obstojec strosek, ki je bil update-an z novim.
    }

    @DeleteMapping("/strosek/{id}")
    public void izbrisiStrosek(@PathVariable @NonNull Long id) {
        strosekBaza.deleteById(id);
    }
}
