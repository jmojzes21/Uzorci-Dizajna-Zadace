package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;

public class RezervacijaOtkazana implements RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {}

  @Override
  public void aktiviraj(Rezervacija rezervacija) {}

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
  public void kadaRezervacijaPostalaOtkazana(Rezervacija trenutna, Rezervacija otkazana) {}

  @Override
  public StanjeId dajId() {
    return StanjeId.otkazana;
  }

  @Override
  public String dajNaziv() {
    return "Otkazana";
  }

}
