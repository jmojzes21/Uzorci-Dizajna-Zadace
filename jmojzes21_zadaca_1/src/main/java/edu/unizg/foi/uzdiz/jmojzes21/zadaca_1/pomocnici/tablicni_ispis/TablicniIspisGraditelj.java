package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.tablicni_ispis;

import java.util.ArrayList;
import java.util.List;

public class TablicniIspisGraditelj {

  private final List<StupacTablice> stupci;

  public TablicniIspisGraditelj() {
    stupci = new ArrayList<>();
  }

  public TablicniIspisGraditelj dodajStupac(String naziv, int sirina) {
    return dodajStupac(naziv, sirina, StupacTablice.PORAVNANJE_LIJEVO);
  }

  public TablicniIspisGraditelj dodajStupac(String naziv, int sirina, int poravnanje) {
    stupci.add(new StupacTablice(naziv, sirina, poravnanje));
    return this;
  }

  public TablicniIspisGraditelj prikazujStupac(boolean prikazi) {
    stupci.getLast().setPrikaziStupac(prikazi);
    return this;
  }

  public TablicniIspis napravi() {
    return new TablicniIspis(stupci);
  }

}
