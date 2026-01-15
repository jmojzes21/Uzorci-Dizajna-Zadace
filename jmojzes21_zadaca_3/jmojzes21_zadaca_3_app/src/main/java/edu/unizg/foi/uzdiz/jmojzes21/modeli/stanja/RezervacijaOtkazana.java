package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;

public class RezervacijaOtkazana extends RezervacijaStanje {

  @Override
  public StanjeId dajId() {
    return StanjeId.otkazana;
  }

  @Override
  public String dajNaziv() {
    return "Otkazana";
  }

}
