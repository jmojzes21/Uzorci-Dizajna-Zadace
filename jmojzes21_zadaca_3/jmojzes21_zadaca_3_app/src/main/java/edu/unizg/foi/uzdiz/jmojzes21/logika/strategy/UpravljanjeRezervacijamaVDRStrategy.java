package edu.unizg.foi.uzdiz.jmojzes21.logika.strategy;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;

public class UpravljanjeRezervacijamaVDRStrategy extends UpravljanjeRezervacijamaStrategy {


  @Override
  public boolean mozeZaprimiti(Aranzman aranzman, Rezervacija rezervacija) {
    var korisnik = rezervacija.korisnik();

    int brojRezervacija = Math.toIntExact(aranzman.primljeneRezervacije().stream()
        .filter(e -> e.korisnik().equals(korisnik))
        .count());

    int maxBrojRezervacija = dajMaxBrojRezervacija(aranzman);
    return brojRezervacija < maxBrojRezervacija;
  }

  @Override
  public boolean mozePostatiAktivna(Aranzman aranzman, Rezervacija rezervacija) {
    var korisnik = rezervacija.korisnik();

    int brojRezervacija = Math.toIntExact(aranzman.aktivneRezervacije().stream()
        .filter(e -> e.korisnik().equals(korisnik))
        .count());

    int maxBrojRezervacija = dajMaxBrojRezervacija(aranzman);
    return brojRezervacija < maxBrojRezervacija;
  }

  @Override
  public void kadaRezervacijaKorisnikaPostalaAktivna(Rezervacija rezervacija, Rezervacija aktivirana) {

  }

  private int dajMaxBrojRezervacija(Aranzman aranzman) {
    int maxPutnika = aranzman.maxBrojPutnika();
    return Math.max(1, maxPutnika / 4);
  }

}
