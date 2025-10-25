package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;

import java.util.HashMap;

public class CitateljOpcija {

  private final HashMap<String, String> opcije = new HashMap<>();

  public void ucitajOpcije(String[] argumenti) {
    int i = 0;

    while (i < argumenti.length) {

      String arg = argumenti[i];

      if (arg.startsWith("--")) {
        String naziv = arg.substring(2);
        if (i + 1 >= argumenti.length) {
          throw new RuntimeException("Opcija " + arg + " nema vrijednost!");
        }

        String vrijednost = argumenti[i + 1];
        opcije.put(naziv, vrijednost);
      }

      i++;
    }

  }

  public String dajOpciju(String naziv) {
    return opcije.get(naziv);
  }

}
