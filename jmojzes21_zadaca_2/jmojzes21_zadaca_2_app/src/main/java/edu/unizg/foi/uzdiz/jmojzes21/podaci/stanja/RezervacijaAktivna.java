package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public class RezervacijaAktivna implements RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaPrimljena());
  }

  @Override
  public void odgodi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaOdgodjena());
  }

  @Override
  public void staviNaCekanje(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaNaCekanju());
  }

  @Override
  public void kadaAktiviranaRezervacija(Rezervacija rezervacija, Rezervacija aktivirana) {

    if (rezervacija.korisnik().equals(aktivirana.korisnik())) {

      Aranzman aranzman1 = rezervacija.dajAranzman();
      Aranzman aranzman2 = aktivirana.dajAranzman();

      if (aranzman1.preklapaSe(aranzman2)) {
        System.out.println(
            "Preklapanje " + rezervacija.korisnik().punoIme() + " " + rezervacija.oznakaAranzmana() + " s rezervacijom "
                + aktivirana.oznakaAranzmana());

        Rezervacija zaOdgodu;

        if (rezervacija.vrijemePrijema().isBefore(aktivirana.vrijemePrijema())) {
          System.out.println("Rezervacija " + aktivirana.oznakaAranzmana() + " postaje odgođena");
          zaOdgodu = aktivirana;
        } else {
          System.out.println("Rezervacija " + rezervacija.oznakaAranzmana() + " postaje odgođena");
          zaOdgodu = rezervacija;
        }

        zaOdgodu.odgodi();
        zaOdgodu.dajAranzman().provjeriAktivneRezervacije();

      }

    }
  }

  @Override
  public String dajNaziv() {
    return "Aktivna";
  }

}
