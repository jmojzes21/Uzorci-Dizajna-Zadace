package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.AktivnaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;

public class KreatorAktivneRezervacije extends KreatorRezervacije {

  @Override
  protected Rezervacija napraviRezervaciju(Rezervacija r) {
    return new AktivnaRezervacija(r);
  }

}
