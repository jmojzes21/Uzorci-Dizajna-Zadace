package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TuristickiAgent {

  public TuristickiAgent() {}

  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    if (korisnikImaRezervaciju(aranzman, rezervacija.ime(), rezervacija.prezime())) {
      String opis = "Korisnik već ima rezervaciju za navedeni aranžman!";
      throw new Exception(opis);
    }

    int brojRezervacija = aranzman.brojRezervacija();
    int minPutnika = aranzman.minBrojPutnika();
    int maxPutnika = aranzman.maxBrojPutnika();

    if (brojRezervacija >= maxPutnika) {
      dodajRezervacijuNaCekanje(aranzman, rezervacija);
      return;
    }

    if (brojRezervacija >= minPutnika) {
      dodajAktivnuRezervaciju(aranzman, rezervacija);
      return;
    }

    dodajPrimljenuRezervaciju(aranzman, rezervacija);

  }

  public boolean korisnikImaRezervaciju(Aranzman aranzman, String ime, String prezime) {
    List<Rezervacija> rezervacijeKorisnika = dajRezervacijeKorisnika(aranzman, ime, prezime);
    return !rezervacijeKorisnika.isEmpty();
  }

  public List<Rezervacija> dajRezervacijeKorisnika(Aranzman aranzman, String ime, String prezime) {
    List<Rezervacija> rezultat = new ArrayList<>();

    rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.rezervacije(), ime, prezime));
    rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.rezervacijeNaCekanju(), ime, prezime));
    rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.otkazaneRezervacije(), ime, prezime));

    return rezultat.stream()
        .sorted(Comparator.comparing(Rezervacija::datumVrijeme))
        .toList();
  }

  private void dodajPrimljenuRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {

    List<Rezervacija> rezervacije = aranzman.rezervacije();

    int brojRezervacija = aranzman.brojRezervacija();
    int minPutnika = aranzman.minBrojPutnika();

    if (brojRezervacija + 1 >= minPutnika) {

      KreatorRezervacije kreatorRezervacije = new KreatorAktivneRezervacije();

      List<Rezervacija> trenutneRezervacije = rezervacije.stream()
          .map(e -> kreatorRezervacije.promijeniVrstu(e))
          .toList();

      rezervacije.clear();
      rezervacije.addAll(trenutneRezervacije);

      var novaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);
      rezervacije.add(novaRezervacija);

    } else {

      KreatorRezervacije kreatorRezervacije = new KreatorPrimljeneRezervacije();

      var novaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);
      rezervacije.add(novaRezervacija);

    }

  }

  private void dodajAktivnuRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
    KreatorRezervacije kreatorRezervacije = new KreatorAktivneRezervacije();

    var novaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);
    aranzman.rezervacije().add(novaRezervacija);
  }

  private void dodajRezervacijuNaCekanje(Aranzman aranzman, Rezervacija rezervacija) {
    KreatorRezervacije kreatorRezervacije = new KreatorRezervacijeNaCekanju();

    var novaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);
    aranzman.rezervacijeNaCekanju().add(novaRezervacija);

  }

  private List<Rezervacija> filtrirajRezervacijeKorisnika(Collection<Rezervacija> rezervacije, String ime,
      String prezime) {
    return rezervacije.stream()
        .filter(e -> e.ime().equals(ime) && e.prezime().equals(prezime))
        .toList();
  }

}
