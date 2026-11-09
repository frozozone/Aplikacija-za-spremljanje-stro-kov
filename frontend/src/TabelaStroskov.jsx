import { prikaziZnesek } from "./pomozneFunkcije";

const slogGlave = { height: "40px", backgroundColor: "rgb(228, 230, 229)", border: "1px" };

function TabelaStroskov({ stroski, obIzberi, obIzbrisi }) {
  return (
    <div className="table-responsive" style={{ maxHeight: "500px", overflowY: "auto", boxShadow: "30px 30px 18px rgba(0,0,0,0.05)", borderRadius: "8px", border: "1px green" }}>
      <table className="table table-hover" style={{ border: "1px" }}>
        <thead className="table" style={{ textAlign: "left", border: "1px" }}>
          <tr>
            <th style={{ ...slogGlave, width: "220px" }}>Naziv</th>
            <th style={{ ...slogGlave, width: "170px" }}>Kategorija</th>
            <th style={{ ...slogGlave, width: "110px" }}>Strošek</th>
            <th style={{ ...slogGlave, width: "120px" }}>Datum</th>
            <th style={{ ...slogGlave, width: "80px" }}>Čas</th>
            <th style={{ ...slogGlave, width: "150px" }}>Plačilo</th>
            <th style={{ ...slogGlave, width: "100px" }}></th>
          </tr>
        </thead>
        <tbody>
          {stroski.map((s) => (
            <tr key={s.id} style={{ textAlign: "left" }} onClick={() => obIzberi(s)} title={s.opis ?? ""}>
              <td>{s.naziv}</td>
              <td>{s.kategorija?.naziv}</td>
              <td>{prikaziZnesek(s.znesek)}</td>
              <td>{s.datum ? new Date(s.datum).toLocaleDateString('sl-SI') : ''}</td>
              <td>{s.cas?.slice(0, 5)}</td>
              <td>{s.nacinPlacila?.naziv}</td>
              <td className="text-end">
                <button className="btn btn-sm btn-outline-danger" onClick={(e) => { e.stopPropagation(); obIzbrisi(s.id); }}>
                  Izbriši
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default TabelaStroskov;
