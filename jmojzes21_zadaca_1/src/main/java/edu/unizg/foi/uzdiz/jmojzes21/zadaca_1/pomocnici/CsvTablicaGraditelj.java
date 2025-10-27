package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;

import java.util.ArrayList;
import java.util.List;

public class CsvTablicaGraditelj {

  private final List<CsvRedak> csvRedci;
  private final int brojElemenataRetka;

  public CsvTablicaGraditelj(int brojElemenataRetka) {
    csvRedci = new ArrayList<>();
    this.brojElemenataRetka = brojElemenataRetka;
  }

  public void dodajRedak(List<String> podaci) throws Exception {

    if (podaci.isEmpty()) {
      throw new Exception("Nije moguće dodati prazak red!");
    }

    if (podaci.size() != brojElemenataRetka) {
      throw new Exception("Csv redak nema potreban broj elemenata!");
    }

    var csvRedak = new CsvRedak(podaci);
    csvRedci.add(csvRedak);
  }

  public CsvTablica dajTablicu() {
    return new CsvTablica(csvRedci);
  }

}
