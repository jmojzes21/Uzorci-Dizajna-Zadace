package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

public class RezervacijaNaCekanju extends Rezervacija {

  public RezervacijaNaCekanju(Rezervacija r) {
    super(r);
  }

  @Override
  public String vrsta() {
    return "Na čekanju";
  }

}
