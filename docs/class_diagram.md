# Razredni diagram

![Razredni diagram](Razredni-diagram/Class_Diagram.svg)

#### Še ni implementirano
- `StoritevStatistika`, `KontrolerStatistika` in `StatistikaKategorije` (PU6)
- `StoritevOpozorilo` in `OpozoriloOdgovor` (PU8)
- `StoritevKategorija` (PU10)

## Opis razredov:
Aplikacija je slojevita: kontroler, storitev, repozitorij, entiteta.

### Entitete:
- `Uporabnik`: uporabnik aplikacije (uporabniško ime, e-pošta, geslo, ime, priimek, vloga). Je lastnik stroškov in priliva. Dokler ni prijave, se uporablja privzeti uporabnik.
- `Vloga`: Vloga uporabnika, vsak uporabnik ima eno.
- `Kategorija`: Kategorija stroška. Ima lahko nadrejeno kategorijo(0..1), kar omogoča podkategorije.
- `NacinPlacila`: Šifrant načinov plačila (gotovina, kartica...)
- `Strosek`: En vnos porabe: naziv, znesek, datum, čas, opis, kategorija, način plačila, uporabnik.
- `Priliv`: Mesečni priliv uporabnika (leto, mesec, znesek), osnova za izračun dnevnega limita.

### Repozitoriji:
Vsi razširjajo JpaRepository, ki že vsebuje osnovne operacije (findAll, findById, save, deleteById), zato so v diagramu prikazane le dodatne metode.
- `UporabnikBaza`:
- `StrosekBaza`:
- `KategorijaBaza`, `NacinPlacilaBaza`:
- `PrilivBaza`:
  
### Storitve:
- `StoritevDnevniLimit`:
- `IzracunDnevnegaLimita`:
- `StoritevPriliv`:
- `StoritevOpozorilo`:
- `StoritevKategorija`:
- `StoritevStatistika`:
  
### Kontrolerji:
- `KontrolerStrosek`:
- `KontrolerPriliv`:
- `KontrolerDnevniLimit`:
- `KontrolerKategorija`, `KontrolerNacinPlacila`:
- `KontrolerStatistika`:
- `ObravnavaNapak`:

### record
- `DnevniLimitOdgovor`:
- `PrilivZahteva`:
- `OpozoriloOdgovor`:
- `StatistikaKategorije`:

## Ključne metode in potek funkcionalnosti:
