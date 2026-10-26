# Aplikacija za spremljanje stroškov
## 1. Namen tega projekta bo omogočiti uporabniku spremljanje stroškov, torej spremljanje odlivov glede na mesečni priliv. Aplikacija bi potencialno lahko računala in priporočila maksimalni strošek tega dneva ali meseca. To bo uporabniku omogočilo boljšo organizacijo svojega budžeta.

## 2. Glavne funkcionalnosti za zdaj:

**Branje stroška**: Aplikacija vrne (testne) podatke o stroških v obliki JSON (GET).  
![POSTMAN zahtevek GET za localhost:8080/strosek](screenshoti\GET_STROSEK.png;base64, VAŠ BASE64_NIZ)
**Branje stroška po identifikatorju**: Aplikacija vrne podatke o strošku s specifičnim ID-jem (GET). 
![POSTMAN zahtevek GET za localhost:8080/strosek/{id}](screenshoti\GET_STROSEK_15.png) 
**Ustvarjanje novega stroška**: Ustvari nov strošek, ki je vnesen v obliki JSON (POST).  
![POSTMAN zahtevek POST za naslov localhost:8080/strosek](screenshoti\DODAJ_STROSEK.png)  
**Posodabljanje obstoječega stroška**: Posodobi obstoječ strošek z 1 ali več drugačnimi atributi in vrne v obliki JSON (PUT). 
![POSTMAN zahtevek PUT za naslov localhost:8080/strosek/{id}](screenshoti\POSODOBI.png)
**Brisanje stroška**: Izbriše strošek glede na ID (DELETE).  
![POSTMAN zahtevek DELETE za naslov localhost:8080/strosek/{id}](screenshoti\DELETE.png)

