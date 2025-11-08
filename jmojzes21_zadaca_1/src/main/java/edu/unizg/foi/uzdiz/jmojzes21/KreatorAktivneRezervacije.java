package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.AktivnaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import java.time.LocalDateTime;

public class KreatorAktivneRezervacije extends KreatorRezervacije {

  @Override
  public Rezervacija napraviRezervaciju(Korisnik korisnik, int oznaka, LocalDateTime datumVrijeme) {
    return new AktivnaRezervacija(korisnik, oznaka, datumVrijeme);
  }

  @Override
  public Rezervacija promijeniVrstu(Rezervacija r) {
    if (r instanceof AktivnaRezervacija) {return r;}
    return new AktivnaRezervacija(r);
  }

}
