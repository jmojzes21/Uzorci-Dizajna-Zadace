package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.RezervacijaNaCekanju;
import java.time.LocalDateTime;

public class KreatorRezervacijeNaCekanju extends KreatorRezervacije {

  @Override
  public Rezervacija napraviRezervaciju(Korisnik korisnik, int oznaka, LocalDateTime datumVrijeme) {
    return new RezervacijaNaCekanju(korisnik, oznaka, datumVrijeme);
  }

  @Override
  public Rezervacija promijeniVrstu(Rezervacija r) {
    return new RezervacijaNaCekanju(r);
  }

}
