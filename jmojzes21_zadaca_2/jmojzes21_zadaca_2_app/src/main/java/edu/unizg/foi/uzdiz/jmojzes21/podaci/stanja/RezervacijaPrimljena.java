package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public class RezervacijaPrimljena implements RezervacijaStanje {

  @Override
  public void aktiviraj(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaAktivna());
  }

  @Override
  public void staviNaCekanje(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaNaCekanju());
  }

  @Override
  public String dajNaziv() {
    return "Primljena";
  }

}
