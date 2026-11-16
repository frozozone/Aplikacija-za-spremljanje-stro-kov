# 1. Uporabniške zgodbe

## US-1: Vnos mesečnega priliva (*Omar*)

**Zgodba**: Kot študent z rednim mesečnim prilivom (štipendija/plača/žepnina) želim vnesti znesek priliva za tekoči mesec, da mi aplikacija lahko izračuna, koliko lahko porabim na dan.

# 2. Sprejemni kriteriji (Acceptance Criteria)

### 1. Prvi vnos

***Given***: danes je 16.11.2026 in za november 2026 priliv še ni vnesen  
***When***: uporabnik v polje za priliv vnese  400€ in klikne ```Shrani```  
***Then***: na strani piše "Mesečni priliv za november 2026: 400€

### 2. Sprememba priliva

***Given***: priliv za november 2026 je 400€  
***When***: uporabnik vnese 500€ in klikne ```Shrani```  
***Then***: na strani piše "```Mesečni priliv za november 2026: 500€```"

### 3. Negativni ali ničelni priliv

***Given***: priliv za november 2026 je 400€  
***When***: uporabnik vnese -500€ ali 0€ in klikne ```Shrani```  
***Then***: na strani izpiše ```priliv mora biti večji od 0``` in priliv ostane 400€


### 4. Priliv še ni vnesen

***Given***: priliv za november 2026 ne obstaja  
***When***: uporabnik odpre aplikacijo  
***Then***: stran izpiše ```Priliv za ta mesec še ni vnesen```

# 3. Podrobnosti implementacije

### Baza

#### Nova tabela ```priliv```
``` sql
CREATE TABLE priliv(
id_priliv SERIAL PRIMARY KEY,
id_uporabnik INTEGER NOT NULL REFERENCES uporabnik(id_uporabnik),
leto INTEGER NOT NULL,
mesec INTEGER NOT NULL CHECK (mesec BETWEEN 1 AND 12),
znesek NUMERIC (10,2) NOT NULL CHECK (znesek>0),
UNIQUE (id_uporabnik, leto, mesec)
);
```

Razlaga nekaterih atributov:
- ```id_uporabnik``` (v tabeli *priliv*) je tuji ključ ki kaže na ```uporabnik.id_uporabnik```. **ON DELETE CASCADE**, zato, da ko se izbriše uporabnik se izbrišejp tudi njegovi prilivi, enako kot stroški

- ```mesec``` ima pogoj da je lahko vrednost med 1 in 12 (januar-december)
- ```znesek``` ima pogoj, da je lahko samo več kot 0
- ``` UNIQUE (id_uporabnik, leto, mesec) ``` en priliv na uporabnika na mesec, kar pokrije 2. kriterij: "Sprememba priliva"

### Backend

- ```GET /priliv```: vnre priliv za tekoči mesec, če ga ni vrne sporočilo *"Priliv za ta mesec še ni vnesen"*
- ```PUT /priliv```: shrani priliv. Če za tekoči mesec že obstaja ga prepiše (posodabljanje oz. sprememba zapisa).
V optimalnem primeru vrne 200: 
``` json
{"id": 1, "leto": 2026, "mesec": 11, "znesek": 500.00}
```
V primeru, da je vnos neveljaven (```znesek=0 || znesek<0```) pa vrne 400:
``` json
{"message": "priliv mora biti večji od 0"}
```
- nove datoteke: 
```entitete/Priliv.java```,
```entitete/PrilivBaza.java```,
```controller/KontrolerPriliv.java```
- napake vrača v obliki 
``` json
{"message": "..."}
```
preko obstoječega ```ObravnavaNapak.java```
- dokler ni prijave, priliv pripada privzetemu uporabniku (ana)

### Frontend

- nova komponenta ```ObrazecPriliv.jsx``` na strani s stroški, nad obrazcem *Nov strošek*
- polje za **znesek**, 
- gumb **Shrani** 
- gumb **Shrani** (ali posodobi)
- besedilo (status) "Mesečni priliv za november 2026: 400€" in prikaz napak
- funkciji ```naloziPriliv``` in ```shraniPriliv``` v ```zaledje.js``` (povezava in klici na beckend so na enem mestu)

### Dodatna razlaga

- Obrazec omogoča vnos samo za tekoči mesec, ker je za aplikacijo v tej fazi relevanten samo en koledarski mesec

