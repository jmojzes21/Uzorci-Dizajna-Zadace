package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import java.util.List;

public class KomandaBP {

  private final TuristickaAgencija agencija;

  public KomandaBP(TuristickaAgencija agencija) {
    this.agencija = agencija;
  }

  public void obradiKomandu(String args) throws Exception {

    var uzorak = new RegexKomandeGraditelj()
        .dodajIzraz("odabir", "(A)|(R)")
        .dajUzorak();

    var matcher = uzorak.matcher(args);
    if (!matcher.matches()) {
      String opis = "BP [A|R]";
      throw new NeispravnaKomandaGreska(opis);
    }

    String odabir = matcher.group("odabir");
    switch (odabir) {
      case "A":
        obrisiSveAranzmane();
        break;
      case "R":
        obrisiSveRezervacije();
        break;
    }

  }

  private void obrisiSveAranzmane() {
    agencija.obrisiSve();
  }

  private void obrisiSveRezervacije() {
    List<Aranzman> aranzmani = agencija.dajAranzmane();
    for (var e : aranzmani) {
      e.obrisiSve();
    }
  }

}
