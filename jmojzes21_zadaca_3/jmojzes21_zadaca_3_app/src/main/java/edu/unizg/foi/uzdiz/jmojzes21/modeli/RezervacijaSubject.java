package edu.unizg.foi.uzdiz.jmojzes21.modeli;

public interface RezervacijaSubject {

  boolean dodajPromatraca(RezervacijaObserver promatrac);

  boolean ukloniPromatraca(RezervacijaObserver promatrac);

  void obavijestiRezervacijaPostalaAktivna(Rezervacija aktivirana);

  void obavijestiRezervacijaNijeViseAktivna(Rezervacija rezervacija);

  void obavijestiPromjenuStanjaAranzmana(Aranzman aranzman);

  void obavijestiPromjenuStanjaRezervacije(Rezervacija rezervacija);


}
