package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

public class Konfiguracija {

  private static Konfiguracija konfiguracija;

  public static Konfiguracija dajKonfiguraciju() {
    if (konfiguracija == null) {
      konfiguracija = new Konfiguracija();
    }
    return konfiguracija;
  }

  private String putanjaAranzmani;
  private String putanjaRezervacije;

  private Konfiguracija() {}

  public String putanjaAranzmani() {return putanjaAranzmani;}

  public String putanjaRezervacije() {return putanjaRezervacije;}

  public void setPutanjaAranzmani(String putanjaAranzmani) {this.putanjaAranzmani = putanjaAranzmani;}
  
  public void setPutanjaRezervacije(String putanjaRezervacije) {this.putanjaRezervacije = putanjaRezervacije;}

}
