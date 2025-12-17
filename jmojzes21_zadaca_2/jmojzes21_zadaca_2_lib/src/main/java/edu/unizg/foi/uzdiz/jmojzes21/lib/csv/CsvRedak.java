package edu.unizg.foi.uzdiz.jmojzes21.lib.csv;

import java.util.List;

/**
 * Sadrži informacije o csv retku.
 */
public class CsvRedak {

  private final String linija;
  private final int brojLinije;
  private final List<String> elementi;

  public CsvRedak(String linija, int brojLinije, List<String> elementi) {
    this.linija = linija;
    this.brojLinije = brojLinije;
    this.elementi = elementi;
  }

  public int brojElemenata() {
    return elementi.size();
  }

  public String linija() {return linija;}

  public int brojLinije() {return brojLinije;}

  public List<String> elementi() {return elementi;}

  public CsvStupacIterator dajIteratorStupaca() {
    return new CsvStupacIterator(elementi);
  }
  
}
