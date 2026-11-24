//vse klice na backend (zaledje) imamo na enem mestu
const NASLOV_ZALEDJA = "http://localhost:8080";

const preberiSporociloNapake = async (odgovor) => {
  try {
    const telo = await odgovor.json();
    return telo.message ?? `Napaka ${odgovor.status}`;
  } catch {
    return `Napaka ${odgovor.status}`; //odgovor nima telesa
  }
};

const preberiTelo = async (odgovor) => {
  const besedilo = await odgovor.text();
  return besedilo ? JSON.parse(besedilo) : null; //brisanje ne vrne nicesar
};

const posljiZahtevo = async (pot, nastavitve = {}) => {
  let odgovor;
  try {
    odgovor = await fetch(NASLOV_ZALEDJA + pot, {
      headers: { "Content-Type": "application/json" },
      ...nastavitve,
    });
  } catch {
    throw new Error("Strežnik ni dosegljiv. Ali backend teče?");
  }
  if (!odgovor.ok) {
    const napaka = new Error(await preberiSporociloNapake(odgovor));
    napaka.status = odgovor.status; //status shranimo, da lahko klicatelj loci vrste napak (npr. 404 pri dnevnem limitu)
    throw napaka;
  }
  return preberiTelo(odgovor);
};

const posljiJson = (pot, metoda, podatki) =>
  posljiZahtevo(pot, { method: metoda, body: JSON.stringify(podatki) });

export const naloziKategorije = () => posljiZahtevo("/kategorija");
export const naloziNacinePlacila = () => posljiZahtevo("/nacin-placila");

//nalozi eno stran stroskov, po zelji filtrirano po kategoriji
export const naloziStroske = (stran, velikost, kategorijaId) => {
  let pot = `/strosek?page=${stran}&size=${velikost}`;
  if (kategorijaId) { //filter dodamo samo ce je kategorija izbrana
    pot += `&kategorijaId=${kategorijaId}`;
  }
  return posljiZahtevo(pot);
};

export const dodajStrosek = (strosek) => posljiJson("/strosek", "POST", strosek);
export const posodobiStrosek = (id, strosek) => posljiJson(`/strosek/${id}`, "PUT", strosek);
export const izbrisiStrosek = (id) => posljiZahtevo(`/strosek/${id}`, { method: "DELETE" });
//dnevni limit za danes, napaka 404 pomeni, da priliv za tekoci mesec se ni vnesen
export const naloziDnevniLimit = () => posljiZahtevo("/dnevni-limit");

export const naloziPriliv = () => posljiZahtevo("/priliv");

//shrani priliv za tekoči mesec, ce obstaja ga backend posodobi
export const shraniPriliv = (znesek) => posljiJson("/priliv", "PUT", {znesek});
