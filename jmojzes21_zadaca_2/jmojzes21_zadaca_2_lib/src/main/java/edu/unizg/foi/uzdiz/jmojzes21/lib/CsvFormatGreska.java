package edu.unizg.foi.uzdiz.jmojzes21.lib;

public class CsvFormatGreska extends Exception {

  public CsvFormatGreska(String opis, int brojLinije, String linija) {
    super(String.format("Linija %d: %s\n%s", brojLinije, opis, linija));
  }

  public CsvFormatGreska(String opis, CsvRedak csvRedak) {
    this(opis, csvRedak.brojLinije(), csvRedak.linija());
  }

}
