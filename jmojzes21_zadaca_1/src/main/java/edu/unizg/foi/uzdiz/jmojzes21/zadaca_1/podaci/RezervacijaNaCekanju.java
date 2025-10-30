package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

import java.time.LocalDateTime;

public class RezervacijaNaCekanju extends Rezervacija {

  public RezervacijaNaCekanju(String ime, String prezime, int oznakaAranzmana, LocalDateTime datumVrijeme) {
    super(ime, prezime, oznakaAranzmana, datumVrijeme);
  }

  public RezervacijaNaCekanju(Rezervacija r) {
    super(r);
  }

  @Override
  public String vrsta() {
    return "Na čekanju";
  }

}
