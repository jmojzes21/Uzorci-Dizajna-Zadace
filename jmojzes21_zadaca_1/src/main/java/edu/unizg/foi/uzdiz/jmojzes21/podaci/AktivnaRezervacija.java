package edu.unizg.foi.uzdiz.jmojzes21.podaci;

import java.time.LocalDateTime;

public class AktivnaRezervacija extends Rezervacija {

  public AktivnaRezervacija(Korisnik korisnik, int oznakaAranzmana, LocalDateTime datumVrijeme) {
    super(korisnik, oznakaAranzmana, datumVrijeme);
  }

  public AktivnaRezervacija(Rezervacija r) {
    super(r);
  }

  @Override
  public String vrsta() {
    return "Aktivna";
  }

}
