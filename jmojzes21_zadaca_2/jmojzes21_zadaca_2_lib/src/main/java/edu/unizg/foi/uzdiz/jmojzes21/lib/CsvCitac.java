package edu.unizg.foi.uzdiz.jmojzes21.lib;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Omogućuje parsiranje csv zapisa.
 */
public class CsvCitac {

  /**
   * Znak odvajanja csv retka.
   */
  private final char znakOdvajanja;

  /**
   * Lista svih csv redaka.
   */
  private final List<CsvRedak> csvRedci = new ArrayList<>();

  /**
   * Trenutna linija koja se obrađuje.
   */
  String linija = null;

  /**
   * Broj trenutne linije koja se obrađuje.
   */
  int brojLinije = 0;

  /**
   * Trenutna pozicija znaka unutar linije koja se obrađuje.
   */
  int pozicija = 0;

  /**
   * Kreiraj objekt sa zadanim znakom odvajanja ",".
   */
  public CsvCitac() {
    this(',');
  }

  /**
   * Kreiraj objekt sa znakom odvajanja
   *
   * @param znakOdvajanja znak odvajanja
   */
  public CsvCitac(char znakOdvajanja) {
    this.znakOdvajanja = znakOdvajanja;
  }

  /**
   * Učitaj i parsiraj csv zapis
   *
   * @param csv csv zapis
   */
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

  /**
   * Vraća sve csv retke koji su učitani iz csv zapisa.
   *
   * @return lista csv redaka
   */
  public CsvRedakIterator csvRedci() {
    return new CsvRedakIterator(csvRedci);
  }

  /**
   * Obradi trenutnu csv liniju.
   */
  private void obradiLiniju() {

    pozicija = 0;

    if (linija.isEmpty() || linija.startsWith("#")) {
      return;
    }

    List<String> elementi = parsirajElementeLinije();

    CsvRedak redak = new CsvRedak(linija, brojLinije, elementi);
    csvRedci.add(redak);
  }

  /**
   * Parsiraj elemente trenutne csv linije
   *
   * @return lista elemenata
   */
  private List<String> parsirajElementeLinije() {
    List<String> elementi = new ArrayList<>();

    pozicija = 0;
    String element = null;

    while (pozicija != -1) {
      elementi.add(dajSljedeciElement());
    }

    return elementi;
  }

  /**
   * Vraća sljedeći element unutar trenutne csv linije.
   *
   * @return sljedeći element ili null ako je prazni stupac
   */
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

  /**
   * Postavlja poziciju unutar trenutne linije na prvi znak koji nije razmka.
   */
  private void preskociRazmake() {
    while (linija.charAt(pozicija) == ' ') {
      pozicija++;
    }
  }

}
