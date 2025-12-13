package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public interface RezervacijaStanje {

  default void zaprimi(Rezervacija rezervacija) {throw new RuntimeException();}

  default void aktiviraj(Rezervacija rezervacija) {throw new RuntimeException();}

  default void staviNaCekanje(Rezervacija rezervacija) {
    throw new RuntimeException("Nije moguce staviti rezervaciju na cekanje");
  }

  default void odgodi(Rezervacija rezervacija) {
    throw new RuntimeException("Nije moguće odgoditi rezervaciju");
  }

  default void otkazi(Rezervacija rezervacija) {
    throw new RuntimeException("Nije moguće otkazati rezervaciju");
  }


  default boolean kadaRezervacijaPostajeAktivna(Rezervacija trenutna, Rezervacija postajeAktivna) {return true;}

  default void kadaRezervacijaPostalaAktivna(Rezervacija trenutna, Rezervacija aktivirana) {}

  default void kadaRezervacijaPostalaOtkazana(Rezervacija trenutna, Rezervacija otkazana) {}
  
  String dajNaziv();

}
