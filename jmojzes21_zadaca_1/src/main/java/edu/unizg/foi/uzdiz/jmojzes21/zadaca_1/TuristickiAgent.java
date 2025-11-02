package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.AktivnaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.PrimljenaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.RezervacijaNaCekanju;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class TuristickiAgent {

  private final Map<Integer, Aranzman> aranzmani;

  public TuristickiAgent(Map<Integer, Aranzman> aranzmani) {
    this.aranzmani = aranzmani;
  }

  // region Zaprimi i otkaži rezervaciju

  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    if (korisnikImaRezervaciju(aranzman, rezervacija.korisnik())) {
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

  public void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik) throws Exception {

    Rezervacija rezervacija = dajRezervacijuKorisnika(aranzman, korisnik);

    if (rezervacija == null) {
      String opis = "Korisnik nema rezervaciju za navedeni aranžman!";
      throw new Exception(opis);
    }

    switch (rezervacija) {
      case PrimljenaRezervacija r:
        otkaziPrimljenuRezervaciju(aranzman, r);
        break;
      case AktivnaRezervacija r:
        otkaziAktivnuRezervaciju(aranzman, r);
        break;
      case RezervacijaNaCekanju r:
        otkaziRezervacijuNaCekanju(aranzman, r);
        break;
      default:
        throw new Exception("Nije moguće otkazati rezervaciju!");
    }

  }

  private void dodajPrimljenuRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {

    List<Rezervacija> rezervacije = aranzman.rezervacije();
    int minPutnika = aranzman.minBrojPutnika();

    var novaRezervacija = promijeniVrstuRezervacije(rezervacija, new KreatorPrimljeneRezervacije());
    aranzman.rezervacije().add(novaRezervacija);

    if (aranzman.brojRezervacija() >= minPutnika) {

      var kreatorRezervacije = new KreatorAktivneRezervacije();

      List<Rezervacija> trenutneRezervacije = rezervacije.stream()
          .map(e -> promijeniVrstuRezervacije(e, kreatorRezervacije))
          .toList();

      rezervacije.clear();
      rezervacije.addAll(trenutneRezervacije);

    }

  }

  private void dodajAktivnuRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
    var novaRezervacija = promijeniVrstuRezervacije(rezervacija, new KreatorAktivneRezervacije());
    aranzman.rezervacije().add(novaRezervacija);
  }

  private void dodajRezervacijuNaCekanje(Aranzman aranzman, Rezervacija rezervacija) {
    var novaRezervacija = promijeniVrstuRezervacije(rezervacija, new KreatorRezervacijeNaCekanju());
    aranzman.rezervacijeNaCekanju().add(novaRezervacija);
  }

  private void otkaziPrimljenuRezervaciju(Aranzman aranzman, PrimljenaRezervacija rezervacija) throws Exception {

    if (!aranzman.rezervacije().remove(rezervacija)) {
      throw new Exception("Nije moguće otkazati rezervaciju!");
    }

    var otkazanaRezervacija = promijeniVrstuRezervacije(rezervacija, new KreatorOtkazaneRezervacije());
    aranzman.otkazaneRezervacije().add(otkazanaRezervacija);

  }

  private void otkaziAktivnuRezervaciju(Aranzman aranzman, AktivnaRezervacija rezervacija) throws Exception {

    if (!aranzman.rezervacije().remove(rezervacija)) {
      throw new Exception("Nije moguće otkazati rezervaciju!");
    }

    List<Rezervacija> rezervacije = aranzman.rezervacije();
    Queue<Rezervacija> rezervacijeNaCekanju = aranzman.rezervacijeNaCekanju();

    var otkazanaRezervacija = promijeniVrstuRezervacije(rezervacija, new KreatorOtkazaneRezervacije());
    aranzman.otkazaneRezervacije().add(otkazanaRezervacija);

    if (!rezervacijeNaCekanju.isEmpty()) {

      var novaRezervacija = promijeniVrstuRezervacije(rezervacijeNaCekanju.poll(), new KreatorAktivneRezervacije());
      rezervacije.add(novaRezervacija);

    }

    if (aranzman.brojRezervacija() < aranzman.minBrojPutnika()) {

      var kreatorRezervacije = new KreatorPrimljeneRezervacije();

      List<Rezervacija> trenutneRezervacije = rezervacije.stream()
          .map(e -> promijeniVrstuRezervacije(e, kreatorRezervacije))
          .toList();

      rezervacije.clear();
      rezervacije.addAll(trenutneRezervacije);

    }

  }

  private void otkaziRezervacijuNaCekanju(Aranzman aranzman, RezervacijaNaCekanju rezervacija) throws Exception {

    if (!aranzman.rezervacijeNaCekanju().remove(rezervacija)) {
      throw new Exception("Nije moguće otkazati rezervaciju!");
    }

    var otkazanaRezervacija = promijeniVrstuRezervacije(rezervacija, new KreatorOtkazaneRezervacije());
    aranzman.otkazaneRezervacije().add(otkazanaRezervacija);

  }

  private Rezervacija promijeniVrstuRezervacije(Rezervacija rezervacija, KreatorRezervacije kreatorRezervacije) {
    return kreatorRezervacije.promijeniVrstu(rezervacija);
  }

  // endregion

  // region Dohvaćanje rezervacija korisnika

  public boolean korisnikImaRezervaciju(Aranzman aranzman, Korisnik korisnik) {
    List<Rezervacija> rezervacijeKorisnika = dajRezervacijeKorisnika(aranzman, korisnik, false);
    return !rezervacijeKorisnika.isEmpty();
  }

  public Rezervacija dajRezervacijuKorisnika(Aranzman aranzman, Korisnik korisnik) {
    var rezervacije = dajRezervacijeKorisnika(aranzman, korisnik, false);
    if (rezervacije.isEmpty()) {return null;}
    return rezervacije.getFirst();
  }

  public List<Rezervacija> dajRezervacijeKorisnika(Aranzman aranzman, Korisnik korisnik, boolean prikaziOtkazane) {
    List<Rezervacija> rezultat = new ArrayList<>();

    rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.rezervacije(), korisnik));
    rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.rezervacijeNaCekanju(), korisnik));

    if (prikaziOtkazane) {
      rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.otkazaneRezervacije(), korisnik));
    }

    return rezultat;
  }

  public List<Rezervacija> dajSveRezervacijeKorisnika(Korisnik korisnik, boolean prikaziOtkazane) {
    List<Rezervacija> rezultat = new ArrayList<>();

    for (Aranzman aranzman : aranzmani.values()) {
      rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.rezervacije(), korisnik));
      rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.rezervacijeNaCekanju(), korisnik));

      if (prikaziOtkazane) {
        rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.otkazaneRezervacije(), korisnik));
      }
    }

    return rezultat;
  }

  private List<Rezervacija> filtrirajRezervacijeKorisnika(Collection<Rezervacija> rezervacije, Korisnik korisnik) {
    return rezervacije.stream()
        .filter(e -> e.korisnik().equals(korisnik))
        .toList();
  }

  // endregion

}
