package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv;

import java.util.List;

public class CsvRedak {

  private final String linija;
  private final int brojLinije;
  private final List<String> elementi;

  public CsvRedak(String linija, int brojLinije, List<String> elementi) {
    this.linija = linija;
    this.brojLinije = brojLinije;
    this.elementi = elementi;
  }

  public String linija() {return linija;}

  public int brojLinije() {return brojLinije;}

  public List<String> elementi() {return elementi;}

}
