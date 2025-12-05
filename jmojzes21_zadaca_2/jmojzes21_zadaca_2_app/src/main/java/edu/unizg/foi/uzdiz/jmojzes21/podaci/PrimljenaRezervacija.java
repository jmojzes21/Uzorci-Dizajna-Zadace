package edu.unizg.foi.uzdiz.jmojzes21.podaci;

import java.time.LocalDateTime;

public class PrimljenaRezervacija extends Rezervacija {

  public PrimljenaRezervacija(Korisnik korisnik, int oznakaAranzmana, LocalDateTime datumVrijeme) {
    super(korisnik, oznakaAranzmana, datumVrijeme);
  }

  public PrimljenaRezervacija(Rezervacija r) {
    super(r);
  }

  @Override
  public String vrsta() {
    return "Primljena";
  }

}
