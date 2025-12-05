package edu.unizg.foi.uzdiz.jmojzes21;

/**
 * Sadrži konfiguraciju programa.
 */
public class Konfiguracija {

  private static Konfiguracija konfiguracija;

  /**
   * Vraća instancu konfiguracije.
   *
   * @return
   */
  public static Konfiguracija dajKonfiguraciju() {
    if (konfiguracija == null) {
      konfiguracija = new Konfiguracija();
    }
    return konfiguracija;
  }

  private String putanjaAranzmani;
  private String putanjaRezervacije;

  private Konfiguracija() {}

  /**
   * Putanja na datoteku s aranžmanima.
   *
   * @return
   */
  public String putanjaAranzmani() {return putanjaAranzmani;}

  /**
   * Putanja na datoteku s rezervacijama.
   *
   * @return
   */
  public String putanjaRezervacije() {return putanjaRezervacije;}

  public void setPutanjaAranzmani(
      String putanjaAranzmani) {this.putanjaAranzmani = putanjaAranzmani;}

  public void setPutanjaRezervacije(
      String putanjaRezervacije) {this.putanjaRezervacije = putanjaRezervacije;}

}
