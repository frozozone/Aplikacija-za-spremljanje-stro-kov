--=============================================
--Aplikacija za spremljanje stroskov - baza (PostgreSQL)
--Skripta se pozene sama, ko Docker prvic ustvari container
--=============================================

--pgcrypto rabimo samo zato, da lahko testnim uporabnikom ze tukaj nastavimo bcrypt hash gesla
--(backend uporablja BCryptPasswordEncoder, ki ta hash razume)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

--vrstni red brisanja je pomemben zaradi tujih kljucev
DROP TABLE IF EXISTS strosek CASCADE;
DROP TABLE IF EXISTS kategorija CASCADE;
DROP TABLE IF EXISTS nacin_placila CASCADE;
DROP TABLE IF EXISTS uporabnik CASCADE;
DROP TABLE IF EXISTS vloga CASCADE;


--1. VLOGA - sifrant vlog (Uporabnik, Administrator)
CREATE TABLE vloga(
    id_vloga SERIAL PRIMARY KEY,
    naziv VARCHAR(30) NOT NULL UNIQUE
);


--2. UPORABNIK - registrirani uporabniki
CREATE TABLE uporabnik(
    id_uporabnik SERIAL PRIMARY KEY,
    uporabnisko_ime VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(254) NOT NULL UNIQUE,
    geslo_hash VARCHAR(255) NOT NULL, --samo bcrypt hash, nikoli pravo geslo
    ime VARCHAR(80) NOT NULL,
    priimek VARCHAR(80) NOT NULL,
    id_vloga INTEGER NOT NULL,
    ustvarjen TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_vloga) REFERENCES vloga(id_vloga)
);


--3. NACIN PLACILA - sifrant (gotovina, kartica, ...)
CREATE TABLE nacin_placila(
    id_nacin_placila SERIAL PRIMARY KEY,
    naziv VARCHAR(80) NOT NULL UNIQUE
);


--4. KATEGORIJA - sifrant kategorij
--id_nadrejena kaze na drugo kategorijo v isti tabeli (rekurzivna relacija):
--NULL pomeni glavno kategorijo, drugace je podkategorija
CREATE TABLE kategorija(
    id_kategorija SERIAL PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL,
    id_nadrejena INTEGER NULL,
    FOREIGN KEY (id_nadrejena) REFERENCES kategorija(id_kategorija) ON DELETE RESTRICT, --kategorije s podkategorijami ne moremo izbrisati
    UNIQUE NULLS NOT DISTINCT (naziv, id_nadrejena) --NULLS NOT DISTINCT, da tudi dve glavni kategoriji ne moreta imeti istega imena
);


--5. STROSEK - posamezni stroski uporabnika
CREATE TABLE strosek(
    id_strosek SERIAL PRIMARY KEY,
    id_uporabnik INTEGER NOT NULL,
    id_kategorija INTEGER NOT NULL,
    id_nacin_placila INTEGER NOT NULL,
    naziv VARCHAR(100) NOT NULL,
    znesek NUMERIC(10, 2) NOT NULL CHECK (znesek > 0),
    datum DATE NOT NULL DEFAULT CURRENT_DATE,
    cas TIME,
    opis VARCHAR(500),
    ustvarjeno TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    --ce se uporabnik izbrise, se izbrisejo tudi njegovi stroski
    FOREIGN KEY (id_uporabnik) REFERENCES uporabnik(id_uporabnik) ON DELETE CASCADE,
    FOREIGN KEY (id_kategorija) REFERENCES kategorija(id_kategorija),
    FOREIGN KEY (id_nacin_placila) REFERENCES nacin_placila(id_nacin_placila)
);

--skoraj vedno iscemo stroske enega uporabnika
CREATE INDEX idx_strosek_uporabnik ON strosek(id_uporabnik);


--========================================================
--TESTNI PODATKI
--========================================================

INSERT INTO vloga (naziv) VALUES
('Uporabnik'),      --id 1
('Administrator');  --id 2


--vsi testni uporabniki imajo geslo: geslo123
INSERT INTO uporabnik (uporabnisko_ime, email, geslo_hash, ime, priimek, id_vloga) VALUES
('admin', 'admin@example.com', crypt('geslo123', gen_salt('bf', 10)), 'Admin', 'Administrator', 2), --id 1
('ana',   'ana@example.com',   crypt('geslo123', gen_salt('bf', 10)), 'Ana',   'Novak',         1), --id 2
('marko', 'marko@example.com', crypt('geslo123', gen_salt('bf', 10)), 'Marko', 'Kovač',         1); --id 3


INSERT INTO nacin_placila (naziv) VALUES
('Gotovina'),          --id 1
('Plačilna kartica'),  --id 2
('Bančno nakazilo');   --id 3


--glavne kategorije (id_nadrejena = NULL)
INSERT INTO kategorija (naziv, id_nadrejena) VALUES
('Hrana in pijača',             NULL), --id 1
('Prevoz',                      NULL), --id 2
('Zabava',                      NULL), --id 3
('Stanovanje in gospodinjstvo', NULL), --id 4
('Naročnine in komunikacije',   NULL), --id 5
('Zdravje in nega',             NULL), --id 6
('Izobraževanje',               NULL), --id 7
('Šport in rekreacija',         NULL), --id 8
('Darila',                      NULL), --id 9
('Drugo',                       NULL); --id 10

--podkategorije
INSERT INTO kategorija (naziv, id_nadrejena) VALUES
('Trgovina',           1), --id 11  Hrana in pijača > Trgovina
('Restavracije',       1), --id 12  Hrana in pijača > Restavracije
('Kava in malica',     1), --id 13  Hrana in pijača > Kava in malica
('Gorivo',             2), --id 14  Prevoz > Gorivo
('Javni prevoz',       2), --id 15  Prevoz > Javni prevoz
('Parkiranje',         2), --id 16  Prevoz > Parkiranje
('Kino in koncerti',   3), --id 17  Zabava > Kino in koncerti
('Najemnina',          4), --id 18  Stanovanje in gospodinjstvo > Najemnina
('Elektrika',          4), --id 19  Stanovanje in gospodinjstvo > Elektrika
('Internet',           5), --id 20  Naročnine in komunikacije > Internet
('Telefon',            5), --id 21  Naročnine in komunikacije > Telefon
('Pretočne storitve',  5), --id 22  Naročnine in komunikacije > Pretočne storitve
('Lekarna',            6), --id 23  Zdravje in nega > Lekarna
('Knjige in učbeniki', 7); --id 24  Izobraževanje > Knjige in učbeniki


--stroski (vecina za ano, da je kaj videti v tabeli in da deluje paginacija)
INSERT INTO strosek (id_uporabnik, id_kategorija, id_nacin_placila, naziv, znesek, datum, cas, opis) VALUES
--ana (id 2)
(2, 13, 1, 'Kava',                       2.50, '2026-06-07', '08:15:00', NULL),
(2, 12, 2, 'Kosilo',                    12.00, '2026-06-07', '12:30:00', 'Dnevno kosilo blizu faksa'),
(2, 15, 2, 'Mesečna karta za avtobus',  38.00, '2026-06-01', '07:45:00', NULL),
(2, 14, 2, 'Bencin',                    54.30, '2026-06-03', '17:20:00', 'Gorivo za vikend domov'),
(2, 11, 2, 'Nakup v Spar',              47.85, '2026-06-04', '16:10:00', 'Tedenski nakup'),
(2, 17, 2, 'Kino',                       9.50, '2026-06-05', '20:00:00', NULL),
(2, 18, 3, 'Najemnina',                320.00, '2026-06-01', '09:00:00', 'Soba v Mariboru'),
(2, 19, 3, 'Elektrika',                 41.20, '2026-06-10', '10:15:00', 'Račun za maj'),
(2, 20, 3, 'Internet',                  29.99, '2026-06-10', '10:20:00', NULL),
(2, 21, 3, 'Telefonski račun',          15.00, '2026-06-12', '11:00:00', NULL),
(2,  8, 2, 'Fitnes članarina',          25.00, '2026-06-13', '18:30:00', NULL),
(2, 24, 2, 'Učbenik',                   34.90, '2026-06-14', '14:25:00', 'Učbenik za statistiko'),
(2, 12, 1, 'Pica',                       8.50, '2026-06-15', '13:10:00', NULL),
(2,  6, 1, 'Frizer',                    18.00, '2026-06-16', '15:45:00', NULL),
(2, 23, 1, 'Zdravila',                  11.40, '2026-06-18', '09:30:00', 'Tablete za glavobol'),
(2,  9, 1, 'Darilo za rojstni dan',     22.00, '2026-06-20', '16:40:00', NULL),
(2, 22, 2, 'Spotify',                   10.99, '2026-06-21', '08:00:00', NULL),
(2,  2, 1, 'Taksi',                     14.60, '2026-06-22', '23:15:00', NULL),
(2, 12, 2, 'Večerja v restavraciji',    27.80, '2026-06-24', '19:30:00', 'Večerja s prijatelji'),
(2, 17, 2, 'Vstopnica za koncert',      35.00, '2026-06-27', '21:00:00', NULL),
(2, 13, 1, 'Zajtrk v pekarni',           4.20, '2026-06-02', '07:50:00', NULL),
(2, 24, 2, 'Knjiga',                    16.90, '2026-06-06', '13:25:00', NULL),
(2, 16, 1, 'Parkirnina',                 3.00, '2026-06-08', '10:40:00', NULL),
(2,  1, 1, 'Sladoled',                   3.50, '2026-06-09', '15:20:00', NULL),
(2,  2, 2, 'Pranje avtomobila',         12.00, '2026-06-11', '17:35:00', NULL),
(2,  2, 2, 'Kolesarska luč',            19.90, '2026-06-17', '12:15:00', NULL),
--marko (id 3)
(3, 13, 1, 'Kava s prijateljem',         6.80, '2026-06-19', '16:30:00', NULL),
(3,  4, 2, 'Čistila',                   13.45, '2026-06-23', '18:10:00', NULL),
(3, 13, 1, 'Malica',                     5.60, '2026-06-25', '10:05:00', NULL),
(3,  3, 2, 'Družabna igra',             24.99, '2026-06-28', '14:50:00', 'Darilo zase'),
--admin (id 1)
(1, 10, 3, 'Gostovanje strežnika',      15.00, '2026-06-30', '09:00:00', 'Testni strošek administratorja');
