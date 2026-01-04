package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

/**
 * Stupac tablice kojeg koristi tablični ispis.
 */
public class StupacTablice {

  public enum Poravnanje {
    lijevo, desno;
  }

  private String naziv;
  private int sirina;

  private Poravnanje poravnanje;
  private boolean prikaziStupac = true;

  public StupacTablice(String naziv, int sirina, Poravnanje poravnanje) {
    setNaziv(naziv);
    setSirina(sirina);
    setPoravnanje(poravnanje);
  }

  public String naziv() {return naziv;}

  public int sirina() {return sirina;}

  public Poravnanje poravnanje() {return poravnanje;}

  public boolean prikaziStupac() {return prikaziStupac;}

  public void setNaziv(String naziv) {this.naziv = naziv;}

  public void setSirina(int sirina) {
    if (sirina < 4) {
      throw new IllegalArgumentException("Širina stupca mora biti barem 4!");
    }
    this.sirina = sirina;
  }

  public void setPoravnanje(Poravnanje poravnanje) {this.poravnanje = poravnanje;}

  public void setPrikaziStupac(boolean prikaziStupac) {this.prikaziStupac = prikaziStupac;}

}
