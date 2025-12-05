package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Turistička agencija.
 */
public class TuristickaAgencija {

  private static TuristickaAgencija turistickaAgencija;

  public static TuristickaAgencija dajInstancu() {
    if (turistickaAgencija == null) {
      turistickaAgencija = new TuristickaAgencija();
    }
    return turistickaAgencija;
  }

  /**
   * Mapa turističkih aranžmana. Ključ je oznaka aranžmana, a vrijednost je objekt aranžmana.
   */
  private final Map<Integer, Aranzman> aranzmani = new HashMap<>();

  public TuristickaAgencija() {}

  /**
   * Vrati listu svih aranžmana.
   *
   * @return lista aranžmana
   */
  public List<Aranzman> dajAranzmane() {
    return aranzmani.values().stream()
        .sorted(Comparator.comparing(Aranzman::oznaka))
        .toList();
  }

  /**
   * Vrati listu aranžmana koji se odvijanju unutar određenog razdoblja.
   *
   * @param datumOd početni datum razdovlja
   * @param datumDo završni datum razdoblja
   * @return lista aranžmana
   */
  public List<Aranzman> dajAranzmane(LocalDate datumOd, LocalDate datumDo) {
    return aranzmani.values().stream()
        .filter(e -> e.pocetniDatum().compareTo(datumOd) >= 0
            && e.pocetniDatum().compareTo(datumDo) <= 0)
        .sorted(Comparator.comparing(Aranzman::oznaka))
        .toList();
  }

  /**
   * Vrati aranžman prema oznaci.
   *
   * @param oznaka oznaka aranžmana
   * @return aranžman ili null
   */
  public Aranzman dajAranzman(int oznaka) {
    return aranzmani.get(oznaka);
  }

  /**
   * Dohvati sve rezervacije određenog aranžmana.
   *
   * @param oznaka                  oznaka
   * @param prikaziPrimljeneAktivne prikaži primljene i aktivne rezervacije
   * @param prikaziNaCekanju        prikaži rezervacije na čekanju
   * @param prikaziOtkazane         prikaži otkazane rezervacije
   * @return lista rezervacija
   */
  public List<Rezervacija> dajRezervacijeAranzmana(int oznaka, boolean prikaziPrimljeneAktivne,
      boolean prikaziNaCekanju, boolean prikaziOtkazane) {

    Aranzman aranzman = aranzmani.get(oznaka);
    if (aranzman == null) {return null;}

    List<Rezervacija> rezultat = new ArrayList<>();

    if (prikaziPrimljeneAktivne) {
      rezultat.addAll(aranzman.primljeneRezervacije());
      rezultat.addAll(aranzman.aktivneRezervacije());
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

  /**
   * Dohvati sve rezervacije sa svih aranžmana određenog korisnika.
   *
   * @param ime     ime korisnika
   * @param prezime prezime korisnika
   * @return rezervacije korisnika
   */
  public List<Rezervacija> dajRezervacijeKorisnika(String ime, String prezime) {

    var korisnik = new Korisnik(ime, prezime);

    var agent = new TuristickiAgent(aranzmani);
    var rezervacije = agent.dajSveRezervacijeKorisnika(korisnik, true);

    return rezervacije.stream()
        .sorted(Comparator.comparing(Rezervacija::vrsta).thenComparing(Rezervacija::datumVrijeme))
        .toList();
  }

  /**
   * Zaprimi rezervaciju korisnika
   *
   * @param rezervacija rezervacija
   * @throws Exception zaprimanje rezervacije nije uspjelo
   */
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

  /**
   * Otkaži rezervaciju korisnika
   *
   * @param ime     ime
   * @param prezime prezime
   * @param oznaka  oznaka aranžmana
   * @throws Exception otkazivanje rezervacije nije uspjelo
   */
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

  /**
   * Učitaj turističke aranžmane.
   *
   * @param aranzmani aranžmani
   */
  public void ucitajAranzmane(List<Aranzman> aranzmani) {
    this.aranzmani.clear();

    for (Aranzman aranzman : aranzmani) {
      this.aranzmani.put(aranzman.oznaka(), aranzman);
    }
  }

}
