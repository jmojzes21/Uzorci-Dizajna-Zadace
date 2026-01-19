package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;

public class RezervacijaPrimljena extends RezervacijaStanje {

  @Override
  public StanjeId dajId() {
    return StanjeId.primljena;
  }

  @Override
  public String dajNaziv() {
    return "Primljena";
  }

}
