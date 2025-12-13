package edu.unizg.foi.uzdiz.jmojzes21;

public class PostavkeSustava {

  public enum NacinSortiranja {
    uzlazno, silazno
  }

  private static final PostavkeSustava postavkeSustava = new PostavkeSustava();

  public static PostavkeSustava dajInstancu() {
    return postavkeSustava;
  }

  private NacinSortiranja nacinSortiranja;

  private PostavkeSustava() {
    nacinSortiranja = NacinSortiranja.uzlazno;
  }

  public boolean sortirajUzlazno() {
    return nacinSortiranja == NacinSortiranja.uzlazno;
  }
  
  public NacinSortiranja nacinSortiranja() {return nacinSortiranja;}

  public void postaviNacinSortiranja(NacinSortiranja nacinSortiranja) {this.nacinSortiranja = nacinSortiranja;}

}
