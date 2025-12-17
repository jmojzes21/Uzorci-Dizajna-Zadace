package edu.unizg.foi.uzdiz.jmojzes21.lib.csv;

import java.util.List;

public class CsvStupacIterator {

  private final List<String> stupci;
  private int pozicija = 0;

  public CsvStupacIterator(List<String> stupci) {
    this.stupci = stupci;
  }

  public boolean imaSljedeci() {
    return pozicija < stupci.size();
  }

  public String sljedeci() {
    return stupci.get(pozicija++);
  }

}
