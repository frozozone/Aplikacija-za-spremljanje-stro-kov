import { useCallback, useEffect, useRef, useState } from "react";
import IzbirniSeznam from "./IzbirniSeznam";
import ModalUrejanjeStroska from "./ModalUrejanjeStroska";
import ObrazecNovStrosek from "./ObrazecNovStrosek";
import TabelaStroskov from "./TabelaStroskov";
import * as zaledje from "./zaledje";
import { danasnjiDatum, prikaziZnesek, urediKategorijeHierarhicno } from "./pomozneFunkcije";
import KarticaDnevniLimit from "./KarticaDnevniLimit";
import ObrazecPriliv from "./ObrazecPriliv";
import OpozoriloLimitModal from "./OpozoriloLimitModal";


const VELIKOST_STRANI = 10; //koliko stroskov se prikaze na eni strani

function StranStroski() {
  const [stroski, setStroski] = useState([]);
  const [iskanje, setIskanje] = useState(""); //tekst, ki ga uporabnik vpise v iskalno polje
  const [kategorije, setKategorije] = useState([]); //oblike { id, oznaka }, ze urejene po glavnih kategorijah
  const [naciniPlacila, setNaciniPlacila] = useState([]);
  const [izbranaKategorija, setIzbranaKategorija] = useState(null); //null pomeni prikazi vse
  const [strosekZaUrejanje, setStrosekZaUrejanje] = useState(null); //null pomeni, da okno za urejanje ni odprto
  const [stran, setStran] = useState(0); //trenutna stran, steje se od 0 naprej
  const [steviloStrani, setSteviloStrani] = useState(0); //koliko strani je vseh skupaj
  const [dnevniLimit, setDnevniLimit] = useState(null); //odgovor GET /dnevni-limit, null dokler se ne nalozi
  const [napakaLimita, setNapakaLimita] = useState(null); //napaka pri nalaganju limita, null ce je vse v redu
  const [opozoriloLimita, setOpozoriloLimita] = useState(null);
  const cakajocaOdlocitev = useRef(null);

  const obravnavajNapako = useCallback((napaka) => {
    console.error("Napaka:", napaka);
    alert(napaka.message);
  }, []);

  const prikaziStroske = useCallback((data) => {
    //ce smo izbrisali zadnji strosek na zadnji strani, se premaknemo na prejsnjo stran
    if (data.content.length === 0 && data.number > 0) {
      setStran(Math.max(data.totalPages - 1, 0));
      return;
    }
    setStroski(data.content);
    setSteviloStrani(data.totalPages); //za napis "stran 1/4" in gumb Naslednja
  }, []);

  //nalozi dnevni limit iz zaledja
  //napak ne obravnavamo z alert-om (obravnavajNapako), ker je npr. manjkajoci priliv normalno stanje,
  //ki ga lepo pokaze kartica sama
  const osveziDnevniLimit = useCallback(() => {
    zaledje.naloziDnevniLimit()
        .then((limit) => {
          setDnevniLimit(limit);
          setNapakaLimita(null);
        })
        .catch((napaka) => {
          setDnevniLimit(null);
          setNapakaLimita(napaka);
        });
  }, []);

  //ponovno nalozi stroske in dnevni limit iz zaledja (po dodajanju, urejanju, brisanju)
  //limit osvezujemo skupaj s stroski, ker vsaka sprememba stroska spremeni tudi limit
  const osvezi = useCallback(() => {
    zaledje.naloziStroske(stran, VELIKOST_STRANI, izbranaKategorija)
        .then(prikaziStroske)
        .catch(obravnavajNapako);
    osveziDnevniLimit();
  }, [stran, izbranaKategorija, prikaziStroske, obravnavajNapako, osveziDnevniLimit]);

  //sifranta (kategorije in nacini placila) nalozimo samo enkrat ob zagonu
  useEffect(() => {
    Promise.all([zaledje.naloziKategorije(), zaledje.naloziNacinePlacila()])
      .then(([vseKategorije, vsiNaciniPlacila]) => {
        setKategorije(urediKategorijeHierarhicno(vseKategorije));
        setNaciniPlacila(vsiNaciniPlacila.map((n) => ({ id: n.id, oznaka: n.naziv })));
      })
      .catch(obravnavajNapako);
  }, [obravnavajNapako]);

  //ko se spremeni kategorija ali stran, nalozimo stroske
  useEffect(() => {
    osvezi();
  }, [osvezi]);

  const shraniPoPreverjanjuLimita = async ({ strosek, stariStrosek = null, shrani }) => {
    const danes = danasnjiDatum();
    if (strosek.datum !== danes) {
      return shrani();
    }

    let limit;
    try {
      limit = await zaledje.naloziDnevniLimit();
    } catch (napaka) {
      if (napaka.status === 404) {
        return shrani();
      }
      obravnavajNapako(napaka);
      return false;
    }

    const stariZnesekDanes = stariStrosek?.datum === danes ? Number(stariStrosek.znesek) : 0;
    const porabaPoShranitvi = Number(limit.porabljenoDanes) - stariZnesekDanes + Number(strosek.znesek);
    const vCente = (znesek) => Math.round(Number(znesek) * 100);
    const presezenoDanes = (vCente(porabaPoShranitvi) - vCente(limit.dnevniLimit)) / 100;

    if (presezenoDanes <= 0) {
      return shrani();
    }

    const danesJeMesec = (datum) => datum?.slice(0, 7) === danes.slice(0, 7);
    const stariZnesekTaMesec = danesJeMesec(stariStrosek?.datum) ? Number(stariStrosek.znesek) : 0;
    const noviZnesekTaMesec = danesJeMesec(strosek.datum) ? Number(strosek.znesek) : 0;
    const porabaPoShranitviTaMesec =
      Number(limit.porabljenoTaMesec) - stariZnesekTaMesec + noviZnesekTaMesec;
    const presezenoTaMesec = Math.max(
      0,
      (vCente(porabaPoShranitviTaMesec) - vCente(limit.priliv)) / 100
    );

    return new Promise((resolve) => {
      cakajocaOdlocitev.current = async (potrjeno) => {
        setOpozoriloLimita(null);
        if (!potrjeno) {
          resolve(false);
          return;
        }
        resolve(await shrani());
      };
      setOpozoriloLimita({ presezenoDanes, presezenoTaMesec });
    });
  };

  const dodajStrosek = (novStrosek) =>
    shraniPoPreverjanjuLimita({
      strosek: novStrosek,
      shrani: async () => {
        try {
          await zaledje.dodajStrosek(novStrosek);
          osvezi();
          return true;
        } catch (napaka) {
          obravnavajNapako(napaka);
          return false;
        }
      },
    });

  const shraniUrejanje = (posodobljenStrosek) =>
    shraniPoPreverjanjuLimita({
      strosek: posodobljenStrosek,
      stariStrosek: strosekZaUrejanje,
      shrani: async () => {
        try {
          await zaledje.posodobiStrosek(strosekZaUrejanje.id, posodobljenStrosek);
          setStrosekZaUrejanje(null);
          osvezi();
          return true;
        } catch (napaka) {
          obravnavajNapako(napaka);
          return false;
        }
      },
    });

  const izberiOpozorilo = async (potrjeno) => {
    const odlocitev = cakajocaOdlocitev.current;
    cakajocaOdlocitev.current = null;
    if (odlocitev) {
      await odlocitev(potrjeno);
    }
  };

  const izbrisiStrosek = async (id) => {
    try {
      await zaledje.izbrisiStrosek(id);
      osvezi();
    } catch (napaka) {
      obravnavajNapako(napaka);
    }
  };

  const izberiKategorijo = (vrednost) => {
    setIzbranaKategorija(vrednost === "" ? null : Number(vrednost));
    setStran(0); //ob menjavi kategorije gremo nazaj na prvo stran
  };

  const filtriraniStroski = stroski.filter((s) => //pokaze samo stroske, ki v nazivu vsebujejo iskani tekst
    s.naziv.toLowerCase().includes(iskanje.toLowerCase())
  );

  return (
    <div className="row g-4">
      <div className="col-lg-8">
        <div className="mb-3">
          <input
            type="text"
            placeholder="Išči po nazivu..."
            className="form-control"
            value={iskanje}
            onChange={(e) => setIskanje(e.target.value)}
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Filtriraj glede na kategorijo</label>
          <IzbirniSeznam
            moznosti={kategorije}
            vrednost={izbranaKategorija}
            obSpremembi={izberiKategorijo}
            prazenNapis="Vse kategorije"
          />
        </div>
        <TabelaStroskov
          stroski={filtriraniStroski}
          obIzberi={setStrosekZaUrejanje}
          obIzbrisi={izbrisiStrosek}
        />
        <div className="d-flex justify-content-between align-items-center mt-3">
          <button className="btn btn-secondary" disabled={stran <= 0} onClick={() => setStran(stran - 1)}>Prejšnja</button>
          <span>Stran {stran + 1} / {steviloStrani || 1}</span>
          <button className="btn btn-secondary" disabled={stran >= steviloStrani - 1 || steviloStrani === 0} onClick={() => setStran(stran + 1)}>Naslednja</button>
        </div>
      </div>
      <div className="col-lg-4">
        <KarticaDnevniLimit dnevniLimit={dnevniLimit} napaka={napakaLimita} />
        <ObrazecNovStrosek kategorije={kategorije} naciniPlacila={naciniPlacila} obDodaj={dodajStrosek} />
        <ObrazecPriliv obShrani={osveziDnevniLimit} />
      </div>
      {strosekZaUrejanje && (
        <ModalUrejanjeStroska
          strosek={strosekZaUrejanje}
          kategorije={kategorije}
          naciniPlacila={naciniPlacila}
          obShrani={shraniUrejanje}
          obZapri={() => setStrosekZaUrejanje(null)}
        />
      )}
      {opozoriloLimita && (
        <OpozoriloLimitModal
          presezenoDanes={prikaziZnesek(opozoriloLimita.presezenoDanes)}
          presezenoTaMesec={opozoriloLimita.presezenoTaMesec}
          obShraniVseeno={() => izberiOpozorilo(true)}
          obPreklici={() => izberiOpozorilo(false)}
        />
      )}
    </div>
  );
}

export default StranStroski;
