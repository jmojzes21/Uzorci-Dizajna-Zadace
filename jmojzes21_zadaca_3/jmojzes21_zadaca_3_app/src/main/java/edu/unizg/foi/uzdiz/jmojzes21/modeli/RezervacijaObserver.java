package edu.unizg.foi.uzdiz.jmojzes21.modeli;

public interface RezervacijaObserver {

  void kadaRezervacijaPostalaAktivna(Rezervacija aktivirana);

  void kadaRezervacijaPostalaOtkazana(Rezervacija otkazana);

}
