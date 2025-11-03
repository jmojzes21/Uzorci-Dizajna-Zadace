package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.tablicni_ispis;

public class StupacTablice {

  public static final int PORAVNANJE_LIJEVO = -1;
  public static final int PORAVNANJE_DESNO = 1;

  private final String naziv;
  private final int sirina;
  private final int poravnanje;

  private boolean prikaziStupac = true;

  public StupacTablice(String naziv, int sirina, int poravnanje) {
    if (sirina < 4) {
      throw new IllegalArgumentException("Širina stupca mora biti barem 4!");
    }

    if (poravnanje != PORAVNANJE_LIJEVO && poravnanje != PORAVNANJE_DESNO) {
      throw new IllegalArgumentException("Poravnanje nije valjano!");
    }

    this.naziv = naziv;
    this.sirina = sirina;
    this.poravnanje = poravnanje;
  }

  public String naziv() {return naziv;}

  public int sirina() {return sirina;}

  public int poravnanje() {return poravnanje;}

  public boolean prikaziStupac() {return prikaziStupac;}

  public void setPrikaziStupac(boolean prikaziStupac) {this.prikaziStupac = prikaziStupac;}

}
