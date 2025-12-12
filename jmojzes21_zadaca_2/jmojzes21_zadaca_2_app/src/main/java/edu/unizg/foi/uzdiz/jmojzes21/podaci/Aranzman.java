package edu.unizg.foi.uzdiz.jmojzes21.podaci;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.AranzmanAktivan;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.AranzmanPopunjen;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.AranzmanStanje;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.AranzmanUPripremi;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Turistički aranžman.
 */
public class Aranzman extends RezervacijaComposite implements RezervacijaSubject, RezervacijaObserver {

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
  public void dodaj(RezervacijaComponent r) {
    if (!(r instanceof Rezervacija)) {
      throw new RuntimeException("Nije moguće dodati " + r.getClass().getName() + " u turistički aranžman!");
    }
    r.postaviRoditelja(this);
    djeca.add(r);
  }

  @Override
  public void ukloni(RezervacijaComponent r) {
    djeca.remove(r);
  }

  public void zaprimiRezervaciju(Rezervacija rezervacija) throws Exception {
    stanje.zaprimiRezervaciju(this, rezervacija);
  }

  public void aktiviraj() throws Exception {
    stanje.aktiviraj(this);
  }

  @Override
  public void kadaAktiviranaRezervacija(Rezervacija aktivirana) {
    List<Rezervacija> rezervacije = aktivneRezervacije();
    for (Rezervacija r : rezervacije) {
      r.kadaAktiviranaRezervacija(aktivirana);
    }
  }

  public void provjeriAktivneRezervacije() {
    if (jeAktivan()) {
      int brojAktivnih = brojAktivnih();
      if (brojAktivnih < minBrojPutnika) {
        List<Rezervacija> aktivne = aktivneRezervacije();
        for (var r : aktivne) {
          r.zaprimi();
        }
        postaviStanje(new AranzmanUPripremi());
      }
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
  public void obavijestiAktiviranjeRezervacije(Rezervacija aktivirana) {
    for (var promatrac : promatraci) {
      promatrac.kadaAktiviranaRezervacija(aktivirana);
    }
  }

  public AranzmanStanje stanje() {
    return stanje;
  }

  public void postaviStanje(AranzmanStanje stanje) {
    this.stanje = stanje;
  }

  public String nazivStanja() {
    return stanje.dajNaziv();
  }
  
  public boolean jeUPripremi() {
    return stanje instanceof AranzmanUPripremi;
  }

  public boolean jeAktivan() {
    return stanje instanceof AranzmanAktivan;
  }

  public boolean jePopunjen() {
    return stanje instanceof AranzmanPopunjen;
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