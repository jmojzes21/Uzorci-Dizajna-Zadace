package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava.NacinSortiranja;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;

public class KomandaIP {

  public KomandaIP() {}

  public void obradiKomandu(String args) throws Exception {

    var postavke = PostavkeSustava.dajInstancu();

    switch (args) {
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

}
