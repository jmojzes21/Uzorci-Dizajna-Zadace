package edu.unizg.foi.uzdiz.jmojzes21.modeli;

public interface RezervacijaSubject {

  void dodajPromatraca(RezervacijaObserver promatrac);

  void ukloniPromatraca(RezervacijaObserver promatrac);

  void obavijestiRezervacijaPostalaAktivna(Rezervacija aktivirana);

  void obavijestiRezervacijaPostalaOtkazana(Rezervacija otkazana);

}
