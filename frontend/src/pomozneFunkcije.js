export const danasnjiDatum = () => { //vrne danasnji datum v obliki LLLL-MM-DD
  const datum = new Date();
  const leto = datum.getFullYear();
  const mesec = String(datum.getMonth() + 1).padStart(2, '0'); //+1 ker getMonth vraca 0-11
  const dan = String(datum.getDate()).padStart(2, '0');
  return `${leto}-${mesec}-${dan}`;
};

export const trenutniCas = () => {
  const datum = new Date();
  const ure = String(datum.getHours()).padStart(2, '0'); //.padStart(2,'0') da dobimo 01,09 namesto 1,9
  const minute = String(datum.getMinutes()).padStart(2, '0');
  return `${ure}:${minute}`;
};

export const prikaziZnesek = (znesek) => `${Number(znesek).toFixed(2)} €`;

const ZAMIK_PODKATEGORIJE = "\u00A0\u00A0\u00A0"; //navadne presledke brskalnik v <option> skrci, nedeljive pa ne

//glavne kategorije ostanejo po abecedi, pod vsako pa so zamaknjene njene podkategorije
export const urediKategorijeHierarhicno = (kategorije) => {
  const glavneKategorije = kategorije.filter((k) => !k.nadrejena);
  return glavneKategorije.flatMap((glavna) => [
    { id: glavna.id, oznaka: glavna.naziv },
    ...kategorije
      .filter((k) => k.nadrejena?.id === glavna.id)
      .map((podkategorija) => ({ id: podkategorija.id, oznaka: `${ZAMIK_PODKATEGORIJE}↳ ${podkategorija.naziv}` })),
  ]);
};

export const prazenObrazecStroska = () => ({
  naziv: "",
  znesek: "",
  kategorijaId: "",
  nacinPlacilaId: "",
  datum: danasnjiDatum(),
  cas: trenutniCas(),
  opis: "",
});

//strosek iz backenda pretvori v obliko, ki jo uporabljajo polja obrazca
export const strosekVObrazec = (strosek) => ({
  naziv: strosek.naziv ?? "",
  znesek: strosek.znesek ?? "",
  kategorijaId: strosek.kategorija?.id ?? "",
  nacinPlacilaId: strosek.nacinPlacila?.id ?? "",
  datum: strosek.datum ?? "",
  cas: strosek.cas ?? "",
  opis: strosek.opis ?? "",
});

//podatke iz obrazca pretvori v obliko, ki jo pricakuje backend (za kategorijo in nacin placila je dovolj id)
export const obrazecVStrosek = (podatki) => ({
  naziv: podatki.naziv,
  znesek: parseFloat(podatki.znesek),
  datum: podatki.datum,
  cas: podatki.cas || null,
  opis: podatki.opis.trim() || null,
  kategorija: { id: Number(podatki.kategorijaId) },
  nacinPlacila: { id: Number(podatki.nacinPlacilaId) },
});

//vrne sporocilo napake ali null, ce je obrazec v redu (ostala polja preveri backend)
export const najdiNapakoObrazca = (podatki) => {
  if (!podatki.kategorijaId) {
    return "Izberi kategorijo! Če strošek ne spada nikamor, izberi Drugo.";
  }
  if (!podatki.nacinPlacilaId) {
    return "Izberi način plačila!";
  }
  return null;
};
