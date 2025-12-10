package edu.unizg.foi.uzdiz.jmojzes21.podaci;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Turistički aranžman.
 */
public class Aranzman extends RezervacijaComposite {

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

  public Aranzman(int oznaka, String naziv) {
    this.oznaka = oznaka;
    this.naziv = naziv;
  }

  @Override
  protected void dodaj(RezervacijaComponent r) {
    if (!(r instanceof Rezervacija)) {
      throw new RuntimeException("Nije moguće dodati " + r.getClass().getName() + " u turistički aranžman!");
    }
    djeca.add(r);
  }

  public void zaprimiRezervaciju(Rezervacija rezervacija) {
    rezervacija.zaprimi();
    dodaj(rezervacija);
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