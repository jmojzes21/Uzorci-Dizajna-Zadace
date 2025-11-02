package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.OtkazanaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.time.LocalDateTime;

public class KreatorOtkazaneRezervacije extends KreatorRezervacije {

  private final LocalDateTime datumVrijemeOtkaza;

  public KreatorOtkazaneRezervacije(LocalDateTime datumVrijemeOtkaza) {
    this.datumVrijemeOtkaza = datumVrijemeOtkaza;
  }

  public KreatorOtkazaneRezervacije() {
    datumVrijemeOtkaza = LocalDateTime.now();
  }

  @Override
  public Rezervacija napraviRezervaciju(Korisnik korisnik, int oznaka, LocalDateTime datumVrijeme) {
    return new OtkazanaRezervacija(korisnik, oznaka, datumVrijeme, datumVrijemeOtkaza);
  }

  @Override
  public Rezervacija promijeniVrstu(Rezervacija r) {
    return new OtkazanaRezervacija(r, datumVrijemeOtkaza);
  }

}
