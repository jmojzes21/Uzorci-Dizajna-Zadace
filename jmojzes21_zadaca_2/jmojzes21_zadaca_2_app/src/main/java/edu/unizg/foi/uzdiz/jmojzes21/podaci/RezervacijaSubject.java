package edu.unizg.foi.uzdiz.jmojzes21.podaci;

public interface RezervacijaSubject {

  void dodajPromatraca(RezervacijaObserver promatrac);

  void ukloniPromatraca(RezervacijaObserver promatrac);

  boolean obavijestiRezervacijaPostajeAktivna(Rezervacija rezervacija);

  void obavijestiRezervacijaPostalaAktivna(Rezervacija aktivirana);

  void obavijestiRezervacijaPostalaOtkazana(Rezervacija otkazana);

}
