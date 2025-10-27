package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv;

import java.util.List;

public class CsvTablica {

  private final List<CsvRedak> redci;

  public CsvTablica(List<CsvRedak> redci) {
    this.redci = redci;
  }

  public List<CsvRedak> redci() {return redci;}
}
