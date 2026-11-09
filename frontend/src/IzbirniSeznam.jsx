//spustni seznam za kategorije in nacine placila; moznosti so oblike { id, oznaka }
function IzbirniSeznam({ moznosti, vrednost, obSpremembi, prazenNapis }) {
  return (
    <select
      className="form-select"
      value={vrednost ?? ""}
      onChange={(e) => obSpremembi(e.target.value)}
    >
      {prazenNapis && <option value="">{prazenNapis}</option>}
      {moznosti.map((moznost) => (
        <option key={moznost.id} value={moznost.id}>{moznost.oznaka}</option>
      ))}
    </select>
  );
}

export default IzbirniSeznam;
