package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.StupacTablice.Poravnanje;
import java.util.ArrayList;
import java.util.List;

/**
 * Omogućuje izgradnju tabličnog ispisa.
 */
public class TablicniIspisGraditelj {

  private final List<StupacTablice> stupci;

  private boolean prelamanjeTeksta = false;
  private boolean ispisDodatnihCrta = false;

  public TablicniIspisGraditelj() {
    stupci = new ArrayList<>();
  }

  /**
   * Dodaj novi stupac u tablicu.
   *
   * @param naziv  naziv stupca u zaglavlju tablice
   * @param sirina širina stupca
   * @return graditelj
   */
  public TablicniIspisGraditelj dodajStupac(String naziv, int sirina) {
    stupci.add(new StupacTablice(naziv, sirina, Poravnanje.lijevo));
    return this;
  }

  /**
   * Poravnaj zadnje dodani stupac u lijevo.
   *
   * @return graditelj
   */
  public TablicniIspisGraditelj poravnajLijevo() {
    return setPoravnanje(Poravnanje.lijevo);
  }

  /**
   * Poravnaj zadnje dodani stupac u desno.
   *
   * @return graditelj
   */
  public TablicniIspisGraditelj poravnajDesno() {
    return setPoravnanje(Poravnanje.desno);
  }

  /**
   * Poravnaj zadnje dodani stupac.
   *
   * @param poravnanje poravnanje
   * @return graditelj
   */
  public TablicniIspisGraditelj setPoravnanje(Poravnanje poravnanje) {
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
  public TablicniIspisGraditelj prikazujStupac(boolean prikazi) {
    if (stupci.isEmpty()) {
      throw new IllegalStateException("Nema stupaca!");
    }
    stupci.getLast().setPrikaziStupac(prikazi);
    return this;
  }

  public TablicniIspisGraditelj koristiPrelamanjeTeksta(boolean prelamanjeTeksta) {
    this.prelamanjeTeksta = prelamanjeTeksta;
    return this;
  }

  public TablicniIspisGraditelj postaviIspisDodatnihCrta(boolean ispisDodatnihCrta) {
    this.ispisDodatnihCrta = ispisDodatnihCrta;
    return this;
  }

  /**
   * Napravi tablični ispis
   *
   * @return tablični ispis
   */
  public ITablicniIspis napravi() {
    ITablicniIspis tablicniIspis = new TablicniIspis(stupci);

    if (prelamanjeTeksta) {
      tablicniIspis = new TablicniIspisPrelamanje(tablicniIspis);
    }
    if (ispisDodatnihCrta) {
      tablicniIspis = new TablicniIspisDodatneCrte(tablicniIspis);
    }
    
    return tablicniIspis;
  }

}
