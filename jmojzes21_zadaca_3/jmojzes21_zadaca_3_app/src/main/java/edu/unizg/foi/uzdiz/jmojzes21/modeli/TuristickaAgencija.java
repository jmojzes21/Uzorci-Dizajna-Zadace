package edu.unizg.foi.uzdiz.jmojzes21.modeli;

import edu.unizg.foi.uzdiz.jmojzes21.logika.UpravljanjeRezervacijamaStrategy;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.statistika.StatistikaAranzmana;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Turistička agencija.
 */
public class TuristickaAgencija extends PutovanjeComposite {

  private UpravljanjeRezervacijamaStrategy upravljanjeRezervacijama;

  @Override
  protected void dodaj(PutovanjeComponent r) {
    if (!(r instanceof Aranzman)) {
      throw new RuntimeException("Nije moguće dodati " + r.getClass().getName() + " u turističku agenciju!");
    }
    r.postaviRoditelja(this);
    djeca.add(r);
  }

  @Override
  public void obrisi(PutovanjeComponent r) {
    djeca.remove(r);
  }

  @Override
  public void obrisiSve() {
    for (var e : djeca) {
      if (e instanceof PutovanjeComposite c) {
        c.obrisiSve();
      }
    }
    djeca.clear();
  }

  /**
   * Daj listu svih aranžmana.
   *
   * @return lista aranžmana
   */
  public List<Aranzman> dajAranzmane() {
    return djeca.stream().map(e -> (Aranzman) e).toList();
  }

  /**
   * Daj listu aranžmana koji se odvijanju unutar određenog razdoblja.
   *
   * @param datumOd početni datum razdovlja
   * @param datumDo završni datum razdoblja
   * @return lista aranžmana
   */
  public List<Aranzman> dajAranzmane(LocalDate datumOd, LocalDate datumDo) {
    List<Aranzman> aranzmani = dajAranzmane();
    return aranzmani.stream()
        .filter(e -> !e.pocetniDatum().isAfter(datumDo) && !e.zavrsniDatum().isBefore(datumOd))
        .toList();
  }

  /**
   * Daj aranžman prema oznaci.
   *
   * @param oznaka oznaka aranžmana
   * @return aranžman ili null
   */
  public Aranzman dajAranzman(int oznaka) {
    List<Aranzman> aranzmani = dajAranzmane();
    return aranzmani.stream()
        .filter(e -> e.oznaka() == oznaka)
        .findFirst()
        .orElse(null);
  }

  /**
   * Dohvati sve rezervacije određenog aranžmana koje zadovoljavaju postavljeni filter.
   *
   * @param oznaka oznaka aranžmana
   * @param filter prikaži samo rezervacije čije je stanje uključeno u filter
   * @return lista rezervacija
   */
  public List<Rezervacija> dajRezervacijeAranzmana(int oznaka, List<Rezervacija.StanjeId> filter) {

    Aranzman aranzman = dajAranzman(oznaka);
    if (aranzman == null) {return null;}

    List<Rezervacija> rezervacije = aranzman.rezervacije();
    return rezervacije.stream()
        .filter(e -> filter.contains(e.idStanja()))
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
    List<Rezervacija> rezervacijeKorisnika = new ArrayList<>();

    List<Aranzman> aranzmani = dajAranzmane();
    for (var aranzman : aranzmani) {
      List<Rezervacija> rezervacije = aranzman.rezervacije();
      rezervacijeKorisnika.addAll(rezervacije.stream()
          .filter(e -> e.korisnik().equals(korisnik))
          .toList());
    }

    return rezervacijeKorisnika;
  }

  /**
   * Zaprimi rezervaciju korisnika.
   *
   * @param rezervacija rezervacija
   * @throws Exception zaprimanje rezervacije nije uspjelo
   */
  public void zaprimiRezervaciju(Rezervacija rezervacija) throws Exception {

    Aranzman aranzman = dajAranzman(rezervacija.oznakaAranzmana());
    if (aranzman == null) {
      String opis = String.format("Ne postoji aranžam oznake %d.", rezervacija.oznakaAranzmana());
      throw new Exception(opis);
    }

    aranzman.zaprimiRezervaciju(rezervacija);

  }

  /**
   * Zaprimi rezervacije korisnika.
   *
   * @param rezervacije lista rezervacija
   */
  public void zaprimiRezervacije(List<Rezervacija> rezervacije) {

    rezervacije.sort(Comparator.comparing(Rezervacija::vrijemePrijema));

    for (Rezervacija rezervacija : rezervacije) {
      try {
        zaprimiRezervaciju(rezervacija);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

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

    Aranzman aranzman = dajAranzman(oznaka);
    if (aranzman == null) {
      String opis = String.format("Ne postoji aranžam oznake %d.\n", oznaka);
      throw new Exception(opis);
    }

    var korisnik = new Korisnik(ime, prezime);
    aranzman.otkaziRezervaciju(korisnik);

  }

  public List<StatistikaAranzmana> dajStatistikuAranzmana(List<Aranzman> aranzmani) {
    return aranzmani.stream()
        .map(e -> new StatistikaAranzmana(e))
        .toList();
  }

  /**
   * Dodaj turističke aranžmane.
   *
   * @param aranzmani aranžmani
   */
  public void dodajAranzmane(List<Aranzman> aranzmani) {
    for (Aranzman aranzman : aranzmani) {
      try {
        dodajAranzman(aranzman);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Dodaj turistički aranžman.
   *
   * @param aranzman aranžman
   */
  public void dodajAranzman(Aranzman aranzman) throws Exception {

    if (dajAranzman(aranzman.oznaka()) != null) {
      String opis = String.format("Nije moguće dodati aranžman %d jer navedeni aranžman već postoji!",
          aranzman.oznaka());
      throw new Exception(opis);
    }

    List<Aranzman> trenutniAranzmani = dajAranzmane();
    for (var a : trenutniAranzmani) {
      a.dodajPromatraca(aranzman);
      aranzman.dodajPromatraca(a);
    }

    dodaj(aranzman);

  }


  public UpravljanjeRezervacijamaStrategy upravljanjeRezervacijama() {
    return upravljanjeRezervacijama;
  }

  public void setUpravljanjeRezervacijama(
      UpravljanjeRezervacijamaStrategy upravljanjeRezervacijama) {
    this.upravljanjeRezervacijama = upravljanjeRezervacijama;
  }
}
