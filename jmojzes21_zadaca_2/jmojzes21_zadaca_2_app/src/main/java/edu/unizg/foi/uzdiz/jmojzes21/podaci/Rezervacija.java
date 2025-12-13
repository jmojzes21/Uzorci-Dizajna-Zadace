package edu.unizg.foi.uzdiz.jmojzes21.podaci;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaNaCekanju;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaNova;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaOdgodjena;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaOtkazana;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaPrimljena;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaStanje;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class Rezervacija extends RezervacijaComponent implements RezervacijaObserver {

  private Korisnik korisnik;

  private int oznakaAranzmana;
  private LocalDateTime vrijemePrijema;
  private LocalDateTime vrijemeOtkaza;

  private RezervacijaStanje stanje;

  public Rezervacija(Korisnik korisnik, int oznakaAranzmana, LocalDateTime vrijemePrijema) {
    this.korisnik = korisnik;
    this.oznakaAranzmana = oznakaAranzmana;
    this.vrijemePrijema = vrijemePrijema;
    vrijemeOtkaza = null;

    stanje = new RezervacijaNova();
  }

  public void zaprimi() {
    stanje.zaprimi(this);
  }

  public void aktiviraj() {
    stanje.aktiviraj(this);
  }

  public void staviNaCekanje() {
    stanje.staviNaCekanje(this);
  }

  public void odgodi() {
    stanje.odgodi(this);
  }

  @Override
  public void kadaAktiviranaRezervacija(Rezervacija aktivirana) {
    if (oznakaAranzmana == aktivirana.oznakaAranzmana) {return;}
    stanje.kadaAktiviranaRezervacija(this, aktivirana);
  }

  public RezervacijaStanje stanje() {
    return stanje;
  }

  public void postaviStanje(RezervacijaStanje stanje) {
    this.stanje = stanje;
  }

  public String nazivStanja() {
    return stanje.dajNaziv();
  }

  public boolean jePrimljena() {
    return stanje instanceof RezervacijaPrimljena;
  }

  public boolean jeAktivna() {
    return stanje instanceof RezervacijaAktivna;
  }

  public boolean jeNaCekanju() {
    return stanje instanceof RezervacijaNaCekanju;
  }

  public boolean jeOtkazana() {
    return stanje instanceof RezervacijaOtkazana;
  }

  public boolean jeOdgodjena() {
    return stanje instanceof RezervacijaOdgodjena;
  }

  public Aranzman dajAranzman() {
    return (Aranzman) dajRoditelja();
  }

  /**
   * Sortira rezervacije prema vremenu prijema.
   *
   * @param rezervacije rezervacije
   * @param uzlazno     ako je true sortira uzlazno, inače sortira silazno
   * @return sortirane rezervacije
   */
  public static List<Rezervacija> sortiraj(List<Rezervacija> rezervacije, boolean uzlazno) {
    var comparator = Comparator.comparing(Rezervacija::vrijemePrijema);
    if (!uzlazno) {comparator = comparator.reversed();}
    return rezervacije.stream().sorted(comparator).toList();
  }
  
  // region Metode za dohvaćanje i postavljanje atributa

  public Korisnik korisnik() {return korisnik;}

  public int oznakaAranzmana() {return oznakaAranzmana;}

  public LocalDateTime vrijemePrijema() {return vrijemePrijema;}

  public LocalDateTime vrijemeOtkaza() {return vrijemeOtkaza;}

  public void setKorisnik(Korisnik korisnik) {this.korisnik = korisnik;}

  public void setOznakaAranzmana(int oznakaAranzmana) {this.oznakaAranzmana = oznakaAranzmana;}

  public void setVrijemePrijema(LocalDateTime vrijemePrijema) {this.vrijemePrijema = vrijemePrijema;}

  public void setVrijemeOtkaza(LocalDateTime vrijemeOtkaza) {this.vrijemeOtkaza = vrijemeOtkaza;}

  // endregion

}
