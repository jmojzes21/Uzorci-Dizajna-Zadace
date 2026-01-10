package edu.unizg.foi.uzdiz.jmojzes21.modeli;

public interface RezervacijaObserver {

  boolean kadaRezervacijaPostajeAktivna(Rezervacija rezervacija);

  void kadaRezervacijaPostalaAktivna(Rezervacija aktivirana);

  void kadaRezervacijaPostalaOtkazana(Rezervacija otkazana);

}
