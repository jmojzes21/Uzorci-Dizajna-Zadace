package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.EvidencijaGresaka;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CsvCitac {

  private final char znakOdvajanja;
  private final boolean preskociPrviRedak;

  private final List<CsvRedak> csvRedci = new ArrayList<>();

  public CsvCitac() {
    this(',', true);
  }

  public CsvCitac(char znakOdvajanja, boolean preskociPrviRedak) {
    this.znakOdvajanja = znakOdvajanja;
    this.preskociPrviRedak = preskociPrviRedak;
  }

  public void ucitajCsv(String csv) {

    BufferedReader citac = new BufferedReader(new StringReader(csv));
    List<String> linije = citac.lines().map(String::trim).toList();

    boolean preskociPrviRedak = this.preskociPrviRedak;

    for (int i = 0; i < linije.size(); i++) {
      String linija = linije.get(i);
      int brojLinije = i + 1;

      try {
        CsvRedak redak = obradiLiniju(linija, brojLinije);
        if (preskociPrviRedak) {
          preskociPrviRedak = false;
          continue;
        }

        csvRedci.add(redak);
      } catch (CsvFormatGreska e) {
        EvidencijaGresaka.dajInstancu().evidentiraj(e);
      }

    }

  }

  public List<CsvRedak> csvRedci() {return csvRedci;}

  private CsvRedak obradiLiniju(String linija, int brojLinije) throws CsvFormatGreska {
    List<String> elementi = parsirajElementeLinije(linija, brojLinije);
    return new CsvRedak(linija, brojLinije, elementi);
  }

  private List<String> parsirajElementeLinije(String linija, int brojLinije)
      throws CsvFormatGreska {

    int trenutnaPozicija = 0;
    List<String> elementi = new ArrayList<>();

    while (true) {

      if (trenutnaPozicija >= linija.length()) {
        elementi.add(null);
        break;
      }

      trenutnaPozicija = preskociRazmake(linija, trenutnaPozicija);

      int pozicijaOdvajanja = -1;
      int pocetakVrijednosti = trenutnaPozicija;
      int krajVrijednosti = -1;

      if (linija.charAt(trenutnaPozicija) == '"') {
        pocetakVrijednosti = trenutnaPozicija + 1;
        krajVrijednosti = linija.indexOf('"', pocetakVrijednosti);

        if (krajVrijednosti == -1) {
          String opis = "Nije moguće pronaći kraj vrijednosti!";
          throw new CsvFormatGreska(opis, brojLinije, linija);
        }

        pozicijaOdvajanja = linija.indexOf(znakOdvajanja, krajVrijednosti + 1);

      } else {
        pozicijaOdvajanja = linija.indexOf(znakOdvajanja, trenutnaPozicija);
        krajVrijednosti = pozicijaOdvajanja;
      }

      if (pozicijaOdvajanja == -1) {
        String zadnjiPodatak = linija.substring(pocetakVrijednosti).trim();
        elementi.add(zadnjiPodatak);
        break;
      }

      if (pozicijaOdvajanja != trenutnaPozicija) {
        String podatak = linija.substring(pocetakVrijednosti, krajVrijednosti).trim();
        elementi.add(podatak);
      } else {
        elementi.add(null);
      }

      trenutnaPozicija = pozicijaOdvajanja + 1;

    }

    return elementi;
  }

  private int preskociRazmake(String linija, int pocetnaPozicija) {
    int pozicija = pocetnaPozicija;
    while (linija.charAt(pozicija) == ' ') {
      pozicija++;
    }
    return pozicija;
  }

}
