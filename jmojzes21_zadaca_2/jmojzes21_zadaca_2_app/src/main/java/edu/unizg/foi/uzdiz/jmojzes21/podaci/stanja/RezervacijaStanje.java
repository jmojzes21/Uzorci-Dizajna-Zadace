package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public interface RezervacijaStanje {

  void zaprimi(Rezervacija rezervacija);

  void aktiviraj(Rezervacija rezervacija);

  void staviNaCekanje(Rezervacija rezervacija);

  void odgodi(Rezervacija rezervacija);

  void otkazi(Rezervacija rezervacija);

  boolean kadaRezervacijaPostajeAktivna(Rezervacija trenutna, Rezervacija postajeAktivna);

  void kadaRezervacijaPostalaAktivna(Rezervacija trenutna, Rezervacija aktivirana);

  void kadaRezervacijaPostalaOtkazana(Rezervacija trenutna, Rezervacija otkazana);

  String dajNaziv();

}
