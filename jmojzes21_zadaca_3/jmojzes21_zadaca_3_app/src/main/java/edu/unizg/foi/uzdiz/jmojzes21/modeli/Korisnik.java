package edu.unizg.foi.uzdiz.jmojzes21.modeli;

import java.util.Objects;

/**
 * Sadrži podatke o korisniku (osobi).
 */
public class Korisnik implements RezervacijaObserver {

  private String ime;
  private String prezime;

  public Korisnik(String ime, String prezime) {
    this.ime = ime;
    this.prezime = prezime;
  }

  @Override
  public void kadaPromjenaStanjaAranzmana(Aranzman aranzman) {
    
  }

  @Override
  public void kadaPromjenaStanjaRezervacije(Rezervacija rezervacija) {

  }

  public String punoIme() {return ime + " " + prezime;}

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Korisnik korisnik)) {return false;}
    return ime.equals(korisnik.ime) && prezime.equals(korisnik.prezime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ime, prezime);
  }

  @Override
  public String toString() {
    return punoIme();
  }

  public String ime() {return ime;}

  public String prezime() {return prezime;}

  public void setIme(String ime) {this.ime = ime;}

  public void setPrezime(String prezime) {this.prezime = prezime;}

}
