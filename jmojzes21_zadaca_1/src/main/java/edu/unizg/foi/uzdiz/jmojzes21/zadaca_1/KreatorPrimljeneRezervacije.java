package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.PrimljenaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;

public class KreatorPrimljeneRezervacije extends KreatorRezervacije {

  @Override
  protected Rezervacija napraviRezervaciju(Rezervacija r) {
    return new PrimljenaRezervacija(r);
  }
  
}
