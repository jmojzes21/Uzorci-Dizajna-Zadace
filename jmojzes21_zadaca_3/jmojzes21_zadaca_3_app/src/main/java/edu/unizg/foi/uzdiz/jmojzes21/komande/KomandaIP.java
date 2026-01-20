package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava.NacinSortiranja;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.IKomanda;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.KomandaKreator;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;

public class KomandaIP implements IKomanda {

  private final String odabir;

  public KomandaIP(String odabir) {
    this.odabir = odabir;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    var postavke = PostavkeSustava.dajInstancu();

    switch (odabir) {
      case "N":
        postavke.postaviNacinSortiranja(NacinSortiranja.uzlazno);
        break;
      case "S":
        postavke.postaviNacinSortiranja(NacinSortiranja.silazno);
        break;
      default:
        String opis = "IP [N|S]";
        throw new NeispravnaKomandaGreska(opis);
    }

  }

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {
      return new KomandaIP(args);
    }
  }

}
