package uzdiz_test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

public class RezervacijeTestHelper {

  public static List<String> rezervacijeAranzmana(TuristickaAgencija agencija, String oznaka) {
    return agencija.izvrsiKomandu("IRTA " + oznaka + " PAČODO");
  }

  public static int dajRedak(List<String> redci, Object... elementi) {
    var redak = redci.stream().filter(e -> redakSadrziElemente(e, elementi)).findFirst().orElse(null);
    if (redak == null) {return -1;}
    return redci.indexOf(redak);
  }

  public static void postojiRedak(List<String> redci, Object... elementi) {
    assertTrue(redci.stream().anyMatch(e -> redakSadrziElemente(e, elementi)),
        () -> "Ne postoji redak: " + String.join(", ", Arrays.stream(elementi).map(e -> e.toString()).toList()));
  }

  public static void nePostojiRedak(List<String> redci, Object... elementi) {
    assertFalse(redci.stream().anyMatch(e -> redakSadrziElemente(e, elementi)),
        () -> "Redak ne smije postojati: " + String.join(", ",
            Arrays.stream(elementi).map(e -> e.toString()).toList()));
  }

  public static boolean redakSadrziElemente(String redak, Object... elementi) {
    return Arrays.stream(elementi).allMatch(e -> switch (e) {
      case String tekst -> redak.contains(tekst);
      case MultiValues mv -> mv.contains(redak.toLowerCase());
      default -> true;
    });
  }

  public static void provjeriStanjeAranzmana(TuristickaAgencija agencija, String oznaka, Aranzman aranzman) {
    List<String> redci = agencija.izvrsiKomandu("ITAP " + oznaka);

    assertTrue(redci.stream().anyMatch(e -> {
      String redak = e.trim().toLowerCase();
      if (redak.startsWith("status")) {
        return aranzman.contains(redak);
      }
      return false;
    }), () -> "Turistički aranžman treba biti u stanju " + aranzman.toString());

  }

  public static String datum(String datum) {
    return datum;
  }

  public static String datumVrijeme(String vrijeme) {
    return vrijeme;
  }
 
}
