package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;

import java.util.HashMap;
import java.util.Map;

public class CitacOpcija {

  private final HashMap<String, String> opcije = new HashMap<>();

  public void ucitajOpcije(String[] argumenti) throws Exception {
    int i = 0;
    
    while (i < argumenti.length) {

      String arg = argumenti[i++];

      if (!arg.startsWith("--")) {
        String opis = String.format("Neispravni argument %s", arg);
        throw new Exception(opis);
      }

      String vrijednost = null;

      if (i < argumenti.length && !argumenti[i].startsWith("--")) {
        vrijednost = argumenti[i++];
      }

      opcije.put(arg, vrijednost);

    }

  }

  public Map<String, String> opcije() {return opcije;}

  public String dajVrijednost(String naziv) throws Exception {
    String vrijednost = opcije.get(naziv);
    if (vrijednost == null) {
      throw new Exception(String.format("Opcija %s mora imati vrijednost!", naziv));
    }

    vrijednost = vrijednost.trim();
    if (vrijednost.isEmpty()) {
      throw new Exception(String.format("Opcija %s ne može imati praznu vrijednost!", naziv));
    }

    return vrijednost;
  }

}
