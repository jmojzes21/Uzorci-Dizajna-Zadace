package edu.unizg.foi.uzdiz.jmojzes21.podaci;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaNaCekanju;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaNova;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaPrimljena;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja.RezervacijaStanje;
import java.time.LocalDateTime;

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

    if (jeAktivna() && korisnik.equals(aktivirana.korisnik)) {

      Aranzman aranzman1 = dajAranzman();
      Aranzman aranzman2 = aktivirana.dajAranzman();

      if (aranzman1.preklapaSe(aranzman2)) {
        System.out.println("Preklapanje " + korisnik.punoIme() + " " + oznakaAranzmana + " s rezervacijom "
            + aktivirana.oznakaAranzmana());

        Rezervacija zaOdgodu;

        if (vrijemePrijema.isBefore(aktivirana.vrijemePrijema)) {
          System.out.println("Rezervacija " + aktivirana.oznakaAranzmana + " postaje odgođena");
          zaOdgodu = aktivirana;
        } else {
          System.out.println("Rezervacija " + oznakaAranzmana + " postaje odgođena");
          zaOdgodu = this;
        }

        zaOdgodu.odgodi();
        zaOdgodu.dajAranzman().provjeriAktivneRezervacije();

      }

    }
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

  public Aranzman dajAranzman() {
    return (Aranzman) dajRoditelja();
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
