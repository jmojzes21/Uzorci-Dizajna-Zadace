package uzdiz_test;

import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class RezervacijeVDRTest {

  // region Početak

  private static final TuristickaAgencija agencija = new TuristickaAgencija();

  @BeforeAll
  public static void prijeSvih() throws Exception {
    agencija.pokreni(List.of("--ta", "aranzmani.csv", "--rta", "rezervacije.csv", "--vdr"));
  }

  @AfterAll
  public static void nakonSvih() throws Exception {
    agencija.zaustavi();
  }

  @BeforeEach
  public void prijeSvakoga() {
    agencija.izvrsiKomandu("BP A");
    agencija.izvrsiKomandu("UP A aranzmani.csv");
    agencija.izvrsiKomandu("UP R rezervacije.csv");
    agencija.readLines();
  }

  @AfterEach
  public void nakonSvakoga() {}

  // endregion

  // region Zaprimi rezervaciju

  // endregion

  // region Otkaži rezervaciju

  // endregion

}
