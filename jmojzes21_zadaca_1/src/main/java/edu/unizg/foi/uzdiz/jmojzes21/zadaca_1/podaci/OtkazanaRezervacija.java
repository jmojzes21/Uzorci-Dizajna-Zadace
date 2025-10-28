package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

public class OtkazanaRezervacija extends Rezervacija {

  public OtkazanaRezervacija(Rezervacija r) {
    super(r);
  }

  @Override
  public String vrsta() {
    return "Otkazana";
  }

}
