package edu.unizg.foi.uzdiz.jmojzes21.podaci;

public interface RezervacijaSubject {

  void dodajPromatraca(RezervacijaObserver promatrac);

  void ukloniPromatraca(RezervacijaObserver promatrac);

  void obavijestiAktiviranjeRezervacije(Rezervacija rezervacija);

}
