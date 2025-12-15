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

  private boolean koristiPrelamanjeTeksta;
  private boolean ispisDodatnihCrta;

  private PostavkeSustava() {
    nacinSortiranja = NacinSortiranja.uzlazno;
    koristiPrelamanjeTeksta = false;
    ispisDodatnihCrta = false;
  }

  public boolean sortirajUzlazno() {
    return nacinSortiranja == NacinSortiranja.uzlazno;
  }

  public NacinSortiranja nacinSortiranja() {return nacinSortiranja;}

  public boolean koristiPrelamanjeTeksta() {return koristiPrelamanjeTeksta;}

  public boolean ispisDodatnihCrta() {return ispisDodatnihCrta;}

  public void postaviNacinSortiranja(NacinSortiranja nacinSortiranja) {this.nacinSortiranja = nacinSortiranja;}

  public void postaviPrelamanjeTeksta(boolean prelamanjeTeksta) {
    koristiPrelamanjeTeksta = prelamanjeTeksta;
  }

  public void postaviIspisDodatnihCrta(boolean ispisDodatnihCrta) {
    this.ispisDodatnihCrta = ispisDodatnihCrta;
  }

}
