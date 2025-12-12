package edu.unizg.foi.uzdiz.jmojzes21.lib.csv;

import java.util.List;

public class CsvRedakIterator {

  private List<CsvRedak> redci;
  private int pozicija = 0;

  public CsvRedakIterator(List<CsvRedak> redci) {
    this.redci = redci;
  }

  public boolean imaSljedeci() {
    return pozicija < redci.size();
  }

  public CsvRedak sljedeci() {
    return redci.get(pozicija++);
  }

}
