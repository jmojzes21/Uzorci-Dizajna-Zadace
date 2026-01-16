package edu.unizg.foi.uzdiz.jmojzes21.logika.strategy;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;

public class UpravljanjeRezervacijamaVDRStrategy extends UpravljanjeRezervacijamaStrategy {

  @Override
  public boolean mozePostatiAktivna(TuristickaAgencija agencija, Rezervacija rezervacija) {
    var korisnik = rezervacija.korisnik();
    var aranzman = rezervacija.dajAranzman();

    int brojRezervacija = Math.toIntExact(aranzman.rezervacije().stream()
        .filter(e -> e.jePrimljena() || e.jeAktivna())
        .filter(e -> e.korisnik().equals(korisnik))
        .count());

    int maxBrojRezervacija = dajMaxBrojRezervacija(aranzman);
    return brojRezervacija < maxBrojRezervacija;
  }

  @Override
  public void kadaRezervacijaKorisnikaPostalaAktivna(TuristickaAgencija agencija, Rezervacija rezervacija,
      Rezervacija aktivirana) {}

  private int dajMaxBrojRezervacija(Aranzman aranzman) {
    int maxPutnika = aranzman.maxBrojPutnika();
    return Math.max(1, maxPutnika / 4);
  }

}
