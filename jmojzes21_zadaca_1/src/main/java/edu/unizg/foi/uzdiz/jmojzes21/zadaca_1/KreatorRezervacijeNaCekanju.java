package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.RezervacijaNaCekanju;

public class KreatorRezervacijeNaCekanju extends KreatorRezervacije {

  @Override
  protected Rezervacija napraviRezervaciju(Rezervacija r) {
    return new RezervacijaNaCekanju(r);
  }

}
