package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.PrimljenaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.time.LocalDateTime;

public class KreatorPrimljeneRezervacije extends KreatorRezervacije {

  @Override
  public Rezervacija napraviRezervaciju(String ime, String prezime, int oznaka, LocalDateTime datumVrijeme) {
    return new PrimljenaRezervacija(ime, prezime, oznaka, datumVrijeme);
  }

  @Override
  public Rezervacija promijeniVrstu(Rezervacija r) {
    return new PrimljenaRezervacija(r);
  }

}
