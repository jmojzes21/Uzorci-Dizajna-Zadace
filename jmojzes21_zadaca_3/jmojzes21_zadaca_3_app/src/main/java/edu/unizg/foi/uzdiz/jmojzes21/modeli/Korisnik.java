package edu.unizg.foi.uzdiz.jmojzes21.modeli;

import edu.unizg.foi.uzdiz.jmojzes21.logika.prototype.Prototype;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import java.util.Objects;

/**
 * Sadrži podatke o korisniku (osobi).
 */
public class Korisnik implements RezervacijaObserver, Prototype<Korisnik> {

  private String ime;
  private String prezime;

  public Korisnik(String ime, String prezime) {
    this.ime = ime;
    this.prezime = prezime;
  }

  @Override
  public void kadaPromjenaStanjaAranzmana(Aranzman aranzman) {
    System.out.printf("%s: Aranžman %d postao %s\n", punoIme(), aranzman.oznaka(), aranzman.nazivStanja());
  }

  @Override
  public void kadaPromjenaStanjaRezervacije(Rezervacija rezervacija) {
    var f = Formati.dajInstancu();
    System.out.printf("%s: Aranžman %d, rezervacija %s %s postala %s\n", punoIme(), rezervacija.oznakaAranzmana(),
        rezervacija.korisnik().punoIme(), f.formatiraj(rezervacija.vrijemePrijema()), rezervacija.nazivStanja());
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

  @Override
  public Korisnik kopiraj() {
    return new Korisnik(ime, prezime);
  }

  public String ime() {return ime;}

  public String prezime() {return prezime;}

  public void setIme(String ime) {this.ime = ime;}

  public void setPrezime(String prezime) {this.prezime = prezime;}

}
