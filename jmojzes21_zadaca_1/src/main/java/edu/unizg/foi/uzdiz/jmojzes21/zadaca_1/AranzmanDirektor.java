package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import java.time.LocalDate;

public class AranzmanDirektor {

  private final AranzmanGraditelj graditelj;

  public AranzmanDirektor() {
    graditelj = new AranzmanGraditelj();
  }

  public Aranzman napraviAranzman(int oznaka, String naziv, LocalDate pocetniDatum, LocalDate zavrsniDatum,
      int minPutnika, int maxPutnika) {
    return graditelj.napraviAranzman(oznaka, naziv)
        .setPocetniDatum(pocetniDatum)
        .setZavrsniDatum(zavrsniDatum)
        .setMinBrojPutnika(minPutnika)
        .setMaxBrojPutnika(maxPutnika)
        .dajAranzman();
  }

}
