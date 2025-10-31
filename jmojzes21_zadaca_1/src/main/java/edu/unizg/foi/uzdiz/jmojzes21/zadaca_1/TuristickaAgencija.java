package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
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

  public TuristickaAgencija() {

  }

  public List<Aranzman> dajAranzmane() {
    return aranzmani.values().stream()
        .sorted(Comparator.comparing(Aranzman::pocetniDatum))
        .toList();
  }

  public List<Aranzman> dajAranzmane(LocalDate datumOd, LocalDate datumDo) {
    return aranzmani.values().stream()
        .filter(e -> e.pocetniDatum().compareTo(datumOd) >= 0
            && e.pocetniDatum().compareTo(datumDo) <= 0)
        .sorted(Comparator.comparing(Aranzman::pocetniDatum))
        .toList();
  }

  public Aranzman dajAranzman(int oznaka) {
    return aranzmani.get(oznaka);
  }

  public List<Rezervacija> dajRezervacijeAranzmana(int oznaka, String filter) {

    Aranzman aranzman = aranzmani.get(oznaka);
    if (aranzman == null) {return null;}

    boolean prikaziPrimljeneAktivne = filter.contains("PA");
    boolean prikaziNaCekanju = filter.contains("Č");
    boolean prikaziOtkazane = filter.contains("O");

    List<Rezervacija> rezultat = new ArrayList<>();

    if (prikaziPrimljeneAktivne) {
      rezultat.addAll(aranzman.rezervacije());
    }

    if (prikaziNaCekanju) {
      rezultat.addAll(aranzman.rezervacijeNaCekanju());
    }

    if (prikaziOtkazane) {
      rezultat.addAll(aranzman.otkazaneRezervacije());
    }

    return rezultat.stream()
        .sorted(Comparator.comparing(Rezervacija::datumVrijeme))
        .toList();
  }

  public List<Rezervacija> dajRezervacijeKorisnika(String ime, String prezime) {
    List<Rezervacija> rezultat = new ArrayList<>();
    var agent = new TuristickiAgent(aranzmani);

    for (Aranzman aranzman : aranzmani.values()) {
      rezultat.addAll(agent.dajRezervacijeKorisnika(aranzman, ime, prezime));
    }

    return rezultat.stream()
        .sorted(Comparator.comparing(Rezervacija::datumVrijeme))
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

    var agent = new TuristickiAgent(aranzmani);
    agent.otkaziRezervaciju(aranzman, ime, prezime);

  }

  public void ucitajAranzmane(List<Aranzman> aranzmani) {
    this.aranzmani.clear();

    for (Aranzman aranzman : aranzmani) {
      this.aranzmani.put(aranzman.oznaka(), aranzman);
    }
  }

}
