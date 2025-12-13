package edu.unizg.foi.uzdiz.jmojzes21.podaci;

public interface RezervacijaObserver {

  boolean kadaRezervacijaPostajeAktivna(Rezervacija rezervacija);

  void kadaRezervacijaPostalaAktivna(Rezervacija aktivirana);

}
