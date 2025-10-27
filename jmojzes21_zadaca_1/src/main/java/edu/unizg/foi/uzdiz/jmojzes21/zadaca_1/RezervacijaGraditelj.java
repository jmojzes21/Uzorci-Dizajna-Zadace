package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.time.LocalDateTime;

public class RezervacijaGraditelj {

  private final Rezervacija rezervacija;

  public RezervacijaGraditelj() {
    rezervacija = new Rezervacija();
  }

  public RezervacijaGraditelj setIme(String ime) {
    rezervacija.setIme(ime);
    return this;
  }

  public RezervacijaGraditelj setPrezime(String prezime) {
    rezervacija.setPrezime(prezime);
    return this;
  }

  public RezervacijaGraditelj setOznakaAranzmana(int oznakaAranzmana) {
    rezervacija.setOznakaAranzmana(oznakaAranzmana);
    return this;
  }

  public RezervacijaGraditelj setDatumVrijeme(LocalDateTime datumVrijeme) {
    rezervacija.setDatumVrijeme(datumVrijeme);
    return this;
  }

  public Rezervacija dajRezervaciju() {
    return rezervacija;
  }

}
