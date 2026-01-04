package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public interface AranzmanStanje {

  void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception;

  void aktiviraj(Aranzman aranzman) throws Exception;

  void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik) throws Exception;

  void aktivirajRezervaciju(Aranzman aranzman, Rezervacija rezervacija);
  
  void provjeriStanje(Aranzman aranzman);

  StanjeId dajId();

  String dajNaziv();

}
