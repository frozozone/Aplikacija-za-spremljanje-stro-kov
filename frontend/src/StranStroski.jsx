import { useCallback, useEffect, useState } from "react";
import IzbirniSeznam from "./IzbirniSeznam";
import ModalUrejanjeStroska from "./ModalUrejanjeStroska";
import ObrazecNovStrosek from "./ObrazecNovStrosek";
import TabelaStroskov from "./TabelaStroskov";
import * as zaledje from "./zaledje";
import { urediKategorijeHierarhicno } from "./pomozneFunkcije";

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

  //ponovno nalozi stroske iz zaledja (po dodajanju, urejanju, brisanju)
  const osvezi = useCallback(() => {
    zaledje.naloziStroske(stran, VELIKOST_STRANI, izbranaKategorija)
      .then(prikaziStroske)
      .catch(obravnavajNapako);
  }, [stran, izbranaKategorija, prikaziStroske, obravnavajNapako]);

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

  const dodajStrosek = async (novStrosek) => {
    try {
      await zaledje.dodajStrosek(novStrosek);
      osvezi();
      return true;
    } catch (napaka) {
      obravnavajNapako(napaka);
      return false;
    }
  };

  const shraniUrejanje = async (posodobljenStrosek) => {
    try {
      await zaledje.posodobiStrosek(strosekZaUrejanje.id, posodobljenStrosek);
      setStrosekZaUrejanje(null);
      osvezi();
    } catch (napaka) {
      obravnavajNapako(napaka);
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
        <ObrazecNovStrosek kategorije={kategorije} naciniPlacila={naciniPlacila} obDodaj={dodajStrosek} />
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
    </div>
  );
}

export default StranStroski;
