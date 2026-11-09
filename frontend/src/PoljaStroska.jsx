import IzbirniSeznam from "./IzbirniSeznam";

function Polje({ oznaka, children }) {
  return (
    <div className="mb-3">
      <label className="form-label">{oznaka}</label>
      {children}
    </div>
  );
}

//polja, ki so enaka v obrazcu za nov strosek in v oknu za urejanje
function PoljaStroska({ podatki, obSpremembi, kategorije, naciniPlacila }) {
  return (
    <>
      <Polje oznaka="Naziv stroška">
        <input
          type="text"
          placeholder="npr. Nova žoga"
          className="form-control"
          value={podatki.naziv}
          onChange={(e) => obSpremembi("naziv", e.target.value)}
        />
      </Polje>
      <Polje oznaka="Znesek (€)">
        <input
          type="number"
          step="0.01"
          min="0"
          placeholder="0.00"
          className="form-control"
          value={podatki.znesek}
          onChange={(e) => obSpremembi("znesek", e.target.value)}
        />
      </Polje>
      <Polje oznaka="Kategorija">
        <IzbirniSeznam
          moznosti={kategorije}
          vrednost={podatki.kategorijaId}
          obSpremembi={(vrednost) => obSpremembi("kategorijaId", vrednost)}
          prazenNapis="Izberi kategorijo..."
        />
      </Polje>
      <Polje oznaka="Način plačila">
        <IzbirniSeznam
          moznosti={naciniPlacila}
          vrednost={podatki.nacinPlacilaId}
          obSpremembi={(vrednost) => obSpremembi("nacinPlacilaId", vrednost)}
          prazenNapis="Izberi način plačila..."
        />
      </Polje>
      <Polje oznaka="Datum">
        <input
          type="date"
          className="form-control"
          value={podatki.datum}
          onChange={(e) => obSpremembi("datum", e.target.value)}
        />
      </Polje>
      <Polje oznaka="Čas">
        <input
          type="time"
          className="form-control"
          value={podatki.cas}
          onChange={(e) => obSpremembi("cas", e.target.value)}
        />
      </Polje>
      <Polje oznaka="Opis (ni obvezen)">
        <textarea
          rows={2}
          maxLength={500}
          className="form-control"
          value={podatki.opis}
          onChange={(e) => obSpremembi("opis", e.target.value)}
        />
      </Polje>
    </>
  );
}

export default PoljaStroska;
