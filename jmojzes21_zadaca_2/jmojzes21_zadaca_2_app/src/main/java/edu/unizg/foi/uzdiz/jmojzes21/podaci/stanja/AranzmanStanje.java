package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public interface AranzmanStanje {

  default void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {
    throw new RuntimeException();
  }

  default void postaviUPripremi(Aranzman aranzman) {throw new RuntimeException();}

  default void aktiviraj(Aranzman aranzman) throws Exception {throw new RuntimeException();}

  default void popuni(Aranzman aranzman) {throw new RuntimeException();}

  default void provjeriAktivneRezervacije(Aranzman aranzman) {throw new RuntimeException();}

  String dajNaziv();

}
