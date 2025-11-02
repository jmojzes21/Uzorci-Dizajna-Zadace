package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CsvCitac {

  private final char znakOdvajanja;

  private final List<CsvRedak> csvRedci = new ArrayList<>();

  String linija = null;
  int brojLinije = 0;
  int pozicija = 0;

  public CsvCitac() {
    this(',');
  }

  public CsvCitac(char znakOdvajanja) {
    this.znakOdvajanja = znakOdvajanja;
  }

  public void ucitajCsv(String csv) {

    csvRedci.clear();
    linija = null;
    brojLinije = 0;
    pozicija = 0;

    if (csv.startsWith("\uFEFF")) {
      csv = csv.substring(1);
    }

    BufferedReader citac = new BufferedReader(new StringReader(csv));
    List<String> linije = citac.lines().map(String::trim).toList();

    for (int i = 0; i < linije.size(); i++) {
      linija = linije.get(i);
      brojLinije = i + 1;

      obradiLiniju();
    }

    linija = null;
    brojLinije = 0;
    pozicija = 0;
  }

  public List<CsvRedak> csvRedci() {return csvRedci;}

  private void obradiLiniju() {

    pozicija = 0;

    if (linija.isEmpty() || linija.startsWith("#")) {
      return;
    }

    List<String> elementi = parsirajElementeLinije();

    CsvRedak redak = new CsvRedak(linija, brojLinije, elementi);
    csvRedci.add(redak);
  }

  private List<String> parsirajElementeLinije() {
    List<String> elementi = new ArrayList<>();

    pozicija = 0;
    String element = null;

    while (pozicija != -1) {
      elementi.add(dajSljedeciElement());
    }

    return elementi;
  }

  private String dajSljedeciElement() {

    if (pozicija >= linija.length()) {
      pozicija = -1;
      return null;
    }

    preskociRazmake();

    int pozicijaOdvajanja;
    int pocetakElementa;
    int krajElementa;

    if (linija.charAt(pozicija) == '"') {
      pocetakElementa = pozicija + 1;
      krajElementa = linija.indexOf('"', pocetakElementa);

      if (krajElementa == -1) {
        krajElementa = linija.length();
        pozicijaOdvajanja = -1;
      } else {
        pozicijaOdvajanja = linija.indexOf(znakOdvajanja, krajElementa + 1);
      }

    } else {
      pozicijaOdvajanja = linija.indexOf(znakOdvajanja, pozicija);

      pocetakElementa = pozicija;
      krajElementa = pozicijaOdvajanja != -1 ? pozicijaOdvajanja : linija.length();
    }

    String element = linija.substring(pocetakElementa, krajElementa).trim();
    if (element.isEmpty()) {element = null;}

    if (pozicijaOdvajanja == -1) {
      pozicija = -1;
    } else {
      pozicija = pozicijaOdvajanja + 1;
    }

    return element;
  }

  private void preskociRazmake() {
    while (linija.charAt(pozicija) == ' ') {
      pozicija++;
    }
  }

}
