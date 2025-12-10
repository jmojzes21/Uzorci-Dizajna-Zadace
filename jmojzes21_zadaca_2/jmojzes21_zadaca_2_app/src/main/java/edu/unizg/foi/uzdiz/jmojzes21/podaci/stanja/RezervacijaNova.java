package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public class RezervacijaNova implements RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaPrimljena());
  }

  @Override
  public String dajNaziv() {
    return "Nova";
  }

}
