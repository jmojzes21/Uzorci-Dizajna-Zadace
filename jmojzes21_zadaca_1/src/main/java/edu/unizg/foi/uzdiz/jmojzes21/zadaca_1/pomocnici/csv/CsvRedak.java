package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.FormatDatuma;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

  public boolean postojiSadrzaj(int i) {
    return elementi.get(i) != null;
  }

  public String dajString(int i) throws CsvFormatGreska {
    return dajElement(i);
  }

  public String dajString(int i, String zadanaVrijednost) {
    return elementi.get(i) != null ? elementi.get(i) : zadanaVrijednost;
  }

  public int dajInt(int i) throws CsvFormatGreska {
    String element = dajElement(i);
    try {
      return Integer.parseInt(element);
    } catch (NumberFormatException e) {
      String opis = String.format("Nije moguće pretvoriti %s u broj!", element);
      throw new CsvFormatGreska(opis, this);
    }
  }

  public int dajInt(int i, int zadanaVrijednost) throws CsvFormatGreska {
    return elementi.get(i) != null ? dajInt(i) : zadanaVrijednost;
  }

  public float dajFloat(int i) throws CsvFormatGreska {
    String element = dajElement(i);
    try {
      return Float.parseFloat(element);
    } catch (NumberFormatException e) {
      String opis = String.format("Nije moguće pretvoriti %s u broj!", element);
      throw new CsvFormatGreska(opis, this);
    }
  }

  public float dajFloat(int i, float zadanaVrijednost) throws CsvFormatGreska {
    return elementi.get(i) != null ? dajFloat(i) : zadanaVrijednost;
  }

  public LocalDate dajDatum(int i) throws CsvFormatGreska {
    String element = dajElement(i);
    try {
      return FormatDatuma.dajInstancu().parsirajDatum(element);
    } catch (Exception e) {
      String opis = String.format("Nije moguće pretvoriti %s u datum!", element);
      throw new CsvFormatGreska(opis, this);
    }
  }

  public LocalDate dajDatum(int i, LocalDate zadanaVrijednost) throws CsvFormatGreska {
    return elementi.get(i) != null ? dajDatum(i) : zadanaVrijednost;
  }

  public LocalTime dajVrijeme(int i) throws CsvFormatGreska {
    String element = dajElement(i);
    try {
      return FormatDatuma.dajInstancu().parsirajVrijeme(element);
    } catch (Exception e) {
      String opis = String.format("Nije moguće pretvoriti %s u vrijeme!", element);
      throw new CsvFormatGreska(opis, this);
    }
  }

  public LocalTime dajVrijeme(int i, LocalTime zadanaVrijednost) throws CsvFormatGreska {
    return elementi.get(i) != null ? dajVrijeme(i) : zadanaVrijednost;
  }

  public LocalDateTime dajDatumVrijeme(int i) throws CsvFormatGreska {
    String element = dajElement(i);
    try {
      return FormatDatuma.dajInstancu().parsirajDatumVrijeme(element);
    } catch (Exception e) {
      String opis = String.format("Nije moguće pretvoriti %s u datum vrijeme!", element);
      throw new CsvFormatGreska(opis, this);
    }
  }

  public LocalDateTime dajDatumVrijeme(int i, LocalDateTime zadanaVrijednost)
      throws CsvFormatGreska {
    return elementi.get(i) != null ? dajDatumVrijeme(i) : zadanaVrijednost;
  }

  public String linija() {return linija;}

  public int brojLinije() {return brojLinije;}

  public List<String> elementi() {return elementi;}

  private String dajElement(int i) throws CsvFormatGreska {
    String element = elementi.get(i);
    if (element == null) {
      String opis = String.format("Csv redak nema podatak za indeks %d!", i);
      throw new CsvFormatGreska(opis, this);
    }
    return element;
  }

}
