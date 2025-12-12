package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.StupacTablice.Poravnanje;
import java.util.List;

/**
 * Omogućuje ispis u obliku tablice.
 */
public class TablicniIspis {

  private final List<StupacTablice> stupci;

  public TablicniIspis(List<StupacTablice> stupci) {
    this.stupci = stupci;
  }

  /**
   * Ispisuje zaglavlje tablice (nazive stupaca).
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
   * Ispisuje jedan redak tablice.
   *
   * @param podaci podaci koje je potrebno ispisati, broj elemenata mora biti jednak broju stupaca
   */
  public void ispisi(List<String> podaci) {

    if (podaci.size() != stupci.size()) {
      throw new RuntimeException("Broj elemenata mora biti jednak broju stupaca!");
    }

    for (int i = 0; i < stupci.size(); i++) {
      StupacTablice stupac = stupci.get(i);
      if (!stupac.prikaziStupac()) {continue;}

      String sadrzaj = podaci.get(i);
      System.out.print(formatirajSadrzaj(stupac, sadrzaj));
    }

    System.out.println("|");
  }

  private String formatirajSadrzaj(StupacTablice stupac, String sadrzaj) {
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
