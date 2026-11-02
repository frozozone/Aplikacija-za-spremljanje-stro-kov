import { useEffect, useState } from "react";
import Header from "./Header";

//nalozi eno stran stroskov iz zaledja, po zelji filtrirano po kategoriji
const naloziStroske = async (stran, velikost, kategorijaId) => {
  let url = `http://localhost:8080/strosek?page=${stran}&size=${velikost}`;
  if (kategorijaId) { //filter dodamo samo ce je kategorija izbrana
    url += `&kategorijaId=${kategorijaId}`;
  }
  const res = await fetch(url);
  if (!res.ok) {
    throw new Error(`Napaka pri nalaganju stroškov: ${res.status}`);
  }
  return res.json();
};

function App(){
  const [strosek, setStrosek] = useState([]);
  const [iskanje, setIskanje] =useState(""); //tekst, ki ga uporabnik vpise v iskalno polje
  const [naziv, setNaziv] = useState("");
  const [znesek, setZnesek] = useState("");
  const [strosekZaUrejanje, setStrosekZaUrejanje] = useState(null);
  const [pokažiModalZaUrejanje, setPokažiModalZaUrejanje] = useState(false);
  const [kategorije, setKategorije] = useState([]); //seznam vseh kategorij
  const [izbranaKategorija, setIzbranaKategorija]=useState(null); //null pomeni prikazi vse
  const [kategorijaStroska, setKategorijaStroska] = useState(""); //kategorija, ki jo izberemo v obrazcu za nov strosek

  const [stran, setStran]= useState(0); //trenutna stran, steje se od 0 naprej
  const velikost =10; //koliko stroskov se prikaze na eni strani
  const [steviloStrani, setSteviloStrani] = useState(0); //Koliko strani je vseh skupaj


   const danasnjiDatum = () => { //vrne danasnji datum v obliki LLLL-MM-DD
     const datum = new Date();
     const leto = datum.getFullYear();
     const mesec = String(datum.getMonth()+1).padStart(2,'0'); //+1 ker getmonth vrača 0-11 ne 0-12
     const dan = String(datum.getDate()).padStart(2,'0');
     return `${leto}-${mesec}-${dan}`;
   };

   const trenutniCas = () => {
     const datum = new Date();
     const ure = String(datum.getHours()).padStart(2,'0'); //.padStart(2,'0') da dobimo 01,09 namesto 1,9
     const minute = String(datum.getMinutes()).padStart(2,'0');
     return `${ure}:${minute}`;
   };
  
  const [datum, setDatum] = useState(danasnjiDatum());
  const [cas, setCas]= useState(trenutniCas());

  //Nalaganje kategorije ob zagonu
  useEffect(()=>{
    fetch("http://localhost:8080/kategorija")
    .then((res)=>res.json())
    .then((data)=>setKategorije(data))
    .catch((err)=>console.error("Napaka pri nalaganju kategorij:",err));
  }, []);

  const prikaziStroske = (data) => {
    setStrosek(data.content ?? []); //stroski za tabelo
    setSteviloStrani(data.totalPages ?? 0); //za napis "stran 1/4" in gumb Naslednja
  };

  const osvezi = () => { //ponovno nalozi stroske iz zaledja (po dodajanju, urejanju, brisanju)
    naloziStroske(stran, velikost, izbranaKategorija)
      .then(prikaziStroske)
      .catch((err) => console.error("Napaka pri osveževanju stroškov:", err));
  };

  //Refreshanje po tem ko izberemo kategorijo ali drugo stran
  useEffect(() => {
    naloziStroske(stran, velikost, izbranaKategorija)
      .then((data) => {
        setStrosek(data.content ?? []);
        setSteviloStrani(data.totalPages ?? 0);
      })
      .catch((err) => console.error("Napaka pri osveževanju stroškov:", err));
  }, [izbranaKategorija, stran]); //ko se spremeni kategorija ali stran, nalozimo stroske

  const dodajStrosek = async() =>{
    if (!kategorijaStroska) {
      alert("Izberi kategorijo! Ce strosek ne spada nikamor, izberi Drugo.");
      return;
    }
    const novStrosek={
      naziv: naziv,
      znesek: parseFloat(znesek),
      datum: datum,
      cas: cas,
      kategorija: { id: Number(kategorijaStroska) }
    }
    try{
      const x=await fetch("http://localhost:8080/strosek", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body:JSON.stringify(novStrosek)
      })

      if(!x.ok){
        throw new Error("Napaka pri dodajanju stroška")
      }

      osvezi();

      setNaziv("");
      setZnesek("");
      setKategorijaStroska("");
      setDatum(danasnjiDatum());
      setCas(trenutniCas());
    } catch (error){
      console.error("Napaka:", error);
    }
  }

  const izbrisiStrosek =(id) => {
    fetch("http://localhost:8080/strosek/" + id, { method: "DELETE" })
      .then((res) => {
        if (!res.ok) throw new Error("Napaka pri brisanju");
        osvezi();
      })
      .catch((err) => console.error(err));
  };

  const shraniUrejanje = async () => {
    if (!strosekZaUrejanje) return;
    const x = await fetch(`http://localhost:8080/strosek/${strosekZaUrejanje.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        naziv: strosekZaUrejanje.naziv,
        znesek: parseFloat(strosekZaUrejanje.znesek),
        datum: strosekZaUrejanje.datum,
        cas: strosekZaUrejanje.cas,
        kategorija: { id: strosekZaUrejanje.kategorija.id } //zaledje rabi samo id kategorije
      })
    });
    if (x.ok) {
      osvezi();
      setPokažiModalZaUrejanje(false);
      setStrosekZaUrejanje(null);
    }
  };

  const filtriraniStroski= strosek.filter((s) => //pokaze samo stroske, ki v nazivu vsebujejo iskani tekst
    s.naziv.toLowerCase().includes(iskanje.toLowerCase()) //useState dela tu ko vlki
  );

//-------------------------------------------------------------------------------------------//
 return (
    <div style={{ maxWidth: "1200px", margin: "0 auto", padding: "20px" }}>
      <Header />
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

            <select
              value={izbranaKategorija ?? ""}
              onChange={(e) => {
                const val = e.target.value;
                setIzbranaKategorija(val === "" ? null : Number(val));
                setStran(0); //ob menjavi kategorije gremo nazaj na prvo stran
                }}
              >
                <option value="">Vse kategorije</option>
                {kategorije.map((k) => (
                  <option key={k.id} value={k.id}>
                    {k.naziv}
                    </option>
                  ))}
              </select>
          </div>
          <div className="table-responsive" style={{ maxHeight: "500px", overflowY: "auto", boxShadow: "30px 30px 18px rgba(0,0,0,0.05)", borderRadius:"8px",border:"1px green" }}>
            <table className="table table-hover" style={{border:"1px"}}>
              <thead className="table" style={{ textAlign: "left", border:"1px" }}>
                <tr>
                  <th style={{ width: "250px", height: "40px", backgroundColor: "rgb(228, 230, 229)", border:"1px" }}>Naziv</th>
                  <th style={{ width: "120px", height: "40px", backgroundColor: "rgb(228, 230, 229)", border:"1px"}}>Strošek</th>
                  <th style={{ width: "150px", height: "40px", backgroundColor: "rgb(228, 230, 229)", border:"1px" }}>Datum</th>
                  <th style={{ width: "150px", height: "40px", backgroundColor: "rgb(228,230,229)", border:"1px" }}> Čas</th>
                  <th style={{ width: "100px", height: "40px", backgroundColor: "rgb(228, 230, 229)", border:"1px" }}></th>
                </tr>
              </thead>
              <tbody>
                {filtriraniStroski.map((s) => (
                  <tr key={s.id} style={{ textAlign: "left" }} onClick={() => { setStrosekZaUrejanje(s); setPokažiModalZaUrejanje(true); }}>
                    <td style={{ width: "250px" }}>{s.naziv}</td>
                    <td style={{ width: "120px" }}>{s.znesek}€</td>
                    <td style={{ width: "150px" }}>{s.datum ? new Date(s.datum).toLocaleDateString('sl-SI') : ''}</td>
                    <td style={{width: "150px"}}>{s.cas}</td>
                    <td className="text-end">
                      <button className="btn btn-sm btn-outline-danger" onClick={(e) => { e.stopPropagation(); izbrisiStrosek(s.id); }}>
                        Izbriši
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className="d-flex justify-content-between align-items-center mt-3">
            <button className="btn btn-secondary" disabled={stran <= 0} onClick={() => setStran(stran - 1)}>Prejšnja</button>
            <span>Stran {stran + 1} / {steviloStrani || 1}</span>
            <button className="btn btn-secondary" disabled={stran >= steviloStrani - 1 || steviloStrani === 0} onClick={() => setStran(stran +1)}>Naslednja</button>
          </div>
        </div>
        <div className="col-lg-4">
          <div className="card" style={{boxShadow: "30px 30px 18px rgba(0,0,0,0.05)"}}>
            <div className="card-header">
              <h5 className="mb-0">Nov strošek</h5>
            </div>
            <div className="card-body">
              <div className="mb-3">
                <label className="form-label">Naziv stroška</label>
                <input
                  type="text"
                  placeholder ="npr. Nova žoga"
                  className="form-control"
                  value={naziv}
                  onChange={(e) => setNaziv(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Znesek (€)</label>
                <input
                  type="number"
                  step="0.01"
                  min="0"
                  placeholder="0.00"
                  className="form-control"
                  value={znesek}
                  onChange={(e) => setZnesek(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Kategorija</label>
                <select
                  className="form-select"
                  value={kategorijaStroska}
                  onChange={(e) => setKategorijaStroska(e.target.value)}
                >
                  <option value="">Izberi kategorijo...</option>
                  {kategorije.map((k) => (
                    <option key={k.id} value={k.id}>{k.naziv}</option>
                  ))}
                </select>
              </div>
              <div className="mb-3">
                <label className="form-label">Datum</label>
                <input
                  type="date"
                  className="form-control"
                  value={datum}
                  onChange={(e) => setDatum(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Čas</label>
                <input
                  type="time"
                  className="form-control"
                  value={cas}
                  onChange={(e) => setCas(e.target.value)}
                />
              </div>
              <button
                onClick={dodajStrosek}
                className="btn btn-primary w-100"
              >
                Dodaj strošek
              </button>
            </div>
          </div>
        </div>
      </div>
      {pokažiModalZaUrejanje && (
        <>
          <div className="modal-backdrop fade show" onClick={() => setPokažiModalZaUrejanje(false)}></div>
          <div className="modal fade show" tabIndex={-1} style={{ display: "block" }} role="dialog" aria-modal="true">
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Uredi strošek</h5>
                  <button className="btn-close" onClick={() => setPokažiModalZaUrejanje(false)}></button>
                </div>
                <div className="modal-body">
                  <div className="mb-3">
                    <label className="form-label">Naziv</label>
                    <input
                      className="form-control"
                      value={strosekZaUrejanje?.naziv || ""}
                      onChange={(e) => setStrosekZaUrejanje({ ...strosekZaUrejanje, naziv: e.target.value })}
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Znesek (€)</label>
                    <input
                      type="number"
                      step="0.01"
                      className="form-control"
                      value={strosekZaUrejanje?.znesek || ""}
                      onChange={(e) => setStrosekZaUrejanje({ ...strosekZaUrejanje, znesek: e.target.value })}
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Kategorija</label>
                    <select
                      className="form-select"
                      value={strosekZaUrejanje?.kategorija?.id ?? ""}
                      onChange={(e) => setStrosekZaUrejanje({ ...strosekZaUrejanje, kategorija: { id: Number(e.target.value) } })}
                    >
                      {kategorije.map((k) => (
                        <option key={k.id} value={k.id}>{k.naziv}</option>
                      ))}
                    </select>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Datum</label>
                    <input
                      type="date"
                      className="form-control"
                      value={strosekZaUrejanje?.datum || ""}
                      onChange={(e) => setStrosekZaUrejanje({ ...strosekZaUrejanje, datum: e.target.value })}
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Čas</label>
                    <input
                      type="time"
                      className="form-control"
                      value={strosekZaUrejanje?.cas || ""}
                      onChange={(e) => setStrosekZaUrejanje({ ...strosekZaUrejanje, cas: e.target.value })}
                    />
                  </div>
                </div>
                <div className="modal-footer">
                  <button className="btn btn-secondary" onClick={() => setPokažiModalZaUrejanje(false)}>Prekliči</button>
                  <button className="btn btn-primary" onClick={shraniUrejanje}>Shrani spremembe</button>
                </div>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default App;