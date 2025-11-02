package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

import java.time.LocalDateTime;

public class Rezervacija {

  private Korisnik korisnik;

  private int oznakaAranzmana;
  private LocalDateTime datumVrijeme;

  public Rezervacija(Korisnik korisnik, int oznakaAranzmana, LocalDateTime datumVrijeme) {
    this.korisnik = korisnik;
    this.oznakaAranzmana = oznakaAranzmana;
    this.datumVrijeme = datumVrijeme;
  }

  public Rezervacija(Rezervacija r) {
    korisnik = r.korisnik;
    oznakaAranzmana = r.oznakaAranzmana;
    datumVrijeme = r.datumVrijeme;
  }

  public Korisnik korisnik() {return korisnik;}

  public int oznakaAranzmana() {return oznakaAranzmana;}

  public LocalDateTime datumVrijeme() {return datumVrijeme;}

  public String vrsta() {return null;}

  public void setKorisnik(Korisnik korisnik) {this.korisnik = korisnik;}

  public void setOznakaAranzmana(int oznakaAranzmana) {this.oznakaAranzmana = oznakaAranzmana;}

  public void setDatumVrijeme(LocalDateTime datumVrijeme) {this.datumVrijeme = datumVrijeme;}

}
