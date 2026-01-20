package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.IKomanda;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.KomandaKreator;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import java.util.List;

public class KomandaBP implements IKomanda {

  private final String odabir;

  public KomandaBP(String odabir) {
    this.odabir = odabir;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    switch (odabir) {
      case "A":
        obrisiSveAranzmane(agencija);
        break;
      case "R":
        obrisiSveRezervacije(agencija);
        break;
    }

  }

  private void obrisiSveAranzmane(TuristickaAgencija agencija) {
    agencija.obrisiSve();
  }

  private void obrisiSveRezervacije(TuristickaAgencija agencija) {
    List<Aranzman> aranzmani = agencija.dajAranzmane();
    for (var e : aranzmani) {
      e.obrisiSve();
    }
  }

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var uzorak = new RegexKomandeGraditelj()
          .dodajIzraz("odabir", "(A)|(R)")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "BP [A|R]";
        throw new NeispravnaKomandaGreska(opis);
      }

      String odabir = matcher.group("odabir");
      return new KomandaBP(odabir);
    }
  }

}
