import { useState } from "react";
import PoljaStroska from "./PoljaStroska";
import { najdiNapakoObrazca, obrazecVStrosek, strosekVObrazec } from "./pomozneFunkcije";

function ModalUrejanjeStroska({ strosek, kategorije, naciniPlacila, obShrani, obZapri }) {
  //modal se ob vsakem odprtju ustvari na novo, zato zacetne podatke preberemo samo enkrat
  const [podatki, setPodatki] = useState(() => strosekVObrazec(strosek));

  const posodobiPolje = (polje, vrednost) => {
    setPodatki((prejsnjiPodatki) => ({ ...prejsnjiPodatki, [polje]: vrednost }));
  };

  const shraniSpremembe = () => {
    const napaka = najdiNapakoObrazca(podatki);
    if (napaka) {
      alert(napaka);
      return;
    }
    obShrani(obrazecVStrosek(podatki));
  };

  return (
    <>
      <div className="modal-backdrop fade show" onClick={obZapri}></div>
      <div className="modal fade show" tabIndex={-1} style={{ display: "block" }} role="dialog" aria-modal="true">
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">Uredi strošek</h5>
              <button className="btn-close" onClick={obZapri}></button>
            </div>
            <div className="modal-body">
              <PoljaStroska
                podatki={podatki}
                obSpremembi={posodobiPolje}
                kategorije={kategorije}
                naciniPlacila={naciniPlacila}
              />
            </div>
            <div className="modal-footer">
              <button className="btn btn-secondary" onClick={obZapri}>Prekliči</button>
              <button className="btn btn-primary" onClick={shraniSpremembe}>Shrani spremembe</button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default ModalUrejanjeStroska;
