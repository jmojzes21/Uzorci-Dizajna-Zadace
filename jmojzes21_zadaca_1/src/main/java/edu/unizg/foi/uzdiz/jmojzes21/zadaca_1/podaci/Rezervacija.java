package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

import java.time.LocalDateTime;

public class Rezervacija {

  private String ime;
  private String prezime;

  public int oznakaAranzmana;
  public LocalDateTime datumVrijeme;

  public Rezervacija() {}

  public String ime() {return ime;}

  public String prezime() {return prezime;}

  public int oznakaAranzmana() {return oznakaAranzmana;}

  public LocalDateTime vrijeme() {return datumVrijeme;}

  public void setIme(String ime) {this.ime = ime;}

  public void setPrezime(String prezime) {this.prezime = prezime;}

  public void setOznakaAranzmana(int oznakaAranzmana) {this.oznakaAranzmana = oznakaAranzmana;}

  public void setDatumVrijeme(LocalDateTime datumVrijeme) {this.datumVrijeme = datumVrijeme;}

}
