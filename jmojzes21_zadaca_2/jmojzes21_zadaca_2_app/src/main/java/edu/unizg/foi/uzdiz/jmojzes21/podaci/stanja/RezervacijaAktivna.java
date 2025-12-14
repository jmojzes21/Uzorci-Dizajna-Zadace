package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import java.time.LocalDateTime;

public class RezervacijaAktivna implements RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaPrimljena());
  }

  @Override
  public void aktiviraj(Rezervacija rezervacija) {}

  @Override
  public void staviNaCekanje(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaNaCekanju());
  }

  @Override
  public void odgodi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaOdgodjena());
  }

  @Override
  public void otkazi(Rezervacija rezervacija) {
    rezervacija.setVrijemeOtkaza(LocalDateTime.now());
    rezervacija.postaviStanje(new RezervacijaOtkazana());
  }

  @Override
  public boolean kadaRezervacijaPostajeAktivna(Rezervacija trenutna, Rezervacija postajeAktivna) {

    if (trenutna.korisnik().equals(postajeAktivna.korisnik())) {

      Aranzman aranzman1 = trenutna.dajAranzman();
      Aranzman aranzman2 = postajeAktivna.dajAranzman();

      if (aranzman1.preklapaSe(aranzman2)) {
        if (postajeAktivna.vrijemePrijema().isAfter(trenutna.vrijemePrijema())) {
          System.out.println(
              "Rezervacija " + postajeAktivna.korisnik().punoIme() + " " + postajeAktivna.oznakaAranzmana()
                  + " ne može postati aktivna.");
          return false;
        }
      }

    }

    return true;
  }

  @Override
  public void kadaRezervacijaPostalaAktivna(Rezervacija rezervacija, Rezervacija aktivirana) {

    if (rezervacija.korisnik().equals(aktivirana.korisnik())) {

      Aranzman aranzman1 = rezervacija.dajAranzman();
      Aranzman aranzman2 = aktivirana.dajAranzman();

      if (aranzman1.preklapaSe(aranzman2)) {
        System.out.println(
            "Preklapanje " + rezervacija.korisnik().punoIme() + " " + rezervacija.oznakaAranzmana() + " s rezervacijom "
                + aktivirana.oznakaAranzmana());

        rezervacija.odgodi();
        rezervacija.dajAranzman().provjeriAktivneRezervacije();

      }
    }
  }

  @Override
  public void kadaRezervacijaPostalaOtkazana(Rezervacija trenutna, Rezervacija otkazana) {}

  @Override
  public String dajNaziv() {
    return "Aktivna";
  }

}
