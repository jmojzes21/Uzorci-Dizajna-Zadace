package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

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
  public Rezervacija napraviRezervaciju(String ime, String prezime, int oznaka, LocalDateTime datumVrijeme) {
    return new OtkazanaRezervacija(ime, prezime, oznaka, datumVrijeme, datumVrijemeOtkaza);
  }

  @Override
  public Rezervacija promijeniVrstu(Rezervacija r) {
    return new OtkazanaRezervacija(r, datumVrijemeOtkaza);
  }

}
