package uzdiz_test;

import static uzdiz_test.RezervacijeTestHelper.datumVrijeme;
import static uzdiz_test.RezervacijeTestHelper.nePostojiRedak;
import static uzdiz_test.RezervacijeTestHelper.postojiRedak;
import static uzdiz_test.RezervacijeTestHelper.rezervacijeAranzmana;

import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RezervacijeNullStrategyTest {

  // region Početak

  private static final TuristickaAgencija agencija = new TuristickaAgencija();

  @BeforeAll
  public static void prijeSvih() throws Exception {
    agencija.pokreni(List.of("--ta", "aranzmani.csv", "--rta", "rezervacije.csv"));
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

  @Test
  public void preklapanje_1() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 4 " + datumVrijeme("2.6.2025 8:00"));
    // postaje aktivna

    List<String> redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    postojiRedak(redci, "Putovanje 3", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 4", Rezervacija.aktivna);

    agencija.izvrsiKomandu("DRTA Nikola Nikola 4 " + datumVrijeme("2.6.2025 8:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Matej Matej 4 " + datumVrijeme("2.6.2025 10:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Zoran Zoran 4 " + datumVrijeme("2.6.2025 11:00"));
    // postaje aktivna

    redci = rezervacijeAranzmana(agencija, "4");
    postojiRedak(redci, "Bruno", Rezervacija.aktivna);
    postojiRedak(redci, "Matej", Rezervacija.aktivna);
    postojiRedak(redci, "Zoran", Rezervacija.aktivna);
    postojiRedak(redci, "Nikola", Rezervacija.aktivna);
    nePostojiRedak(redci, Rezervacija.odgodjena);

  }

  // endregion

  // region Otkaži rezervaciju

  // endregion

}
