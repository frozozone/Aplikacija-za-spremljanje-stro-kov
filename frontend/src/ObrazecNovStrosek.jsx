import { useState } from "react";
import PoljaStroska from "./PoljaStroska";
import { najdiNapakoObrazca, obrazecVStrosek, prazenObrazecStroska } from "./pomozneFunkcije";

function ObrazecNovStrosek({ kategorije, naciniPlacila, obDodaj }) {
  const [podatki, setPodatki] = useState(prazenObrazecStroska);

  const posodobiPolje = (polje, vrednost) => {
    setPodatki((prejsnjiPodatki) => ({ ...prejsnjiPodatki, [polje]: vrednost }));
  };

  const dodajStrosek = async () => {
    const napaka = najdiNapakoObrazca(podatki);
    if (napaka) {
      alert(napaka);
      return;
    }
    const uspelo = await obDodaj(obrazecVStrosek(podatki));
    if (uspelo) {
      setPodatki(prazenObrazecStroska());
    }
  };

  return (
    <div className="card" style={{ boxShadow: "30px 30px 18px rgba(0,0,0,0.05)" }}>
      <div className="card-header">
        <h5 className="mb-0">Nov strošek</h5>
      </div>
      <div className="card-body">
        <PoljaStroska
          podatki={podatki}
          obSpremembi={posodobiPolje}
          kategorije={kategorije}
          naciniPlacila={naciniPlacila}
        />
        <button onClick={dodajStrosek} className="btn btn-primary w-100">
          Dodaj strošek
        </button>
      </div>
    </div>
  );
}

export default ObrazecNovStrosek;
