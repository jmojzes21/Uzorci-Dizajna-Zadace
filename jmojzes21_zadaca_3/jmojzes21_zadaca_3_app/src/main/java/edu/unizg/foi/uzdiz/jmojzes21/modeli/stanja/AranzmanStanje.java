package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;

public abstract class AranzmanStanje {

  public abstract void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija);

  public abstract void aktiviraj(Aranzman aranzman);

  public abstract void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik);

  public abstract void aktivirajRezervaciju(Aranzman aranzman, Rezervacija rezervacija);

  public abstract void provjeriStanje(Aranzman aranzman);

  public abstract StanjeId dajId();

  public abstract String dajNaziv();

}
