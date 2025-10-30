package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

import java.time.LocalDateTime;

public class PrimljenaRezervacija extends Rezervacija {

  public PrimljenaRezervacija(String ime, String prezime, int oznakaAranzmana, LocalDateTime datumVrijeme) {
    super(ime, prezime, oznakaAranzmana, datumVrijeme);
  }

  public PrimljenaRezervacija(Rezervacija r) {
    super(r);
  }

  @Override
  public String vrsta() {
    return "Primljena";
  }

}
