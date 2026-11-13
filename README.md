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
## Imena članov ekipe
- Omar Gušmirović
- Mojca Ivanovič Kopecky
- Patrik Martinec
## Vizija projekta

### Opis rešitve

Aplikacija je namenjena predvsem študentom, ki vsak mesec dobijo podoben znesek (npr. štipendija, žepnina, študentsko delo). Uporabna je tudi za vse druge, ki imajo reden mesečni priliv.

Problem, ki ga rešuje: sredi meseca ugotoviš, da ti je ostalo npr. 30 €, do naslednjega priliva pa je še 10 ali 15 dni.

Aplikacija ti zato vsak dan pove, koliko lahko še porabiš, da ti denar zdrži do konca meseca. Za nazaj pa pokaže, na kaj si denar porabil.

Priporočeni dnevni limit se izračuna tako:

*dnevni limit = (mesečni priliv − porabljeno ta mesec) / preostali dnevi v mesecu*

Primer: če imaš 600 € priliva in si do 15. v mesecu porabil 300 €, ti za preostalih 15 dni ostane 300 €, torej 20 € na dan.

Limit se vsak dan izračuna na novo. Če danes porabiš manj, je jutri limit višji, če porabiš več, je nižji.

#### Ključne funkcionalnosti

Že narejeno:
- pregled stroškov po straneh (PU1),
- iskanje po nazivu in filtriranje po kategoriji (PU2),
- dodajanje, urejanje in brisanje stroškov z izbiro kategorije (PU3, PU4, PU5)

Načrtovano:
- vnos mesečnega priliva (PU7),
- prikaz priporočenega dnevnega limita (PU9),
- opozorilo, če z novim stroškom presežeš dnevni limit (PU8),
- pregled porabe po kategorijah za tekoči mesec (PU6); če je stroškov premalo, se izpiše sporočilo "premalo podatkov",
- upravljanje kategorij, ki ga opravlja administrator (PU10)

### Obseg rešitve in morebitne omejitve

Aplikacija pokriva vnos stroškov, izračun dnevnega limita in pregled porabe za tekoči koledarski mesec.

Omejitve:
- stroške vnašaš ročno, povezave z banko ni,
- podprta je ena valuta, obdobje je vedno koledarski mesec,
- namenjena je rednim mesečnim prilivom; pri nerednih (npr. honorarji) limit ni smiseln,
- limit je samo priporočilo, aplikacija vnosa stroška ne prepreči,
- limit je točen šele, ko so vneseni večji stroški (npr. najemnina), zato jih je najbolje vnesti takoj na začetku meseca,
- varčevalnih ciljev ni; če želiš nekaj prihraniti, vpišeš ustrezno nižji priliv,
- večmesečnih trendov in napovedi ni,
- prijave uporabnikov še ni, zato priliv in limit veljata za privzetega uporabnika

### Besednjak

| Izraz | Razlaga |
|---|---|
| **Strošek** | En vnos porabljenega denarja z nazivom, zneskom, datumom, kategorijo in načinom plačila (čas in opis sta neobvezna). Npr. "Kosilo, 12,00 €, 7. 6. 2026, Restavracije, plačilna kartica". |
| **Kategorija** | Glavna skupina, v katero razvrstiš strošek, da vidiš, za kaj zapravljaš. Npr. Hrana in pijača, Prevoz, Zabava. |
| **Podkategorija** | Natančnejša kategorija znotraj glavne kategorije; filter po glavni kategoriji pokaže tudi stroške njenih podkategorij. Npr. Prevoz > Gorivo. |
| **Način plačila** | Kako je bil strošek plačan. Npr. gotovina, plačilna kartica ali bančno nakazilo. |
| **Mesečni priliv** | Znesek, ki ga uporabnik redno dobi vsak mesec in iz katerega se računa dnevni limit. Npr. štipendija 600 €. |
| **Porabljeno ta mesec** | Vsota vseh uporabnikovih stroškov od začetka tekočega koledarskega meseca do danes. |
| **Preostali znesek** | Koliko denarja še ostane do konca meseca (mesečni priliv − porabljeno ta mesec). Npr. 600 € − 300 € = 300 €. |
| **Preostali dnevi** | Število dni, ki jih je treba še pokriti do konca tekočega meseca. Npr. v 30-dnevnem mesecu jih je po 15. dnevu še 15. |
| **Priporočeni dnevni limit** | Znesek, ki ga lahko porabiš na dan, da ti denar zdrži do konca meseca (preostali znesek / preostali dnevi); vsak dan se izračuna na novo. Npr. 300 € / 15 dni = 20 € na dan. |
| **Preseganje limita** | Ko bi z novim stroškom porabil več od priporočenega limita, te aplikacija opozori, vnosa pa ne prepreči. Npr. limit je 20 €, vneseš strošek 35 €. |
| **Uporabnik** | Oseba z računom v aplikaciji, ki vnaša in pregleduje svoje stroške ter mesečni priliv. Npr. testni račun ana. |
| **Administrator** | Uporabnik z vlogo Administrator, ki poleg tega upravlja kategorije in podkategorije (zaenkrat še nima dodatnih pravic). Npr. testni račun admin. |

## Diagram primerov uporabe (DPU)

Diagram prikazuje ključne funkcionalnosti v načrtovani različici aplikacije.

![Diagram primerov uporabe](dokumentacija/dpu.png)

**Že implementirane funkcionalnosti:**\
PU1: pregled stroškov\
PU2: iskanje in filtriranje stroškov\
PU3: dodajanje stroška\
PU4: urejanje in brisanje stroška\
PU5: izbira kategorije

**Načrtovane funkcionalnosti:**\
PU6: pregled statistike po kategorijah\
PU7: vnos mesečnega  priliva\
PU8: opozorilo o preseganju priporočenega dnevnega limita\
PU9: prikaz priporočenega dnevnega limita\
PU10: upravljanje kategorij

Akterja:
- Uporabnik, ki upravlja stroške in mesečni priliv ter vidi priporočeni dnevni limit
- Administrator, ki upravlja kategorije

Povezave:
- `<<include>>`: dodajanje (PU3) in urejanje stroška (PU4) vedno vključujeta izbiro kategorije (PU5)
- `<<extend>>`: iskanje in filtriranje (PU2) razširja pregled stroškov (PU1)
- `<<extend>>`: opozorilo o preseganju priporočenega dnevnega limita (PU8) razširja urejanje stroška (PU3), vendar samo če strošek preseže dnevni limit

## Podroben opis primerov uporabe:
[Opis primerov uporabe](dokumentacija/Primeri_uporabe_PU-01_PU-10.pdf)
## Uporabljene tehnologije
Backend:
- Java 21
- Spring Boot 3.2.5 (Spring Web, Spring Data JPA, Validation)
- Postgres: 16
- Swagger (springdoc-openapi 2.2.0)
- Maven 3.9.16 (prek Maven Wrapperja 3.3.4; Mavena ni treba namestiti posebej)

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
```
.
├── backend/                                # Spring Boot aplikacija
│   ├── src/main/java/com/example/backend/
│   │   ├── BackendApplication.java         # zažene aplikacijo
│   │   ├── controller/                     # kontrolerji z endpointi
│   │   │   ├── KontrolerStrosek.java       # dodajanje, branje, urejanje, brisanje stroškov
│   │   │   ├── KontrolerKategorija.java    # vrne seznam kategorij
│   │   │   ├── KontrolerNacinPlacila.java  # vrne seznam načinov plačila
│   │   │   └── ObravnavaNapak.java         # napake iz kontrolerjev vrne kot {"message": "..."}
│   │   ├── entitete/                       # entitete (tabele) in dostop do baze
│   │   │   ├── Strosek.java                # tabela strosek
│   │   │   ├── Kategorija.java             # tabela kategorija (z nadrejeno kategorijo)
│   │   │   ├── NacinPlacila.java           # tabela nacin_placila
│   │   │   ├── Uporabnik.java              # tabela uporabnik
│   │   │   ├── Vloga.java                  # tabela vloga
│   │   │   ├── StrosekBaza.java            # JpaRepository + findByKategorija
│   │   │   ├── KategorijaBaza.java         # JpaRepository + kategorije po abecedi
│   │   │   ├── NacinPlacilaBaza.java       # JpaRepository + načini plačila po abecedi
│   │   │   └── UporabnikBaza.java          # JpaRepository + iskanje po uporabniškem imenu
│   │   └── config/
│   │       └── CorsConfig.java             # dovoli, da frontend kliče backend
│   ├── src/main/resources/
│   │   └── application.properties          # povezava na bazo, Swagger in privzeti uporabnik
│   ├── mvnw, mvnw.cmd                      # Maven Wrapper
│   └── pom.xml                             # knjižnice backenda
├── frontend/                               # React aplikacija
│   ├── src/
│   │   ├── main.jsx                        # vstopna točka
│   │   ├── App.jsx                         # ogrodje strani (glava + stran s stroški)
│   │   ├── Header.jsx                      # glava strani
│   │   ├── StranStroski.jsx                # glavna stran: nalaganje podatkov, iskanje, filter, strani
│   │   ├── TabelaStroskov.jsx              # tabela stroškov z gumboma za urejanje in brisanje
│   │   ├── ObrazecNovStrosek.jsx           # obrazec za dodajanje stroška
│   │   ├── ModalUrejanjeStroska.jsx        # okno za urejanje stroška
│   │   ├── PoljaStroska.jsx                # polja, skupna obrazcu in oknu za urejanje
│   │   ├── IzbirniSeznam.jsx               # spustni seznam (kategorije, načini plačila)
│   │   ├── pomozneFunkcije.js              # datum, čas, preverjanje obrazca, izpis zneska
│   │   ├── zaledje.js                      # vsi klici na backend
│   │   └── index.css                       # stili
│   ├── vite.config.js                      # nastavitve Vite
│   └── package.json                        # knjižnice frontenda
├── baza/baza-setup/
│   ├── docker-compose.yml                  # zažene PostgreSQL v Dockerju
│   └── praktikum_baza.sql                  # ustvari tabele in vstavi testne podatke
├── dokumentacija/
│   ├── dpu.png                             # diagram primerov uporabe
│   ├── screenshoti/                        # screenshoti testiranja endpointov
│   └── *_vaja1.MD                          # poročilo vaje
└── .env.example                            # predloga za okoljske spremenljivke (podatki za bazo)
```

**Opombe:**
- Vse `...Baza` datoteke dedujejo od `JpaRepository`, zato že imajo osnovne metode (`findAll`, `findById`, `save`, `deleteById`). `StrosekBaza` ima še `findByKategorija`, ki vrne stroške izbrane kategorije in vseh njenih podkategorij.
- Tabele v bazi so: `strosek`, `kategorija`, `nacin_placila`, `uporabnik` in `vloga`.
- `praktikum_baza.sql` je samo ime datoteke. S praktikumom nima zveze, ima le podobno konfiguracijo.

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
- Node.js 22 ali novejši (zraven pride tudi npm) naložimo v mapo frontend,
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

Poleg tega **mora biti** nastavljena sistemska spremenljivka JAVA_HOME, ki kaže na mapo, kjer je nameščena Java (npr. C:\Program Files\Java\jdk-21). Brez nje mvnw ne deluje. Preveriš jo z ukazom:

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

ALI

*mvn spring-boot:run* če imaš ustrezni Maven naložen

(Ukazi v teh navodilih so za cmd. Če uporabljaš PowerShell, npr. terminal v VS Code, je isto samo z drugo sintakso recimo: *.\mvnw spring-boot:run*.)

Backend teče na http://localhost:8080, seznam vseh endpointov pa je v Swaggerju na http://localhost:8080/swagger-ui.html

### 5. Zagon frontenda

V novem terminalu pojdi v mapo frontend in poženi:

*npm install* (če še nimaš) za node modules

*npm run dev*

Ukaz npm install je treba pognati samo prvič. Aplikacijo nato odpreš v brskalniku na **http://localhost:5173**

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

- imena spremenljivk, metod in razredov so v slovenščini in smiselno in intuitivno poimenovane, npr. dodajStrosek, izbranaKategorija in brez okrajšav recimo dodStros namesto dodajStrosek
- če koda ni takoj jasna in intuitivna, je zraven kratek komentar, ki opiše delovanje drugače pa samo informativni komentar nad blokom kode (nepotrebni komentar ki opisuje to kar nam že pove sintaksa je odveč)
- če si nek velik blok kode mergal na main vejo ali pa pač delaš na njem, zapiši kdaj je bil merge izveden, in kdo ga je izvedel, ter kdo je kodo pisal recimo nekaj takega: ```//Merge 11.9.2026 Omar, feat: Filtriranje stroška po kategoriji"```
- datoteka ni daljša od 400 vrstic
- uporablja se camelCase poimenovanje recimo "dodajStrosek" (če sintaksa tega ne dovoli pa PascalCase recimo "DodajStrosek" namesto "dodajStrosek"
- funkcije so čim krajše in čim enostavnejše, morajo opravljiti eno funkcijo, če je možno funkcijo razbiti na več delov, potem pišeš več funkcij
- imena datotek so smiselna in intuitivna
- preden se nadaljuje z razvojem je treba kodo ki je bila napisana stestirati, najlažje skozi Swagger, potem v datagripu ali kje drugje pogledati kako se baza obnaša in na koncu še ročno se postaviti v scenarij in feature stestirati v čim več možnih situacijah.
- Pisanje SQL stavkov ali sintakse z velikimi črkami recimo: "SELECT * FROM strosek" namesto "select * from strosek"
- Komentari za to do recimo "TODO: Popravi izgled forma za dodajanje stroška"
- če je le možno se držimo exception handlinga torej try-catch stavkov, v večino primerih boljši način kot pa recimo ugnezdeni if stavki (enako stvar dosežemo z bolj pregledno kodo)
