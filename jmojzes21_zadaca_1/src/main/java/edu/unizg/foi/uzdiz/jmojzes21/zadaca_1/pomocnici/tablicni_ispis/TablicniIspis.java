package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.tablicni_ispis;

import java.util.List;

public class TablicniIspis {

  private final List<StupacTablice> stupci;

  public TablicniIspis(List<StupacTablice> stupci) {
    this.stupci = stupci;
  }

  public void ispisiZaglavlje() {
    for (var stupac : stupci) {
      if (!stupac.prikaziStupac()) {continue;}
      String sadrzaj = formatirajSadrzaj(stupac, stupac.naziv());
      System.out.print(sadrzaj);
    }
    System.out.println("|");
  }

  public void ispisi(List<String[]> redovi) {
    for (var red : redovi) {
      ispisi(red);
    }
  }

  public void ispisi(String[] red) {
    if (red.length != stupci.size()) {
      throw new IllegalArgumentException(
          "Broj stupaca retka mora biti jednak broju stupaca tablice!");
    }

    for (int i = 0; i < stupci.size(); i++) {
      if (!stupci.get(i).prikaziStupac()) {continue;}
      String sadrzaj = formatirajSadrzaj(stupci.get(i), red[i]);
      System.out.print(sadrzaj);
    }
    System.out.println("|");
  }

  private String formatirajSadrzaj(StupacTablice stupac, String sadrzaj) {
    int sirina = stupac.sirina();

    if (sadrzaj.length() > sirina) {
      sadrzaj = sadrzaj.substring(0, sirina - 3) + "...";
    }
    String format = "| %" + stupac.poravnanje() * sirina + "s ";
    return String.format(format, sadrzaj);
  }

}
