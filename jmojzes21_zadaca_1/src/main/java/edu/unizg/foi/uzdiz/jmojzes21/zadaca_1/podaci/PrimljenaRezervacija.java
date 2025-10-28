package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

public class PrimljenaRezervacija extends Rezervacija {

  public PrimljenaRezervacija(Rezervacija r) {
    super(r);
  }

  @Override
  public String vrsta() {
    return "Primljena";
  }

}
