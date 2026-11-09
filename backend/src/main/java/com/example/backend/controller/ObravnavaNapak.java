package com.example.backend.controller;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

//napake iz vseh kontrolerjev pretvori v odgovor oblike {"message": "..."}, ki ga frontend lahko prikaze
@RestControllerAdvice
public class ObravnavaNapak {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> obravnavajNapakoStatusa(ResponseStatusException napaka) {
        return ustvariOdgovor(HttpStatus.valueOf(napaka.getStatusCode().value()), napaka.getReason());
    }

    //@Valid ni uspel, vrnemo sporocilo prvega neveljavnega polja
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> obravnavajNeveljavnePodatke(MethodArgumentNotValidException napaka) {
        String sporocilo = napaka.getBindingResult().getFieldErrors().stream()
            .map(polje -> polje.getDefaultMessage())
            .findFirst()
            .orElse("Podatki niso veljavni");
        return ustvariOdgovor(HttpStatus.BAD_REQUEST, sporocilo);
    }

    //baza je zavrnila podatke, npr. kategorija ali nacin placila s tem id-jem ne obstaja
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> obravnavajKrsitevBaze(DataIntegrityViolationException napaka) {
        return ustvariOdgovor(HttpStatus.BAD_REQUEST, "Podatki niso veljavni (npr. izbrana kategorija ali način plačila ne obstaja)");
    }

    private ResponseEntity<Map<String, String>> ustvariOdgovor(HttpStatus status, String sporocilo) {
        return ResponseEntity.status(status).body(Map.of("message", sporocilo == null ? status.getReasonPhrase() : sporocilo));
    }
}
