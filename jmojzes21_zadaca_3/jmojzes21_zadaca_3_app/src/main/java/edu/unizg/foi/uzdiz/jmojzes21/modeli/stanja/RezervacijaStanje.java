package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;

public abstract class RezervacijaStanje {

  public void zaprimi(Rezervacija rezervacija) {}

  public void aktiviraj(Rezervacija rezervacija) {}

  public void staviNaCekanje(Rezervacija rezervacija) {}

  public void odgodi(Rezervacija rezervacija) {}

  public void otkazi(Rezervacija rezervacija) {}

  public void kadaRezervacijaPostalaAktivna(Rezervacija trenutna, Rezervacija aktivirana) {}

  public void kadaRezervacijaNijeViseAktivna(Rezervacija trenutna, Rezervacija nijeAktivna) {}

  public abstract StanjeId dajId();

  public abstract String dajNaziv();

}
