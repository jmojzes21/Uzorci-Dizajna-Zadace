package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.PrimljenaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.time.LocalDateTime;

public class KreatorPrimljeneRezervacije extends KreatorRezervacije {

  @Override
  public Rezervacija napraviRezervaciju(Korisnik korisnik, int oznaka, LocalDateTime datumVrijeme) {
    return new PrimljenaRezervacija(korisnik, oznaka, datumVrijeme);
  }

  @Override
  public Rezervacija promijeniVrstu(Rezervacija r) {
    if (r instanceof PrimljenaRezervacija) {return r;}
    return new PrimljenaRezervacija(r);
  }

}
