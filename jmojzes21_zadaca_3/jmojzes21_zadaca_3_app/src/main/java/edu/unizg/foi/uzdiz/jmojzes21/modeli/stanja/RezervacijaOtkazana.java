package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;

public class RezervacijaOtkazana extends RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {
    throw new RuntimeException("Nije moguće zaprimiti rezervaciju jer je rezervacija otkazana!");
  }

  @Override
  public void aktiviraj(Rezervacija rezervacija) {
    throw new RuntimeException("Nije moguće aktivirati rezervaciju jer je rezervacija otkazana!");
  }

  @Override
  public void staviNaCekanje(Rezervacija rezervacija) {
    throw new RuntimeException("Nije moguće rezervaciju staviti na čekanje jer je rezervacija otkazana!");
  }

  @Override
  public void odgodi(Rezervacija rezervacija) {
    throw new RuntimeException("Nije moguće odgoditi rezervaciju jer je rezervacija otkazana!");
  }

  @Override
  public void otkazi(Rezervacija rezervacija) {}

  @Override
  public StanjeId dajId() {
    return StanjeId.otkazana;
  }

  @Override
  public String dajNaziv() {
    return "Otkazana";
  }

}
