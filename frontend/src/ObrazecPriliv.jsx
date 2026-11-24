import { useEffect, useState } from "react";
import * as zaledje from "./zaledje";
import { prikaziZnesek } from "./pomozneFunkcije";

//iz stevilke meseca (1-12) naredi slovensko ime, npr. 11 -> "november"
const imeMeseca = (mesec) => new Date(2000, mesec - 1).toLocaleString("sl-SI", { month: "long" });

//obrazec za vnos mesecnega priliva za tekoci mesec
//obShrani = funkcija iz StranStroski, ki po shranjevanju osvezi dnevni limit
function ObrazecPriliv({ obShrani }) {
  const [znesek, setZnesek] = useState(""); //kar uporabnik vpise v polje
  const [priliv, setPriliv] = useState(null); //odgovor backenda, null dokler se priliv ne nalozi
  const [sporocilo, setSporocilo] = useState(null); //napaka ali obvestilo (npr. priliv se ni vnesen), null ce ga ni

  //shrani priliv, pri napaki (npr. 400 za znesek <= 0) izpise sporocilo, prejsnji priliv pa ostane prikazan
  const shrani = async () => {
    try {
      const rezultat = await zaledje.shraniPriliv(Number(znesek));
      setPriliv(rezultat); //shranjen priliv se izpise pod gumbom
      setSporocilo(null); //ce je prej bila napaka, jo pobrisemo
      obShrani(); //stran ponovno nalozi dnevni limit, ker je odvisen od priliva
    } catch (napaka) {
      setSporocilo(napaka.message);
    }
  };

  //priliv nalozimo enkrat, ko se stran odpre, 404 pomeni da priliv se ni vnesen
  useEffect(() => {
    zaledje.naloziPriliv()
      .then(setPriliv)
      .catch((napaka) => setSporocilo(napaka.message));
  }, []);

  return (
    <div className="card mb-4" style={{ boxShadow: "30px 30px 18px rgba(0,0,0,0.05)", marginTop: "20px" }}>
      <div className="card-header">
        <h5 className="mb-0">Mesečni priliv</h5>
      </div>
      <div className="card-body">
        <div className="mb-3">
          <label className="form-label">Znesek (€)</label>
          <input
            type="number"
            step="0.01"
            placeholder="0.00"
            className="form-control"
            value={znesek}
            onChange={(e) => setZnesek(e.target.value)}
          />
        </div>
        <button onClick={shrani} className="btn btn-primary w-100">
          Shrani
        </button>
        {priliv && (
          <p className="mt-3 mb-0">
            Mesečni priliv za {imeMeseca(priliv.mesec)} {priliv.leto}: {prikaziZnesek(priliv.znesek)}
          </p>
        )}
        {sporocilo && (
          <p className="mt-3 mb-0 text-muted">
            {sporocilo}
          </p>
        )}
      </div>
    </div>
  );
}

export default ObrazecPriliv;
