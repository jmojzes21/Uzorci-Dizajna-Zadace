package uzdiz_test;

import static uzdiz_test.RezervacijeTestHelper.datumVrijeme;
import static uzdiz_test.RezervacijeTestHelper.nePostojiRedak;
import static uzdiz_test.RezervacijeTestHelper.postojiRedak;
import static uzdiz_test.RezervacijeTestHelper.provjeriStanjeAranzmana;
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
  public void primljeni_duplikat_aranzman_postaje_aktivan() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 1 " + datumVrijeme("1.6.2025 10:00"));
    // postaje aktivna

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Bruno", "8:10", Rezervacija.aktivna);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.aktivna);

    provjeriStanjeAranzmana(agencija, "1", Aranzman.aktivan);

  }

  @Test
  public void primljeni_duplikat_aranzman_aktivan() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 3 " + datumVrijeme("1.6.2025 10:00"));
    // postaje aktivna

    List<String> redci = rezervacijeAranzmana(agencija, "3");
    postojiRedak(redci, "Bruno", "8:10", Rezervacija.aktivna);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.aktivna);

  }


  @Test
  public void max_putnika_8() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 1 " + datumVrijeme("1.6.2025 9:00"));
    agencija.izvrsiKomandu("DRTA Bruno Bruno 1 " + datumVrijeme("1.6.2025 10:00"));

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Bruno", "8:10", Rezervacija.aktivna);
    postojiRedak(redci, "Bruno", "9:00", Rezervacija.aktivna);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.aktivna);
    nePostojiRedak(redci, "Bruno", Rezervacija.odgodjena);

  }

  @Test
  public void max_putnika_30() {

    for (int i = 0; i < 8; i++) {
      agencija.izvrsiKomandu("DRTA Bruno Bruno 8 " + datumVrijeme("1.7.2025 8:" + (10 + i)));
    }

    List<String> redci = rezervacijeAranzmana(agencija, "8");

    for (int i = 0; i < 8; i++) {
      postojiRedak(redci, "Bruno", "8:" + (10 + i), Rezervacija.aktivna);
    }

    nePostojiRedak(redci, "Bruno", Rezervacija.odgodjena);
 
  }

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
