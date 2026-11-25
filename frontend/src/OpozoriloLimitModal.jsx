import { prikaziZnesek } from "./pomozneFunkcije";

function OpozoriloLimitModal({ presezenoDanes, presezenoTaMesec, obShraniVseeno, obPreklici }) {
  return (
    <>
      <div className="modal-backdrop fade show"></div>
      <div className="modal fade show" tabIndex={-1} style={{ display: "block" }} role="dialog" aria-modal="true">
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">Presežen dnevni limit</h5>
              <button className="btn-close" onClick={obPreklici} aria-label="Prekliči"></button>
            </div>
            <div className="modal-body">
              <p>Ta vnos presega dnevni limit za {presezenoDanes}.</p>
              {presezenoTaMesec > 0 && (
                <p className="mb-0 text-danger">
                  Ta mesec si priliv presegel za {prikaziZnesek(presezenoTaMesec)}.
                </p>
              )}
            </div>
            <div className="modal-footer">
              <button className="btn btn-secondary" onClick={obPreklici}>Prekliči</button>
              <button className="btn btn-primary" onClick={obShraniVseeno}>Shrani vseeno</button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default OpozoriloLimitModal;
