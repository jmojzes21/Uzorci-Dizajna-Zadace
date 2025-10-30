package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.RezervacijaNaCekanju;
import java.time.LocalDateTime;

public class KreatorRezervacijeNaCekanju extends KreatorRezervacije {

  @Override
  protected Rezervacija napraviRezervaciju(String ime, String prezime, int oznaka, LocalDateTime datumVrijeme) {
    return new RezervacijaNaCekanju(ime, prezime, oznaka, datumVrijeme);
  }

  @Override
  protected Rezervacija promijeniVrstu(Rezervacija r) {
    return new RezervacijaNaCekanju(r);
  }
  
}
