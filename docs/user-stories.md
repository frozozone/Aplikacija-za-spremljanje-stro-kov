# US-1: Vnos mesečnega priliva (*Omar*)

## Uporabniška zgodba

**Zgodba**: Kot študent z rednim mesečnim prilivom (štipendija/plača/žepnina) želim vnesti znesek priliva za tekoči mesec, da mi aplikacija lahko izračuna, koliko lahko porabim na dan.

## Sprejemni kriteriji (Acceptance Criteria)

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

## Podrobnosti implementacije

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

# US-2: Prikaz priporočenega dnevnega limita (PU9)

**Odgovorni član:** Mojca Ivanovič Kopecky

## Uporabniška zgodba (user story)
**_Kot_** študent z mesečnim prilivom **_želim_** v aplikaciji videti, koliko denarja lahko še porabim danes, **_da_** svoj mesečni priliv enakomerno razdelim in ga ne porabim prehitro.

## Sprejemni kriteriji (acceptance criteria)
- **AC1: Uspešen prikaz limita**
**_Given:_** uporabnik ima za tekoči mesec vnesen priliv 600€, do včeraj je porabil 300€, danes 12€, današnji datum je 16.11.2026
**_When:_** odpre stran s stroški
**_Then:_** kartica prikaže dnevni limit 20€, preostali znesek meseca 288€, preostalih 15 dni in napis `"Danes lahko porabiš še 8€"`
- **AC2: Limit za danes je presežen**
**_Given:_** uporabnik ima priliv 600 €, do včeraj je porabil 300 €, danes je porabil 25 €, mesečna poraba je pod prilivom
**_When:_** odpre stran s stroški
**_Then:_** v rdečem besedilu piše `"Za danes si limit presegel za 5 €"` in ne piše `"Danes lahko porabiš še ..."`

- **AC3: Mesečni priliv je presežen**
**_Given:_** uporabnik ima priliv 600 €, v tekočem mesecu je porabil 650 €
**_When:_** odpre stran s stroški
**_Then:_** v rdečem besedilu piše `"Ta mesec si priliv presegel za 50 €"`, dnevni limit pa ni prikazan kot  `"0 €"`

- **AC4: Priliv še ni vnesen (napaka)**
**_Given:_** uporabnik za tekoči mesec še nima vnesenega priliva
**_When:_** odpre stran s stroški
**_Then:_** namesto zneskov se prikaže sporočilo `"Za prikaz dnevnega limita najprej vnesite mesečni priliv"` in ni nobenega zneska (limit, preostalo, dnevi)

- **AC5: Posodobitev po spremembi stroška**
**_Given:_** kartica prikazuje `"Danes lahko porabiš še 8€"`
**_When:_** doda današnji strošek 3€
**_Then:_** kartica brez ročnega osveževanja strani prikaže `»Danes lahko porabiš še 5 €«`

- **AC6: Zadnji dan v mesecu**
**_Given:_** današnji datum je 30. 11. 2026, priliv je 600 €, do včeraj je porabljeno 570 €, danes 0 €
**_When:_** odpre stran s stroški
**_Then:_** preostalih dni je 1 in `"Danes lahko porabiš še 30€"`

## Podrobnosti implementacije
**Baza:**
- Nove tabele ni, uporablja se `priliv` iz US-1 in obstoječa tabela `strošek`
- Dokler US-1 ni združen je v `praktikum_baza.sql` začasna tabela `priliv` in testni podatki za ano v tekočem mesecu (priliv 600€, stroški 250€ + 50€ prvi dan v mesecu, 12€ je danes); datumi so relativni na `CURRENT_DATE`, zato primer deluje v vsakem mesecu

**Backend:**
- `GET /dnevni-limit`: vedno za danes, za privzetega uporabnika (ana). Vrne 200 z JSON:
```
{
  "priliv": 600.00, 
  "porabljenoDoVceraj": 300.00,
  "porabljenoDanes": 12.00,
  "porabljenoTaMesec": 312.00, 
  "preostaliDnevi": 15, 
  "dnevniLimit": 20.00,
  "danesLahkoPorabisSe": 8.00, 
  "presezenoDanes": 0.00, 
  "presezenoTaMesec": 0.00
}
```
- če priliva za tekoči mesec ni vrne 404: ` {"message": "Za prikaz dnevnega limita najprej vnesite mesečni priliv"}`
- Formule:
     - `preostaliDnevi = zadnji dan v mesecu - današnji dan + 1`
     - `dnevni limit = (priliv - porabljenoDoVceraj) / preostaliDnevi`, zaokroženo navzdol na 2 decimalki, 0 če je `porabljenoDoVceraj >= priliv`
     - `danesLahkoPorabisSe = max(dnevniLimit - porabljenoDanes, 0)` 
     - `presezenoDanes = max(porabljenoDanes - dnevniLimit, 0)`
     - `presezenoTaMesec = max(porabljenoTaMesec - priliv, 0)`
- Nove datoteke:
     - `controller/KontrolerDnevniLimit.java`
     - `storitve/StoritevDnevniLimit.java` (bere priliv in porabo)
     - `storitve/IzracunDnevnegaLimita.java` (čist izračun brez baze)
     - `storitve/DnevniLimitOdgovor.java`
     - `IzracunDnevnegaLimitaTest.java `(testi za kriterije)
 - V `porabljenoTaMesec` štejejo samo stroški do vključno danes, zato je `porabljenoTaMesec = porabljenoDoVceraj + porabljenoDanes`
 - Spremenjena datoteka: entitete/StrosekBaza.java (poizvedba vsotaStroskov, za vsoto porabe med dvema datumoma)
 - Napake vrača v obliki `{"message": "..."}` prek obstoječe datoteke `ObravnavaNapak.java`
 - Dokler ni prijave vse velja za privzetega uporabnika (ana)

**Frontend:**
- Nova komponenta `KarticaDnevniLimit.jsx` na strani s stroški, nad obrazcem Nov strošek
- Prikaz po vrstnem redu prioritete: priliv ni vnesen, priliv presežen, limit presežen, sicer `"Danes lahko porabiš še x €"`
- Nova funkcija `naloziDnevniLimit` v `zaledje.js`; napaka nosi tudi `status`, da kartica loči 404 od drugih napak
- `StranStroski.jsx` naloži limit in ga osveži ob vsakem dodajanju, urejanju in brisanju stroška

**Dodatna razlaga:**
- Dnevni limit se zaokroži navzdol, da priporočen limit nikoli ni previsoko
- Pri preseženemu prilivu se limit ne prikaže kot 0€
- US-3 uporablja isti `GET /dnevni-limit` za preverjanje pri shranjevanju stroška

### Dodatna razlaga

- Obrazec omogoča vnos samo za tekoči mesec, ker je za aplikacijo v tej fazi relevanten samo en koledarski mesec

