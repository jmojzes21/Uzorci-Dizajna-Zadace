package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public class AranzmanPopunjen implements AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {
    aranzman.dodaj(rezervacija);
    rezervacija.zaprimi();
    rezervacija.staviNaCekanje();
  }

  @Override
  public String dajNaziv() {
    return "Popunjen";
  }

}
