package edu.unizg.foi.uzdiz.jmojzes21.podaci;

import java.time.LocalDateTime;

public class RezervacijaNaCekanju extends Rezervacija {

  public RezervacijaNaCekanju(Korisnik korisnik, int oznakaAranzmana, LocalDateTime datumVrijeme) {
    super(korisnik, oznakaAranzmana, datumVrijeme);
  }

  public RezervacijaNaCekanju(Rezervacija r) {
    super(r);
  }

  @Override
  public String vrsta() {
    return "Na čekanju";
  }

}
