# Aplikacija za spremljanje stroškov

Projekt pri predmetu razvoj informacijskih sistemov. Gre za preprosto spletno aplikacijo, kjer si lahko zapisuješ svoje stroške (npr. kava, najemnina, bencin) in jih razvrstiš po kategorijah.

## O projektu

Ideja je, da imaš na enem mestu pregled, na kaj zapravljaš denar.

Kaj aplikacija dela:
- dodajanje novega stroška (naziv, znesek, datum, čas, kategorija, način plačila, opis),
- urejanje in brisanje stroškov,
- prikaz stroškov po straneh, najnovejši so na vrhu,
- filtriranje po kategoriji (filter po glavni kategoriji pokaže tudi stroške njenih podkategorij),
- iskanje po nazivu

Aplikacija ima tri dele: frontend (kar vidi uporabnik), backend (REST API) in bazo.

## Uporabljene tehnologije
Backend:
- Java 21
- Spring Boot 3.2.5 (Spring Web, Spring Data JPA, Validation)
- PostgreSQL driver
- Swagger (springdoc-openapi 2.2.0)
- Maven (prek Maven Wrapperja, ki je že v projektu)

Frontend:
- React 19.3
- Vite 8.3
- Bootstrap 5.3
- ESLint 10.13

Baza in orodja:
- PostgreSQL 16 (teče v Dockerju)
- Docker Desktop
- Node.js 22 ali novejši
- VS Code in DataGrip

## Struktura projekta

Projekt je razdeljen na štiri glavne mape:

- backend - Spring Boot aplikacija
  - src/main/java/com/example/backend
    - BackendApplication.java - zažene aplikacijo
    - controller - java razredi/datoteke (kontrolerji), ki vsebujejo endpointe
      - KontrolerStrosek.java - dodajanje, branje, urejanje in brisanje stroškov
      - KontrolerKategorija.java - vrne seznam kategorij
      - KontrolerNacinPlacila.java - vrne seznam načinov plačila
      - ObravnavaNapak.java - napake pretvori v odgovor oblike {"message": "..."}, ki ga prikaže frontend
    - entitete - razredi, ki predstavljajo tabele v bazi, in interface-i za dostop do baze
      - Strosek.java, Kategorija.java, NacinPlacila.java, Uporabnik.java, Vloga.java - razredi za tabele v bazi
      - StrosekBaza.java, KategorijaBaza.java, ... - interface-i, ki dedujejo od JpaRepository, zato že imajo osnovne metode za delo z bazo (findAll, findById, save, delete)
        - StrosekBaza ima še svojo metodo findByKategorija, ki vrne stroške izbrane kategorije in njenih podkategorij
    - config - CorsConfig.java, ki dovoli, da frontend kliče backend
  - src/main/resources/application.properties - nastavitve za povezavo na bazo in Swagger
  - pom.xml - seznam knjižnic, ki jih backend uporablja
- frontend - React aplikacija
  - src
    - App.jsx - glavna komponenta
    - StranStroski.jsx - stran s stroški (filtri, strani, nalaganje podatkov)
    - TabelaStroskov.jsx, ObrazecNovStrosek.jsx, ModalUrejanjeStroska.jsx - tabela, obrazec za nov strošek in okno za urejanje
    - PoljaStroska.jsx, IzbirniSeznam.jsx - polja obrazca, ki so skupna za dodajanje in urejanje
    - zaledje.js - vsi klici na backend
    - pomozneFunkcije.js - datum, čas, urejanje kategorij, pretvorba podatkov obrazca
    - Header.jsx - glava strani
    - main.jsx - vstopna točka
    - index.css - stili
  - package.json - seznam knjižnic, ki jih frontend uporablja
- baza/baza-setup
  - docker-compose.yml - zažene PostgreSQL v Dockerju
  - praktikum_baza.sql - ustvari tabele (vloga, uporabnik, nacin_placila, kategorija, strosek) ter vstavi testne podatke (praktikum je samo ime datoteke, nima dejansko veze s praktikumom, ima le podobno konfiguracijo kot praktikum)
- dokumentacija - poročila vaj in screenshoti
- .env.example - predloga za okoljske spremenljivke (podatki za bazo)

Kje so pomembne stvari:
- endpointi so v mapi backend/.../controller
- uporabnik, geslo in ime baze so v datoteki .env (ta ni v gitu), application.properties jih samo prebere
- klici na backend so v frontend/src/zaledje.js
- tabele in testni podatki so v praktikum_baza.sql

## Navodila za nameščanje in zagon

### Predpogoji

Na računalniku moraš imeti nameščeno:
- Git,
- Javo (JDK 21 ali novejšo),
- Node.js 22 ali novejši (zraven pride tudi npm),
- Docker Desktop,
- po želji DataGrip za pregled baze

### 1. Prenos projekta
*git clone https://github.com/frozozone/Aplikacija-za-spremljanje-stro-kov.git*

*cd Aplikacija-za-spremljanje-stro-kov*

### 2. Okoljske spremenljivke

Uporabnik in geslo za bazo nista v gitu, ker ne spadata v javni repozitorij. Namesto tega so v datoteki .env v korenu projekta, ki jo moraš ustvariti sam.

V korenu projekta skopiraj predlogo:

*copy .env.example .env*

Nato odpri .env in vpiši svoje podatke, npr.:
- POSTGRES_USER=admin
- POSTGRES_PASSWORD=tvoje_geslo
- POSTGRES_DB=praktikum_baza

To datoteko uporabljata oba, Docker (ko ustvari bazo) in backend (ko se poveže na bazo), zato podatke vpišeš samo enkrat.

Poleg tega mora biti nastavljena sistemska spremenljivka JAVA_HOME, ki kaže na mapo, kjer je nameščena Java (npr. C:\Program Files\Java\jdk-21). Brez nje mvnw ne deluje. Preveriš jo z ukazom:

*echo %JAVA_HOME%*

### 3. Zagon baze
Docker Desktop mora biti prižgan. V mapi baza/baza-setup poženi:

*docker compose up -d*

Ob prvem zagonu se samodejno ustvarijo tabele in testni podatki. Z ukazom *docker ps* preveriš, ali container "praktikum" teče (status mora biti Up).

Za povezavo na bazo (npr. v DataGripu) uporabiš host localhost, port 5432 ter ime baze, uporabnika in geslo iz .env.

Če imaš na portu 5432 že kakšno drugo bazo, jo najprej ustavi.

Skripta praktikum_baza.sql se izvede samo, ko se container ustvari prvič. Če se skripta spremeni (npr. nove tabele), moraš container in njegove podatke pobrisati in ga ustvariti znova (vsi podatki v bazi se izgubijo):

*docker compose down -v*

*docker compose up -d*

Testni uporabniki v bazi (vsi imajo geslo *geslo123*, shranjeno kot bcrypt hash; prijave v aplikaciji še ni):
- admin - vloga Administrator (zaenkrat nima dodatnih pravic)
- ana - večina testnih stroškov
- marko - nekaj stroškov

Dokler prijava ni narejena, aplikacija prikaže stroške vseh uporabnikov, nove stroške pa shrani uporabniku, ki je nastavljen v application.properties (aplikacija.privzeti-uporabnik, privzeto ana).

### 4. Zagon backenda

V novem terminalu pojdi v mapo backend in poženi:

*mvnw spring-boot:run*

(Ukazi v teh navodilih so za cmd. Če uporabljaš PowerShell, npr. terminal v VS Code, napiši *.\mvnw spring-boot:run*.)

Backend teče na http://localhost:8080, seznam vseh endpointov pa je v Swaggerju na http://localhost:8080/swagger-ui.html

### 5. Zagon frontenda

V novem terminalu pojdi v mapo frontend in poženi:

*npm install*

*npm run dev*

Ukaz npm install je treba pognati samo prvič. Aplikacijo nato odpreš v brskalniku na http://localhost:5173

Vrstni red zagona je pomemben: najprej baza, nato backend, na koncu frontend.

## Navodila za razvijalce

### Veje

V veji main je vedno koda, ki deluje. Za vsako novo stvar naredimo svojo vejo:
- feature/ime - nova funkcionalnost, npr. feature/filter-po-datumu
- hotfix/ime - popravek napake, npr. hotfix/brisanje-stroska

### Potek dela

1. Pred začetkom dela poberi najnovejšo verzijo:

   *git pull*

2. Naredi novo vejo in se prestavi vanjo:

   *git checkout -b feature/ime-funkcionalnosti*

3. Ko končaš s spremembami:

   *git status*

   *git add .*

   *git commit -m "feat: kratek opis"*

   *git push origin feature/ime-funkcionalnosti*

4. Ko vse deluje, vejo združiš v main (na GitHubu odpreš Pull Request ali pa lokalno):

   *git checkout main*

   *git merge feature/ime-funkcionalnosti*

   *git push*

### Commit sporočila

Prva vrstica naj bo kratka (do približno 50 znakov) in naj se začne s tipom spremembe:
- feat: nova funkcionalnost, npr. "feat: dodano filtriranje po kategoriji"
- fix: popravek napake, npr. "fix: brisanje stroška ne osveži tabele"
- docs: spremembe v dokumentaciji, npr. "docs: posodobljen README"
- refactor: preurejena koda, ki ne spremeni delovanja

### Nekaj pravil

- pred pushom preveri, da se projekt zažene (vsi trije terminali morajo delati)
- en commit naj bo ena smiselna sprememba, raje več manjših kot en ogromen
- ne pushaj datotek, ki so v .gitignore (node_modules, target, dist, .env, Word dokumenti in druge stvari, ki niso potrebne za projekt)
- gesel in drugih skrivnosti nikoli ne pišemo v kodo, ampak v .env

## Standardi kodiranja

- imena spremenljivk, metod in razredov so v slovenščini, npr. dodajStrosek, izbranaKategorija
- kjer koda ni takoj jasna in intuitivna, je zraven kratek komentar