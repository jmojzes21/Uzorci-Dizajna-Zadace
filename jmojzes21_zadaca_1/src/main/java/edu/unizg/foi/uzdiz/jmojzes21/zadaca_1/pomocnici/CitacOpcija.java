package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;

import java.util.HashMap;
import java.util.Map;

/**
 * Omogućuje čitanje opcija (argumenata) koji se koriste pri pokretanju programa.
 */
public class CitacOpcija {

  private final HashMap<String, String> opcije = new HashMap<>();

  /**
   * Učita i parsira argumente i sprema ih u mapu.
   *
   * @param argumenti lista argumenata
   * @throws Exception neispravni argument
   */
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

  /**
   * Vraća mapu opcija gdje je ključ naziv opcije.
   *
   * @return mapa opcija
   */
  public Map<String, String> opcije() {return opcije;}

  /**
   * Vraća vrijednost opcije. Opcija mora postojati i ne smije biti prazna.
   *
   * @param naziv naziv opcije
   * @return vrijednost opcije
   * @throws Exception ako opcija ne postoji, nema vrijednost, opcija ima praznu vrijednost
   */
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
