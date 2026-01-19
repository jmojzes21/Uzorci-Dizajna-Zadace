package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import java.time.LocalDateTime;

public abstract class RezervacijaStanje {

  public void zaprimi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaPrimljena());
  }

  public void aktiviraj(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaAktivna());
  }

  public void staviNaCekanje(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaNaCekanju());
  }

  public void odgodi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaOdgodjena());
  }

  public void otkazi(Rezervacija rezervacija) {
    rezervacija.setVrijemeOtkaza(LocalDateTime.now());
    rezervacija.postaviStanje(new RezervacijaOtkazana());
  }

  public void kadaRezervacijaPostalaAktivna(Rezervacija trenutna, Rezervacija aktivirana) {}

  public void kadaRezervacijaNijeViseAktivna(Rezervacija trenutna, Rezervacija nijeAktivna) {}

  public abstract StanjeId dajId();

  public abstract String dajNaziv();

}
