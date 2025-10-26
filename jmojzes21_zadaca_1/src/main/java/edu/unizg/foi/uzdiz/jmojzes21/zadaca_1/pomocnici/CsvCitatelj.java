package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvCitatelj {

  private char znakOdvajanja = ',';

  public CsvCitatelj() {}

  public void ucitajCsv(String csv) {

    try (BufferedReader citac = new BufferedReader(new StringReader(csv))) {

      int brojLinije = 1;

      while (true) {
        String linija = citac.readLine();
        if (linija == null) {break;}

        obradiLiniju(brojLinije, linija.trim());
        brojLinije++;
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  private void obradiLiniju(int brojLinije, String linija) {

    int trenutnaPozicija = 0;
    List<String> podaci = new ArrayList<>();

    while (true) {

      if (trenutnaPozicija >= linija.length()) {
        podaci.add("");
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
          System.out.println("Nije moguće pronaći kraj vrijednosti za csv liniju " + linija);
          return;
        }

        pozicijaOdvajanja = linija.indexOf(znakOdvajanja, krajVrijednosti + 1);

      } else {
        pozicijaOdvajanja = linija.indexOf(znakOdvajanja, trenutnaPozicija);
        krajVrijednosti = pozicijaOdvajanja;
      }

      if (pozicijaOdvajanja == -1) {
        String zadnjiPodatak = linija.substring(pocetakVrijednosti).trim();
        podaci.add(zadnjiPodatak);
        break;
      }

      if (pozicijaOdvajanja != trenutnaPozicija) {
        String podatak = linija.substring(pocetakVrijednosti, krajVrijednosti).trim();
        podaci.add(podatak);
      } else {
        podaci.add("");
      }

      trenutnaPozicija = pozicijaOdvajanja + 1;

    }

    System.out.println(Arrays.toString(podaci.toArray()));

  }

  private int preskociRazmake(String linija, int pocetnaPozicija) {
    int pozicija = pocetnaPozicija;
    while (linija.charAt(pozicija) == ' ') {
      pozicija++;
    }
    return pozicija;
  }

}
