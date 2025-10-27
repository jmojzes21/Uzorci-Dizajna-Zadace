package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv;

public class CsvFormatGreska extends Exception {

  public CsvFormatGreska(String opis, int brojLinije, String linija) {
    super(String.format("Linija %d: %s\n%s", brojLinije, opis, linija));
  }
  
}
