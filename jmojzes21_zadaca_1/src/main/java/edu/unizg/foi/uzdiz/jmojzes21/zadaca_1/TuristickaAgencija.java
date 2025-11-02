package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuristickaAgencija {

  private static TuristickaAgencija turistickaAgencija;

  public static TuristickaAgencija dajInstancu() {
    if (turistickaAgencija == null) {
      turistickaAgencija = new TuristickaAgencija();
    }
    return turistickaAgencija;
  }

  private final Map<Integer, Aranzman> aranzmani = new HashMap<>();

  public TuristickaAgencija() {}

  public List<Aranzman> dajAranzmane() {
    return aranzmani.values().stream()
        .sorted(Comparator.comparing(Aranzman::oznaka))
        .toList();
  }

  public List<Aranzman> dajAranzmane(LocalDate datumOd, LocalDate datumDo) {
    return aranzmani.values().stream()
        .filter(e -> e.pocetniDatum().compareTo(datumOd) >= 0
            && e.pocetniDatum().compareTo(datumDo) <= 0)
        .sorted(Comparator.comparing(Aranzman::oznaka))
        .toList();
  }

  public Aranzman dajAranzman(int oznaka) {
    return aranzmani.get(oznaka);
  }

  public List<Rezervacija> dajRezervacijeAranzmana(int oznaka, boolean prikaziGlavne, boolean prikaziNaCekanju,
      boolean prikaziOtkazane) {

    Aranzman aranzman = aranzmani.get(oznaka);
    if (aranzman == null) {return null;}

    List<Rezervacija> rezultat = new ArrayList<>();

    if (prikaziGlavne) {
      rezultat.addAll(aranzman.rezervacije());
    }

    if (prikaziNaCekanju) {
      rezultat.addAll(aranzman.rezervacijeNaCekanju());
    }

    if (prikaziOtkazane) {
      rezultat.addAll(aranzman.otkazaneRezervacije());
    }

    return rezultat.stream()
        .sorted(Comparator.comparing(Rezervacija::vrsta).thenComparing(Rezervacija::datumVrijeme))
        .toList();
  }

  public List<Rezervacija> dajRezervacijeKorisnika(String ime, String prezime) {

    var korisnik = new Korisnik(ime, prezime);

    var agent = new TuristickiAgent(aranzmani);
    var rezervacije = agent.dajSveRezervacijeKorisnika(korisnik, true);

    return rezervacije.stream()
        .sorted(Comparator.comparing(Rezervacija::vrsta).thenComparing(Rezervacija::datumVrijeme))
        .toList();
  }

  public void zaprimiRezervaciju(Rezervacija rezervacija) throws Exception {

    Aranzman aranzman = aranzmani.get(rezervacija.oznakaAranzmana());
    if (aranzman == null) {
      String opis = String.format("Ne postoji aranžam oznake %d.\n",
          rezervacija.oznakaAranzmana());
      throw new Exception(opis);
    }

    var agent = new TuristickiAgent(aranzmani);
    agent.zaprimiRezervaciju(aranzman, rezervacija);

  }

  public void otkaziRezervaciju(String ime, String prezime, int oznaka) throws Exception {

    Aranzman aranzman = aranzmani.get(oznaka);
    if (aranzman == null) {
      String opis = String.format("Ne postoji aranžam oznake %d.\n", oznaka);
      throw new Exception(opis);
    }

    var korisnik = new Korisnik(ime, prezime);

    var agent = new TuristickiAgent(aranzmani);
    agent.otkaziRezervaciju(aranzman, korisnik);

  }

  public void ucitajAranzmane(List<Aranzman> aranzmani) {
    this.aranzmani.clear();

    for (Aranzman aranzman : aranzmani) {
      this.aranzmani.put(aranzman.oznaka(), aranzman);
    }
  }

}
