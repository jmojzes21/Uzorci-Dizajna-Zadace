package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.AktivnaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.time.LocalDateTime;

public class KreatorAktivneRezervacije extends KreatorRezervacije {

  @Override
  public Rezervacija napraviRezervaciju(String ime, String prezime, int oznaka, LocalDateTime datumVrijeme) {
    return new AktivnaRezervacija(ime, prezime, oznaka, datumVrijeme);
  }

  @Override
  public Rezervacija promijeniVrstu(Rezervacija r) {
    return new AktivnaRezervacija(r);
  }

}
