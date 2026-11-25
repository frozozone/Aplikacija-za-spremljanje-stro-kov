# Razredni diagram

![Razredni diagram](Razredni-diagram/Class_Diagram.svg)

[Diagram v polni velikosti](Razredni-diagram/Class_Diagram.svg)

#### Še ni implementirano
- `StoritevStatistika`, `KontrolerStatistika` in `StatistikaKategorije` (PU6)
- `StoritevKategorija` (PU10)

## Opis razredov:
Aplikacija je slojevita: kontroler, storitev, repozitorij, entiteta.

### Entitete:
- `Uporabnik`: uporabnik aplikacije (uporabniško ime, e-pošta, geslo, ime, priimek, vloga). Je lastnik stroškov in priliva. Dokler ni prijave, se uporablja privzeti uporabnik.
- `Vloga`: vloga uporabnika, vsak uporabnik ima eno.
- `Kategorija`: kategorija stroška; ima lahko nadrejeno kategorijo(0..1), kar omogoča podkategorije.
- `NacinPlacila`: šifrant načinov plačila (gotovina, kartica...)
- `Strosek`: en vnos porabe: naziv, znesek, datum, čas, opis, kategorija, način plačila, uporabnik.
- `Priliv`: mesečni priliv uporabnika (leto, mesec, znesek), osnova za izračun dnevnega limita.

### Repozitoriji:
Vsi razširjajo JpaRepository, ki že vsebuje osnovne operacije (findAll, findById, save, deleteById), zato so v diagramu prikazane le dodatne metode.
- `UporabnikBaza`: dostop do uporabnikov, `findByUporabniskoIme` poišče uporabnika (privzetega).
- `StrosekBaza`: dostop do stroškov, `findByKategorija` vrne stroške izbrane kategorije in njenih podkategorij, `vsotaStroskov` vrne vsoto stroškov uporabnika med dvema datumoma.
- `KategorijaBaza`, `NacinPlacilaBaza`: `findAllByOrderByNazivAsc` vrne šifrant po abecedi.
- `PrilivBaza`: `findByUporabnikIdAndLetoAndMesec` poišče priliv za izbran mesec.
  
### Storitve:
- `StoritevDnevniLimit`: iz baze zbere priliv in porabo ter ju preda izračunu, sama ne računa.
- `IzracunDnevnegaLimita`: pomožni razred s statičnimi metodami. Izračun je ločen od baze, zato ga je lahko testirati.
- `StoritevPriliv`: poišče priliv za tekoči mesec ter ga ustvari ali posodobi.
- `StoritevKategorija`: dodajanje, preimenovanje in brisanje kategorij.
- `StoritevStatistika`: statistika porabe po kategorijah za izbrano obdobje.
  
### Kontrolerji:
- `KontrolerStrosek`:
- `KontrolerPriliv`:
- `KontrolerDnevniLimit`:
- `KontrolerKategorija`, `KontrolerNacinPlacila`:
- `KontrolerStatistika`:
- `ObravnavaNapak`:

### record:
- `DnevniLimitOdgovor`:
- `PrilivZahteva`:
- `StatistikaKategorije`:

## Ključne metode in potek funkcionalnosti:
