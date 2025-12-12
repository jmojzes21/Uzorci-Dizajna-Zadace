package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

import java.util.function.Function;

/**
 * Stupac tablice kojeg koristi tablični ispis.
 *
 * @param <T> tip klase za koju se odnosi tablični ispis
 */
public class StupacTablice<T> {

  private String naziv;
  private int sirina;
  private Function<T, String> dajVrijednost;

  private Poravnanje poravnanje;
  private boolean prikaziStupac = true;

  public StupacTablice(String naziv, int sirina, Function<T, String> dajVrijednost,
      Poravnanje poravnanje) {
    setNaziv(naziv);
    setSirina(sirina);
    setDajVrijednost(dajVrijednost);
    setPoravnanje(poravnanje);
  }

  public String dajVrijednost(T o) {
    return dajVrijednost.apply(o);
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

  public void setDajVrijednost(Function<T, String> dajVrijednost) {
    this.dajVrijednost = dajVrijednost;
  }

  public void setPoravnanje(Poravnanje poravnanje) {
    this.poravnanje = poravnanje;
  }

  public void setPrikaziStupac(boolean prikaziStupac) {this.prikaziStupac = prikaziStupac;}

}
