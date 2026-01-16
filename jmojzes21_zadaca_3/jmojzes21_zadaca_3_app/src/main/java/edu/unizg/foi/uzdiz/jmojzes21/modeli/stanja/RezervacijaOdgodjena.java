package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import java.time.LocalDateTime;

public class RezervacijaOdgodjena extends RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaPrimljena());
  }

  @Override
  public void aktiviraj(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaAktivna());
  }

  @Override
  public void staviNaCekanje(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaNaCekanju());
  }


  @Override
  public void otkazi(Rezervacija rezervacija) {
    rezervacija.setVrijemeOtkaza(LocalDateTime.now());
    rezervacija.postaviStanje(new RezervacijaOtkazana());
  }

  @Override
  public void kadaRezervacijaNijeViseAktivna(Rezervacija trenutna, Rezervacija nijeAktivna) {

    if (trenutna.korisnik().equals(nijeAktivna.korisnik())) {
      Aranzman aranzman = trenutna.dajAranzman();
      aranzman.aktivirajRezervaciju(trenutna);
    }

  }

  @Override
  public StanjeId dajId() {
    return StanjeId.odgodjena;
  }

  @Override
  public String dajNaziv() {
    return "Odgođena";
  }

}
