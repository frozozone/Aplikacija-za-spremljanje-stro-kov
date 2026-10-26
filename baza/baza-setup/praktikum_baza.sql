DROP TABLE IF EXISTS Strosek CASCADE;


CREATE TABLE Strosek(
    ID_strosek SERIAL PRIMARY KEY,
    naziv varchar(50),
    znesek decimal(6,2),
    datum date,
    cas time
);

INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Kava', 2.50, '2026-06-07', '08:15:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Kosilo', 12.00, '2026-06-07', '12:30:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Mesečna karta za avtobus', 38.00, '2026-06-01', '07:45:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Bencin', 54.30, '2026-06-03', '17:20:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Nakup v Spar', 47.85, '2026-06-04', '16:10:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Kino', 9.50, '2026-06-05', '20:00:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Najemnina', 320.00, '2026-06-01', '09:00:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Elektrika', 41.20, '2026-06-10', '10:15:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Internet', 29.99, '2026-06-10', '10:20:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Telefonski račun', 15.00, '2026-06-12', '11:00:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Fitnes članarina', 25.00, '2026-06-13', '18:30:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Učbenik', 34.90, '2026-06-14', '14:25:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Pica', 8.50, '2026-06-15', '13:10:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Frizer', 18.00, '2026-06-16', '15:45:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Zdravila', 11.40, '2026-06-18', '09:30:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Darilo za rojstni dan', 22.00, '2026-06-20', '16:40:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Spotify', 10.99, '2026-06-21', '08:00:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Taksi', 14.60, '2026-06-22', '23:15:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Večerja v restavraciji', 27.80, '2026-06-24', '19:30:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Vstopnica za koncert', 35.00, '2026-06-27', '21:00:00');

-- 10 novih stroškov
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Zajtrk v pekarni', 4.20, '2026-06-02', '07:50:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Knjiga', 16.90, '2026-06-06', '13:25:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Parkirnina', 3.00, '2026-06-08', '10:40:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Sladoled', 3.50, '2026-06-09', '15:20:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Pranje avtomobila', 12.00, '2026-06-11', '17:35:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Kolesarska luč', 19.90, '2026-06-17', '12:15:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Kava s prijateljem', 6.80, '2026-06-19', '16:30:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Čistila', 13.45, '2026-06-23', '18:10:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Malica', 5.60, '2026-06-25', '10:05:00');
INSERT INTO strosek (naziv, znesek, datum, cas) VALUES ('Družabna igra', 24.99, '2026-06-28', '14:50:00');
