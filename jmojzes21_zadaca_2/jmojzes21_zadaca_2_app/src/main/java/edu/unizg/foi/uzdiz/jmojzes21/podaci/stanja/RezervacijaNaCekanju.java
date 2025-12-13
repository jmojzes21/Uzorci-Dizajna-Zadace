package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public class RezervacijaNaCekanju implements RezervacijaStanje {

  @Override
  public void aktiviraj(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaAktivna());
  }

  @Override
  public String dajNaziv() {
    return "Na čekanju";
  }

}
