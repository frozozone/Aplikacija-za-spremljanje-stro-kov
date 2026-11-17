import { prikaziZnesek } from "./pomozneFunkcije";

//kartica samo prikazuje podatke, ki jih dobi iz StranStroski (nalaganje in osvezevanje je tam)
//dnevniLimit = odgovor GET /dnevni-limit, napaka = napaka iz zaledja (ima message in status)

//glavno sporocilo kartice, vrstni red preverjanja je pomemben:
//preseganje meseca je resnejse od preseganja danes, zato ga preverimo prvega
function GlavnoSporocilo({ dnevniLimit }) {
    if (dnevniLimit.presezenoTaMesec > 0) {
        return <p className="fs-5 fw-bold text-danger mb-3">Ta mesec si priliv presegel za {prikaziZnesek(dnevniLimit.presezenoTaMesec)}</p>;
    }
    if (dnevniLimit.presezenoDanes > 0) {
        return <p className="fs-5 fw-bold text-danger mb-3">Za danes si limit presegel za {prikaziZnesek(dnevniLimit.presezenoDanes)}</p>;
    }
    return <p className="fs-5 fw-bold text-success mb-3">Danes lahko porabiš še {prikaziZnesek(dnevniLimit.danesLahkoPorabisSe)}</p>;
}

//seznam podrobnosti pod glavnim sporocilom: limit, preostali znesek in preostali dnevi
function Podrobnosti({ dnevniLimit }) {
    const presezenMesec = dnevniLimit.presezenoTaMesec > 0;
    //zaledje preostalega zneska ne vrne, zato ga izracunamo tukaj
    const preostaliZnesek = dnevniLimit.priliv - dnevniLimit.porabljenoTaMesec;
    return (
        <ul className="list-unstyled mb-0 small">
            {!presezenMesec && ( //ko je priliv presezen, limita in preostalega zneska ne kazemo (ne sme pisati 0 €)
                <>
                    <li className="d-flex justify-content-between"><span>Dnevni limit</span><span>{prikaziZnesek(dnevniLimit.dnevniLimit)}</span></li>
                    <li className="d-flex justify-content-between"><span>Preostalo ta mesec</span><span>{prikaziZnesek(preostaliZnesek)}</span></li>
                </>
            )}
            <li className="d-flex justify-content-between"><span>Preostali dnevi (z današnjim)</span><span>{dnevniLimit.preostaliDnevi}</span></li>
        </ul>
    );
}

//izbere, kaj kartica pokaze: napako, nalaganje ali podatke
function VsebinaKartice({ dnevniLimit, napaka }) {
    if (napaka?.status === 404) { //zaledje vrne 404, ko priliv za ta mesec se ni vnesen, kar ni prava napaka
        return <p className="mb-0 text-muted">{napaka.message}</p>;
    }
    if (napaka) { //vse ostale napake (npr. backend ne tece)
        return <p className="mb-0 text-danger">Dnevnega limita ni mogoče naložiti: {napaka.message}</p>;
    }
    if (!dnevniLimit) { //odgovor zaledja se ni prispel
        return <p className="mb-0 text-muted">Nalaganje ...</p>;
    }
    return (
        <>
            <GlavnoSporocilo dnevniLimit={dnevniLimit} />
            <Podrobnosti dnevniLimit={dnevniLimit} />
        </>
    );
}

//okvir kartice (isti slog kot ostale kartice v aplikaciji)
function KarticaDnevniLimit({ dnevniLimit, napaka }) {
    return (
        <div className="card mb-4" style={{ boxShadow: "30px 30px 18px rgba(0,0,0,0.05)" }}>
            <div className="card-header">
                <h5 className="mb-0">Dnevni limit</h5>
            </div>
            <div className="card-body">
                <VsebinaKartice dnevniLimit={dnevniLimit} napaka={napaka} />
            </div>
        </div>
    );
}

export default KarticaDnevniLimit;