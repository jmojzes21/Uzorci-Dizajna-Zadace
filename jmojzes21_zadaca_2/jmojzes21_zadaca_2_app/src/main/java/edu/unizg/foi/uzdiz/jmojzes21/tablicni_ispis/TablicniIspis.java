package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

import java.util.List;

/**
 * Omogućuje ispis u obliku tablice za neki tip klase
 *
 * @param <T> tip klase za koju se odnosi tablični ispis
 */
public class TablicniIspis<T> {

  private final List<StupacTablice<T>> stupci;

  public TablicniIspis(List<StupacTablice<T>> stupci) {
    this.stupci = stupci;
  }

  /**
   * Ispisuje zaglavlje tablice, odnosno nazive stupaca.
   */
  public void ispisiZaglavlje() {
    for (var stupac : stupci) {
      if (!stupac.prikaziStupac()) {continue;}
      String sadrzaj = formatirajSadrzaj(stupac, stupac.naziv());
      System.out.print(sadrzaj);
    }
    System.out.println("|");
  }

  /**
   * Ispiši sve elemente svaki u jedan red.
   *
   * @param elementi elementi
   */
  public void ispisi(List<T> elementi) {
    for (var e : elementi) {
      ispisi(e);
    }
  }

  /**
   * Ispiši jedan element u jedan red.
   *
   * @param element element
   */
  public void ispisi(T element) {
    for (StupacTablice<T> stupac : stupci) {
      if (!stupac.prikaziStupac()) {continue;}
      String sadrzaj = formatirajSadrzaj(stupac, stupac.dajVrijednost(element));
      System.out.print(sadrzaj);
    }
    System.out.println("|");
  }

  private String formatirajSadrzaj(StupacTablice<T> stupac, String sadrzaj) {
    int sirina = stupac.sirina();

    if (sadrzaj.length() > sirina) {
      sadrzaj = sadrzaj.substring(0, sirina - 3) + "...";
    }

    int poravnanje = switch (stupac.poravnanje()) {
      case Poravnanje.lijevo -> -1;
      case Poravnanje.desno -> 1;
    };

    String format = "| %" + poravnanje * sirina + "s ";
    return String.format(format, sadrzaj);
  }

}
