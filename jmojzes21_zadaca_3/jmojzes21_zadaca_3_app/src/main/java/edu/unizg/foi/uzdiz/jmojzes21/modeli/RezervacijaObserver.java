package edu.unizg.foi.uzdiz.jmojzes21.modeli;

public interface RezervacijaObserver {

  default void kadaRezervacijaPostalaAktivna(Rezervacija aktivirana) {}

  default void kadaRezervacijaPostalaOtkazana(Rezervacija otkazana) {}

  default void kadaPromjenaStanjaAranzmana(Aranzman aranzman) {}

  default void kadaPromjenaStanjaRezervacije(Rezervacija rezervacija) {}

}
