package edu.unizg.foi.uzdiz.jmojzes21.pomocnici;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import java.util.ArrayList;
import java.util.List;

public class StanjeRezervacijeParser {

  public List<StanjeId> parsiraj(String tekst) {

    boolean prikaziPrimljeneAktivne = tekst.contains("PA");
    boolean prikaziNaCekanju = tekst.contains("Č");
    boolean prikaziOdgodjene = false;
    if (tekst.contains("OD")) {
      prikaziOdgodjene = true;
      tekst = tekst.replace("OD", "");
    }
    boolean prikaziOtkazane = tekst.contains("O");

    List<StanjeId> stanja = new ArrayList<>();

    if (prikaziPrimljeneAktivne) {
      stanja.add(StanjeId.primljena);
      stanja.add(StanjeId.aktivna);
    }

    if (prikaziNaCekanju) {
      stanja.add(StanjeId.naCekanju);
    }

    if (prikaziOdgodjene) {
      stanja.add(StanjeId.odgodjena);
    }

    if (prikaziOtkazane) {
      stanja.add(StanjeId.otkazana);
    }

    return stanja;
  }

}

