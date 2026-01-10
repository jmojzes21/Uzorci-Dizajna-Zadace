package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import java.time.LocalDateTime;

public class RezervacijaNaCekanju implements RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaPrimljena());
  }

  @Override
  public void aktiviraj(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaAktivna());
  }

  @Override
  public void staviNaCekanje(Rezervacija rezervacija) {}

  @Override
  public void odgodi(Rezervacija rezervacija) {}

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
    return StanjeId.naCekanju;
  }

  @Override
  public String dajNaziv() {
    return "Na čekanju";
  }

}
