package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

import java.time.LocalDateTime;

public class AktivnaRezervacija extends Rezervacija {

  public AktivnaRezervacija(String ime, String prezime, int oznakaAranzmana, LocalDateTime datumVrijeme) {
    super(ime, prezime, oznakaAranzmana, datumVrijeme);
  }

  public AktivnaRezervacija(Rezervacija r) {
    super(r);
  }

  @Override
  public String vrsta() {
    return "Aktivna";
  }

}
