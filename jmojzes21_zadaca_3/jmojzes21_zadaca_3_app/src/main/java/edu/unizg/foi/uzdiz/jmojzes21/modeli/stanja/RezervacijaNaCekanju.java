package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;

public class RezervacijaNaCekanju extends RezervacijaStanje {

  @Override
  public StanjeId dajId() {
    return StanjeId.naCekanju;
  }

  @Override
  public String dajNaziv() {
    return "Na čekanju";
  }

}
