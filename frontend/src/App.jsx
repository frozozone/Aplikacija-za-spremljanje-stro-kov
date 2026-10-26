import { useEffect, useState } from "react";

function App () {
  const [strosek, setStrosek] = useState([]);
  const [search, setSearch] = useState("");
  const [naziv, setNaziv] = useState("");
  const [znesek, setZnesek] = useState("");
  const [strosekZaUrejanje, setStrosekZaUrejanje] = useState(null);
  const [pokažiModalZaUrejanje, setPokažiModalZaUrejanje] = useState(false);

  const [page, setPage]= useState(0);
  const [size, setSize]= useState(10);
  const [sort, setSort]= useState("datum,desc");

  
   const danasnjiDatum = () => {
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

  useEffect(() => {
    fetch("http://localhost:8080/strosek")
      .then((res) => res.json())
      .then((data) => setStrosek(data))
      .catch((err) => console.error("Napaka:", err));
  }, []);

  const dodajStrosek = async() =>{
    const novStrosek={
      naziv: naziv,
      znesek: parseFloat(znesek),
      datum: datum,
      cas: cas
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
      setDatum(danasnjiDatum());
      setCas(trenutniCas());
    } catch (error){
      console.error("Napaka:", error);
    }
  }
  const osvezi = () => {
    fetch("http://localhost:8080/strosek")
      .then((res) => res.json())
      .then((data) => setStrosek(data));
  };

  const izbrisiStrosek = (id) => {
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
        cas: strosekZaUrejanje.cas
      })
    });
    if (x.ok) {
      osvezi();
      setPokažiModalZaUrejanje(false);
      setStrosekZaUrejanje(null);
    }
  };

  const filteredStrosek = strosek.filter((s) =>
    s.naziv.toLowerCase().includes(search.toLowerCase())
  );


  return (
    <div style={{ maxWidth: "1200px", margin: "0 auto", padding: "20px" }}>
      <nav className="navbar navbar-dark justify-content-center mb-4 r-2" style={{ backgroundColor: "rgb(74, 80, 77)", borderRadius:"5px"}}>
        <div className="container-fluid justify-content-center">
          <span className="navbar-brand mb-0 h1">Spremljanje stroškov</span>
        </div>
      </nav>
      <div className="row g-4">
        <div className="col-lg-8">
          <div className="mb-3">
            <input
              type="text"
              placeholder="Išči po nazivu..."
              className="form-control"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
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
                {filteredStrosek.map((s) => (
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