package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.AktivnaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.OtkazanaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.PrimljenaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.RezervacijaNaCekanju;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class TuristickiAgent {

  private final Map<Integer, Aranzman> aranzmani;

  public TuristickiAgent(Map<Integer, Aranzman> aranzmani) {
    this.aranzmani = aranzmani;
  }

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

  public void otkaziRezervaciju(Aranzman aranzman, String ime, String prezime) throws Exception {

    Rezervacija rezervacija = dajRezervacijuKorisnika(aranzman, ime, prezime);

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
      case OtkazanaRezervacija r:
        String opis = "Rezervacija za navedeni aranžman je već otkazana!";
        throw new Exception(opis);
      default:
        throw new IllegalStateException("Rezervacija nije valjana!");
    }

  }

  public boolean korisnikImaRezervaciju(Aranzman aranzman, String ime, String prezime) {
    List<Rezervacija> rezervacijeKorisnika = dajRezervacijeKorisnika(aranzman, ime, prezime);
    return !rezervacijeKorisnika.isEmpty();
  }

  public Rezervacija dajRezervacijuKorisnika(Aranzman aranzman, String ime, String prezime) {
    var rezervacije = dajRezervacijeKorisnika(aranzman, ime, prezime);
    if (rezervacije.isEmpty()) {return null;}
    return rezervacije.getFirst();
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

  private void otkaziPrimljenuRezervaciju(Aranzman aranzman, PrimljenaRezervacija rezervacija) throws Exception {

    if (!aranzman.rezervacije().remove(rezervacija)) {
      throw new Exception("Nije moguće otkazati rezervaciju!");
    }

    KreatorRezervacije kreatorRezervacije = new KreatorOtkazaneRezervacije();
    var otkazanaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);

    aranzman.otkazaneRezervacije().add(otkazanaRezervacija);

  }

  private void otkaziAktivnuRezervaciju(Aranzman aranzman, AktivnaRezervacija rezervacija) throws Exception {

    if (!aranzman.rezervacije().remove(rezervacija)) {
      throw new Exception("Nije moguće otkazati rezervaciju!");
    }

    List<Rezervacija> rezervacije = aranzman.rezervacije();
    Queue<Rezervacija> rezervacijeNaCekanju = aranzman.rezervacijeNaCekanju();

    KreatorRezervacije kreatorOtkazaneRezervacije = new KreatorOtkazaneRezervacije();
    var otkazanaRezervacija = kreatorOtkazaneRezervacije.promijeniVrstu(rezervacija);

    aranzman.otkazaneRezervacije().add(otkazanaRezervacija);

    if (!rezervacijeNaCekanju.isEmpty()) {

      var kreatorRezervacije = new KreatorAktivneRezervacije();
      var novaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacijeNaCekanju.poll());

      rezervacije.add(novaRezervacija);

    }

    if (aranzman.brojRezervacija() < aranzman.minBrojPutnika()) {

      var kreatorRezervacije = new KreatorPrimljeneRezervacije();

      List<Rezervacija> trenutneRezervacije = rezervacije.stream()
          .map(e -> kreatorRezervacije.promijeniVrstu(e))
          .toList();

      rezervacije.clear();
      rezervacije.addAll(trenutneRezervacije);

    }

  }

  private void otkaziRezervacijuNaCekanju(Aranzman aranzman, RezervacijaNaCekanju rezervacija) throws Exception {

    if (!aranzman.rezervacijeNaCekanju().remove(rezervacija)) {
      throw new Exception("Nije moguće otkazati rezervaciju!");
    }

    KreatorRezervacije kreatorRezervacije = new KreatorOtkazaneRezervacije();
    var otkazanaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);

    aranzman.otkazaneRezervacije().add(otkazanaRezervacija);

  }

  private List<Rezervacija> filtrirajRezervacijeKorisnika(Collection<Rezervacija> rezervacije, String ime,
      String prezime) {
    return rezervacije.stream()
        .filter(e -> e.ime().equals(ime) && e.prezime().equals(prezime))
        .toList();
  }

}
