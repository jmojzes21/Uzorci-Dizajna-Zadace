package uzdiz_test;

import java.util.List;

public class Rezervacija extends MultiValues {

  public static Rezervacija primljena = new Rezervacija(List.of("primljena"));
  public static Rezervacija aktivna = new Rezervacija(List.of("aktivna"));
  public static Rezervacija naCekanju = new Rezervacija(List.of("na čekanju"));
  public static Rezervacija odgodjena = new Rezervacija(List.of("odgođena"));
  public static Rezervacija otkazana = new Rezervacija(List.of("otkazana"));

  public Rezervacija(List<String> nazivi) {
    super(nazivi);
  }

}
