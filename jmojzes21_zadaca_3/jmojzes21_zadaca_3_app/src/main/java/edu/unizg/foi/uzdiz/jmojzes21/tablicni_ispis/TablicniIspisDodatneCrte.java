package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

import java.util.List;

public class TablicniIspisDodatneCrte extends TablicniIspisDecorator {

  private boolean ispisujDodatneCrte = true;

  public TablicniIspisDodatneCrte(ITablicniIspis tablicniIspis) {
    super(tablicniIspis);
  }

  @Override
  public void ispisiZaglavlje() {
    super.ispisiZaglavlje();
    ispisujDodatneCrte = false;
  }

  @Override
  public void ispisi(List<String> podaci) {
    if (ispisujDodatneCrte) {
      ispisiDodatnuCrtu();
    } else {
      ispisujDodatneCrte = true;
    }

    super.ispisi(podaci);
  }

  private void ispisiDodatnuCrtu() {
    var stupci = tablicniIspis.stupci();

    for (StupacTablice stupac : stupci) {
      if (!stupac.prikaziStupac()) {continue;}
      System.out.print("|");
      System.out.print("-".repeat(stupac.sirina() + 2));
    }
    System.out.println("|");
  }

}
