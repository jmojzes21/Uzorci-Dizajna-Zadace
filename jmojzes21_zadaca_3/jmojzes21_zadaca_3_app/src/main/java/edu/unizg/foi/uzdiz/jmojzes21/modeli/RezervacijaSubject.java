package edu.unizg.foi.uzdiz.jmojzes21.modeli;

import java.util.List;

public interface RezervacijaSubject {

  List<RezervacijaObserver> dajPromatrace();

  boolean dodajPromatraca(RezervacijaObserver promatrac);

  boolean ukloniPromatraca(RezervacijaObserver promatrac);

  void obavijestiRezervacijaPostalaAktivna(Rezervacija aktivirana);

  void obavijestiRezervacijaNijeViseAktivna(Rezervacija rezervacija);

  void obavijestiPromjenuStanjaAranzmana(Aranzman aranzman);

  void obavijestiPromjenuStanjaRezervacije(Rezervacija rezervacija);


}
