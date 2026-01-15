package edu.unizg.foi.uzdiz.jmojzes21.modeli;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.AranzmanOtkazan;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.AranzmanStanje;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.AranzmanUPripremi;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Turistički aranžman.
 */
public class Aranzman extends PutovanjeComposite implements RezervacijaSubject, RezervacijaObserver {

  public enum StanjeId {
    uPripremi, aktivan, popunjen, otkazan
  }

  private int oznaka;
  private String naziv;

  private String program;
  private LocalDate pocetniDatum;
  private LocalDate zavrsniDatum;

  private LocalTime vrijemeKretanja;
  private LocalTime vrijemePovratka;

  private float cijena;

  private int minBrojPutnika;
  private int maxBrojPutnika;

  private int brojNocenja;
  private int brojDorucka;
  private int brojRuckova;
  private int brojVecera;

  private float doplataZaJednokrevetnuSobu;
  private List<String> prijevoz;

  private AranzmanStanje stanje;

  private final List<RezervacijaObserver> promatraci = new ArrayList<>();

  public Aranzman(int oznaka, String naziv) {
    this.oznaka = oznaka;
    this.naziv = naziv;
    stanje = new AranzmanUPripremi();
  }

  @Override
  public void dodaj(PutovanjeComponent r) {
    if (!(r instanceof Rezervacija)) {
      throw new RuntimeException("Nije moguće dodati " + r.getClass().getName() + " u turistički aranžman!");
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
    djeca.clear();
    postaviStanje(new AranzmanUPripremi());
  }

  public void zaprimiRezervaciju(Rezervacija rezervacija) {
    stanje.zaprimiRezervaciju(this, rezervacija);
  }

  public void otkaziRezervaciju(Korisnik korisnik) {
    stanje.otkaziRezervaciju(this, korisnik);
  }

  public void aktivirajRezervaciju(Rezervacija rezervacija) {
    stanje.aktivirajRezervaciju(this, rezervacija);
  }

  public void aktiviraj() {
    stanje.aktiviraj(this);
  }

  public void otkazi() {

    if (stanje instanceof AranzmanOtkazan) {
      String opis = String.format("Nije moguće otkazati aranžman %d jer je on već otkazan.", oznaka);
      throw new RuntimeException(opis);
    }

    List<Rezervacija> rezervacije = rezervacije();
    for (var r : rezervacije) {
      r.otkazi();
      obavijestiRezervacijaPostalaOtkazana(r);
    }

    postaviStanje(new AranzmanOtkazan());

  }

  public void provjeriStanje() {
    stanje.provjeriStanje(this);
  }

  @Override
  public void kadaRezervacijaPostalaAktivna(Rezervacija aktivirana) {
    List<Rezervacija> rezervacije = aktivneRezervacije();
    for (Rezervacija r : rezervacije) {
      r.kadaRezervacijaPostalaAktivna(aktivirana);
    }
  }

  @Override
  public void kadaRezervacijaPostalaOtkazana(Rezervacija otkazana) {
    List<Rezervacija> rezervacije = odgodjeneRezervacije();
    for (Rezervacija r : rezervacije) {
      r.kadaRezervacijaPostalaOtkazana(otkazana);
    }
  }

  @Override
  public void dodajPromatraca(RezervacijaObserver promatrac) {
    if (!promatraci.contains(promatrac)) {
      promatraci.add(promatrac);
    }
  }

  @Override
  public void ukloniPromatraca(RezervacijaObserver promatrac) {
    promatraci.remove(promatrac);
  }


  @Override
  public void obavijestiRezervacijaPostalaAktivna(Rezervacija aktivirana) {
    for (var promatrac : promatraci) {
      promatrac.kadaRezervacijaPostalaAktivna(aktivirana);
    }
  }

  @Override
  public void obavijestiRezervacijaPostalaOtkazana(Rezervacija otkazana) {
    for (var promatrac : promatraci) {
      promatrac.kadaRezervacijaPostalaOtkazana(otkazana);
    }
  }

  public AranzmanStanje stanje() {
    return stanje;
  }

  public void postaviStanje(AranzmanStanje stanje) {
    this.stanje = stanje;
  }

  public StanjeId idStanja() {
    return stanje.dajId();
  }

  public String nazivStanja() {
    return stanje.dajNaziv();
  }

  public List<Rezervacija> rezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .toList();
  }

  public List<Rezervacija> primljeneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jePrimljena())
        .toList();
  }

  public List<Rezervacija> aktivneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeAktivna())
        .toList();
  }

  public List<Rezervacija> primljeneAktivneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jePrimljena() || e.jeAktivna())
        .toList();
  }

  public List<Rezervacija> rezervacijeNaCekanju() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeNaCekanju())
        .toList();
  }

  public List<Rezervacija> otkazaneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeOtkazana())
        .toList();
  }

  public List<Rezervacija> odgodjeneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeOdgodjena())
        .toList();
  }

  public int brojPrimljenih() {
    return Math.toIntExact(djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jePrimljena())
        .count());
  }

  public int brojAktivnih() {
    return Math.toIntExact(djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeAktivna())
        .count());
  }

  public TuristickaAgencija dajAgenciju() {
    return (TuristickaAgencija) dajRoditelja();
  }

  /**
   * Provjeri preklapa li se ovaj aranžman s drugim aranžmanom.
   *
   * @param drugi
   * @return true ako se aranžmani preklapaju
   */
  public boolean preklapaSe(Aranzman drugi) {
    if (pocetniDatum().isBefore(drugi.pocetniDatum())) {
      return zavrsniDatum().compareTo(drugi.pocetniDatum()) >= 0;
    } else {
      return drugi.zavrsniDatum().compareTo(pocetniDatum()) >= 0;
    }
  }

  public Rezervacija dajNajnovijuRezervaciju(List<Rezervacija> rezervacije) {
    return rezervacije.stream().max(Comparator.comparing(Rezervacija::vrijemePrijema)).orElse(null);
  }

  /**
   * Sortira turističke aranžmane prema datumu početka.
   *
   * @param aranzmani turistički aranžmani
   * @param uzlazno   ako je true sortira uzlazno, inače sortira silazno
   * @return sortirani turistički aranžmani
   */
  public static List<Aranzman> sortiraj(List<Aranzman> aranzmani, boolean uzlazno) {
    var comparator = Comparator.comparing(Aranzman::pocetniDatum)
        .thenComparing(Aranzman::vrijemeKretanja, Comparator.nullsLast(LocalTime::compareTo));
    if (!uzlazno) {comparator = comparator.reversed();}
    return aranzmani.stream().sorted(comparator).toList();
  }

  // region Metode za dohvaćanje i postavljanje atributa

  public int oznaka() {return oznaka;}

  public String naziv() {return naziv;}

  public String program() {return program;}

  public LocalDate pocetniDatum() {return pocetniDatum;}

  public LocalDate zavrsniDatum() {return zavrsniDatum;}

  public LocalTime vrijemeKretanja() {return vrijemeKretanja;}

  public LocalTime vrijemePovratka() {return vrijemePovratka;}

  public float cijena() {return cijena;}

  public int minBrojPutnika() {return minBrojPutnika;}

  public int maxBrojPutnika() {return maxBrojPutnika;}

  public int brojNocenja() {return brojNocenja;}

  public int brojDorucka() {return brojDorucka;}

  public int brojRuckova() {return brojRuckova;}

  public int brojVecera() {return brojVecera;}

  public float doplataZaJednokrevetnuSobu() {return doplataZaJednokrevetnuSobu;}

  public List<String> prijevoz() {return prijevoz;}

  public void setOznaka(int oznaka) {this.oznaka = oznaka;}

  public void setNaziv(String naziv) {this.naziv = naziv;}

  public void setProgram(String program) {this.program = program;}

  public void setPocetniDatum(LocalDate pocetniDatum) {this.pocetniDatum = pocetniDatum;}

  public void setZavrsniDatum(LocalDate zavrsniDatum) {this.zavrsniDatum = zavrsniDatum;}

  public void setVrijemeKretanja(
      LocalTime vrijemeKretanja) {this.vrijemeKretanja = vrijemeKretanja;}

  public void setVrijemePovratka(
      LocalTime vrijemePovratka) {this.vrijemePovratka = vrijemePovratka;}

  public void setCijena(float cijena) {this.cijena = cijena;}

  public void setMinBrojPutnika(int minBrojPutnika) {this.minBrojPutnika = minBrojPutnika;}

  public void setMaxBrojPutnika(int maxBrojPutnika) {this.maxBrojPutnika = maxBrojPutnika;}

  public void setBrojNocenja(int brojNocenja) {this.brojNocenja = brojNocenja;}

  public void setBrojDorucka(int brojDorucka) {this.brojDorucka = brojDorucka;}

  public void setBrojRuckova(int brojRuckova) {this.brojRuckova = brojRuckova;}

  public void setBrojVecera(int brojVecera) {this.brojVecera = brojVecera;}

  public void setDoplataZaJednokrevetnuSobu(
      float doplataZaJednokrevetnuSobu) {this.doplataZaJednokrevetnuSobu = doplataZaJednokrevetnuSobu;}

  public void setPrijevoz(List<String> prijevoz) {this.prijevoz = prijevoz;}

  // endregion

}