package uzdiz_test;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TuristickaAgencijaTest {

  // region Početak

  private static TuristickaAgencija agencija = new TuristickaAgencija();

  @BeforeAll
  public static void prijeSvih() throws Exception {
    agencija.pokreni();
  }

  @AfterAll
  public static void nakonSvih() throws Exception {
    agencija.zaustavi();
  }

  @BeforeEach
  public void prijeSvakoga() {
    reset();
  }

  @AfterEach
  public void nakonSvakoga() {}

  // endregion

  // region Zaprimi rezervaciju

  @Test
  public void aktivneRezervacije() {

    agencija.izvrsiKomandu("DRTA Zoran Zoran 1 2.6.2025 10:42");

    List<String> redci = agencija.dajRezervacije("1");
    postojiRedak(redci, "Bruno", Rezervacija.aktivna);
    postojiRedak(redci, "Maja", Rezervacija.aktivna);
    postojiRedak(redci, "Zoran", Rezervacija.aktivna);

    provjeriStanjeAranzmana("1", Aranzman.aktivan);

    agencija.izvrsiKomandu("DRTA Matej Matej 1 2.6.2025 11:00");

    redci = agencija.dajRezervacije("1");
    postojiRedak(redci, "Matej", Rezervacija.aktivna);

  }

  @Test
  public void preklapanje1() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 4 2.6.2025 8:00");

    List<String> redci = agencija.dajRezervacije("4");
    postojiRedak(redci, "Bruno", Rezervacija.odgodjena);
    postojiRedak(redci, Rezervacija.primljena);

    agencija.izvrsiKomandu("DRTA Nikola Nikola 4 2.6.2025 8:00");
    agencija.izvrsiKomandu("DRTA Matej Matej 4 2.6.2025 10:00");
    agencija.izvrsiKomandu("DRTA Zoran Zoran 4 2.6.2025 8:00");

    redci = agencija.dajRezervacije("4");
    postojiRedak(redci, "Bruno", Rezervacija.odgodjena);
    postojiRedak(redci, "Matej", Rezervacija.odgodjena);
    postojiRedak(redci, Rezervacija.aktivna);

    redci = agencija.dajRezervacije("3");
    postojiRedak(redci, "Zoran", Rezervacija.odgodjena);
    postojiRedak(redci, Rezervacija.aktivna);

  }

  // endregion

  // region Otkaži rezervaciju

  // endregion

  // region Ostalo

  private void postojiRedak(List<String> redci, Object... elementi) {
    Assertions.assertTrue(redci.stream().anyMatch(e -> redakSadrziElemente(e, elementi)));
  }

  private boolean redakSadrziElemente(String redak, Object... elementi) {
    return Arrays.stream(elementi).allMatch(e -> switch (e) {
      case String tekst -> redak.contains(tekst);
      case MultiValues mv -> mv.contains(redak.toLowerCase());
      default -> true;
    });
  }

  private void provjeriStanjeAranzmana(String oznaka, Aranzman aranzman) {
    agencija.izvrsiKomandu("ITAP " + oznaka);
    List<String> redci = agencija.readLines();

    Assertions.assertTrue(redci.stream().anyMatch(e -> {
      String redak = e.trim().toLowerCase();
      if (redak.startsWith("status")) {
        return aranzman.contains(redak);
      }
      return false;
    }));

  }

  private void reset() {
    agencija.izvrsiKomandu("BP A");
    agencija.izvrsiKomandu("UP A aranzmani.csv");
    agencija.izvrsiKomandu("UP R rezervacije.csv");
    agencija.readLines();
  }

  // endregion

}
