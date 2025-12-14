package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public class RezervacijaOdgodjena implements RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {}

  @Override
  public void aktiviraj(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaAktivna());
  }

  @Override
  public void staviNaCekanje(Rezervacija rezervacija) {}

  @Override
  public void odgodi(Rezervacija rezervacija) {}

  @Override
  public void otkazi(Rezervacija rezervacija) {}

  @Override
  public boolean kadaRezervacijaPostajeAktivna(Rezervacija trenutna, Rezervacija postajeAktivna) {
    return true;
  }

  @Override
  public void kadaRezervacijaPostalaAktivna(Rezervacija trenutna, Rezervacija aktivirana) {}

  @Override
  public void kadaRezervacijaPostalaOtkazana(Rezervacija trenutna, Rezervacija otkazana) {

    if (trenutna.korisnik().equals(otkazana.korisnik())) {

      Aranzman aranzman = trenutna.dajAranzman();

      boolean mozePostatiAktivna = aranzman.obavijestiRezervacijaPostajeAktivna(trenutna);

      if (mozePostatiAktivna) {
        trenutna.aktiviraj();
        aranzman.obavijestiRezervacijaPostalaAktivna(trenutna);
      }

    }

  }

  @Override
  public String dajNaziv() {
    return "Odgođena";
  }

}
