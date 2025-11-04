package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import java.time.LocalDate;

public class AranzmanDirektor {

  private final AranzmanGraditelj graditelj;

  public AranzmanDirektor(AranzmanGraditelj graditelj) {
    this.graditelj = graditelj;
  }

  public Aranzman napraviAranzman(int oznaka, String naziv, String program, LocalDate pocetniDatum,
      LocalDate zavrsniDatum, float cijena, int minPutnika, int maxPutnika) {
    return graditelj.napraviAranzman(oznaka, naziv)
        .setProgram(program)
        .setPocetniDatum(pocetniDatum)
        .setZavrsniDatum(zavrsniDatum)
        .setCijena(cijena)
        .setMinBrojPutnika(minPutnika)
        .setMaxBrojPutnika(maxPutnika)
        .dajAranzman();
  }

}
