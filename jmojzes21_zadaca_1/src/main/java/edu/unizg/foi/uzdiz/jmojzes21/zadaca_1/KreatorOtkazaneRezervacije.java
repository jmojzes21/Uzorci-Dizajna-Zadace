package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.OtkazanaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;

public class KreatorOtkazaneRezervacije extends KreatorRezervacije {

  @Override
  protected Rezervacija napraviRezervaciju(Rezervacija r) {
    return new OtkazanaRezervacija(r);
  }

}
