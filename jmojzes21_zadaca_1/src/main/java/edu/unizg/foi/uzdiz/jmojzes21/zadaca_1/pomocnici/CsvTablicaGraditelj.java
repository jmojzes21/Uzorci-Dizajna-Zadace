package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;

import java.util.ArrayList;
import java.util.List;

public class CsvTablicaGraditelj {

  private final List<CsvRedak> csvRedci;
  private int potrebnoPodataka = 0;

  public CsvTablicaGraditelj() {
    csvRedci = new ArrayList<>();
  }

  public void dodajRedak(List<String> podaci) throws Exception {

    if (podaci.isEmpty()) {
      throw new Exception("Csv redak je prazan");
    }

    if (potrebnoPodataka == 0) {
      potrebnoPodataka = podaci.size();
    } else {
      if (podaci.size() != potrebnoPodataka) {
        throw new Exception("Csv redak nema točan broj podataka");
      }
    }

    var csvRedak = new CsvRedak(podaci);
    csvRedci.add(csvRedak);
  }

  public CsvTablica dajTablicu() {
    return new CsvTablica(csvRedci);
  }

}
