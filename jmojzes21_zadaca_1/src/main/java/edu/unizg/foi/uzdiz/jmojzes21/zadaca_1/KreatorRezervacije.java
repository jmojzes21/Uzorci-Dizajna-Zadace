package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.time.LocalDateTime;

public abstract class KreatorRezervacije {

  public abstract Rezervacija napraviRezervaciju(Korisnik korisnik, int oznaka, LocalDateTime datumVrijeme);

  public abstract Rezervacija promijeniVrstu(Rezervacija r);

}
