package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import java.time.LocalDate;

/**
 * Direktor za izgradnju turističkih aranžmana.
 */
public class AranzmanDirektor {

  private final AranzmanGraditelj graditelj;

  public AranzmanDirektor(AranzmanGraditelj graditelj) {
    this.graditelj = graditelj;
  }

  /**
   * Napravi turistički aranžman s obaveznim podacima.
   *
   * @param oznaka       oznaka
   * @param naziv        naziv
   * @param program      program
   * @param pocetniDatum početni datum
   * @param zavrsniDatum završni datum
   * @param cijena       cijena
   * @param minPutnika   min broj putnika
   * @param maxPutnika   max borj putnika
   * @return turistički aranžman
   */
  public Aranzman napraviAranzman(int oznaka, String naziv, String program, LocalDate pocetniDatum,
      LocalDate zavrsniDatum, float cijena, int minPutnika, int maxPutnika) throws Exception {
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
