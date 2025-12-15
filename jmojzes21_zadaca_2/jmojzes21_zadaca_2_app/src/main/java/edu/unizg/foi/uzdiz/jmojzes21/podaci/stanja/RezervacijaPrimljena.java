package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija.StanjeId;
import java.time.LocalDateTime;

public class RezervacijaPrimljena implements RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {}

  @Override
  public void aktiviraj(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaAktivna());
  }

  @Override
  public void staviNaCekanje(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaNaCekanju());
  }

  @Override
  public void odgodi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaOdgodjena());
  }

  @Override
  public void otkazi(Rezervacija rezervacija) {
    rezervacija.setVrijemeOtkaza(LocalDateTime.now());
    rezervacija.postaviStanje(new RezervacijaOtkazana());
  }

  @Override
  public boolean kadaRezervacijaPostajeAktivna(Rezervacija trenutna, Rezervacija postajeAktivna) {
    return true;
  }

  @Override
  public void kadaRezervacijaPostalaAktivna(Rezervacija trenutna, Rezervacija aktivirana) {}

  @Override
  public void kadaRezervacijaPostalaOtkazana(Rezervacija trenutna, Rezervacija otkazana) {}

  @Override
  public StanjeId dajId() {
    return StanjeId.primljena;
  }

  @Override
  public String dajNaziv() {
    return "Primljena";
  }

}
