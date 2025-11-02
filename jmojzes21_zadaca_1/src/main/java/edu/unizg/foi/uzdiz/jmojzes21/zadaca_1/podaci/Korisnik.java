package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

import java.util.Objects;

public class Korisnik {

  private String ime;
  private String prezime;

  public Korisnik(String ime, String prezime) {
    this.ime = ime;
    this.prezime = prezime;
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Korisnik korisnik)) {return false;}
    return ime.equals(korisnik.ime) && prezime.equals(korisnik.prezime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ime, prezime);
  }

  public String ime() {return ime;}

  public String prezime() {return prezime;}

  public void setIme(String ime) {this.ime = ime;}

  public void setPrezime(String prezime) {this.prezime = prezime;}

}
