package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;

public class RezervacijaNova extends RezervacijaStanje {

  @Override
  public StanjeId dajId() {
    return StanjeId.nova;
  }

  @Override
  public String dajNaziv() {
    return "Nova";
  }

}
