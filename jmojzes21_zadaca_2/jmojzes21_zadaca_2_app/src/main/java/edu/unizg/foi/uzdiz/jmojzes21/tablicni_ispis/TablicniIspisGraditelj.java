package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Omogućuje izgradnju tabličnog ispisa.
 *
 * @param <T> tip klase za koju se odnosi tablični ispis
 */
public class TablicniIspisGraditelj<T> {

  private final List<StupacTablice<T>> stupci;

  public TablicniIspisGraditelj() {
    stupci = new ArrayList<>();
  }

  /**
   * Dodaj novi stupac u tablicu.
   *
   * @param naziv         naziv stupca u zaglavlju tablice
   * @param sirina        širina stupca
   * @param dajVrijednost povratna funkcija koja prikazuje vrijednost stupca iz objekta
   * @return graditelj
   */
  public TablicniIspisGraditelj<T> dodajStupac(String naziv, int sirina,
      Function<T, String> dajVrijednost) {
    stupci.add(new StupacTablice<>(naziv, sirina, dajVrijednost, Poravnanje.lijevo));
    return this;
  }

  /**
   * Poravnaj zadnje dodani stupac u lijevo.
   *
   * @return graditelj
   */
  public TablicniIspisGraditelj<T> poravnajLijevo() {
    setPoravnanje(Poravnanje.lijevo);
    return this;
  }

  /**
   * Poravnaj zadnje dodani stupac u desno.
   *
   * @return graditelj
   */
  public TablicniIspisGraditelj<T> poravnajDesno() {
    setPoravnanje(Poravnanje.desno);
    return this;
  }

  /**
   * Poravnaj zadnje dodani stupac.
   *
   * @param poravnanje poravnanje
   * @return graditelj
   */
  public TablicniIspisGraditelj<T> setPoravnanje(Poravnanje poravnanje) {
    if (stupci.isEmpty()) {
      throw new IllegalStateException("Nema stupaca!");
    }
    stupci.getLast().setPoravnanje(poravnanje);
    return this;
  }

  /**
   * Postavi prikazuje li se zadnje dodani stupac.
   *
   * @param prikazi prikazi stupac ili ne
   * @return graditelj
   */
  public TablicniIspisGraditelj<T> prikazujStupac(boolean prikazi) {
    if (stupci.isEmpty()) {
      throw new IllegalStateException("Nema stupaca!");
    }
    stupci.getLast().setPrikaziStupac(prikazi);
    return this;
  }

  /**
   * Napravi tablični ispis
   *
   * @return tablični ispis
   */
  public TablicniIspis<T> napravi() {
    return new TablicniIspis<T>(stupci);
  }

}
