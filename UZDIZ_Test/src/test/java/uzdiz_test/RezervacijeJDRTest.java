package uzdiz_test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uzdiz_test.RezervacijeTestHelper.TAG_MEMENTO;
import static uzdiz_test.RezervacijeTestHelper.dajRedak;
import static uzdiz_test.RezervacijeTestHelper.datum;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class RezervacijeJDRTest {

  // region Početak

  private static final TuristickaAgencija agencija = new TuristickaAgencija();

  @BeforeAll
  public static void prijeSvih() throws Exception {
    agencija.pokreni(List.of("--ta", "aranzmani.csv", "--rta", "rezervacije.csv", "--jdr"));
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

  // region Komande

  @Test
  public void komandaITAK() {

    List<String> redci = agencija.izvrsiKomandu("ITAK");

    postojiRedak(redci, "Putovanje 1 Azija", Aranzman.uPripremi);
    postojiRedak(redci, "Putovanje 2 Afrika", Aranzman.uPripremi);
    postojiRedak(redci, "Putovanje 3 Južna Amerika", Aranzman.aktivan);
    postojiRedak(redci, "Putovanje 4 Australija", Aranzman.uPripremi);

    postojiRedak(redci, "Putovanje 10", Aranzman.popunjen);
    postojiRedak(redci, "Putovanje 15", Aranzman.aktivan);
    postojiRedak(redci, "Putovanje 16", Aranzman.aktivan);
    postojiRedak(redci, "Putovanje 17", Aranzman.aktivan);
    postojiRedak(redci, "Putovanje 18", Aranzman.aktivan);

    redci = agencija.izvrsiKomandu(String.format("ITAK %s %s", datum("1.7.2025"), datum("30.7.2025")));

    nePostojiRedak(redci, "Putovanje 1 Azija");
    nePostojiRedak(redci, "Putovanje 2 Afrika");

    postojiRedak(redci, "Putovanje 3 Južna Amerik");
    postojiRedak(redci, "Putovanje 4 Australija");

    nePostojiRedak(redci, "Putovanje 10");
    nePostojiRedak(redci, "Putovanje 15");
    nePostojiRedak(redci, "Putovanje 16");
    nePostojiRedak(redci, "Putovanje 17");
    nePostojiRedak(redci, "Putovanje 18");

  }

  @Test
  public void komandaITAP() {

    List<String> redci = agencija.izvrsiKomandu("ITAP 3");
    postojiRedak(redci, "Putovanje 3 Južna Amerika");
    postojiRedak(redci, "Putovanje u Južnu Ameriku");
    postojiRedak(redci, Aranzman.aktivan);

  }

  @Test
  public void komandaIRTA() {

    agencija.izvrsiKomandu("DRTA Ante Ante 15 " + datumVrijeme("1.9.2025 8:00"));
    agencija.izvrsiKomandu("DRTA Bruno Bruno 16 " + datumVrijeme("1.9.2025 9:00")); // aktivna
    agencija.izvrsiKomandu("DRTA Ante Ante 16 " + datumVrijeme("1.9.2025 9:10")); // ogođena
    agencija.izvrsiKomandu("DRTA Marko Marko 16 " + datumVrijeme("1.9.2025 9:20"));
    agencija.izvrsiKomandu("ORTA Marko Marko 16"); // otkazana
    agencija.izvrsiKomandu("DRTA Željko Željko 16 " + datumVrijeme("1.9.2025 9:30")); // aktivna
    agencija.izvrsiKomandu("DRTA Nikola Nikola 16 " + datumVrijeme("1.9.2025 9:40")); // na čekanju

    List<String> redci = agencija.izvrsiKomandu("IRTA 1 PA");
    postojiRedak(redci, "Bruno", Rezervacija.primljena);

    redci = agencija.izvrsiKomandu("IRTA 16 PA");
    postojiRedak(redci, "Bruno", Rezervacija.aktivna);
    postojiRedak(redci, "Željko", Rezervacija.aktivna);
    nePostojiRedak(redci, Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.odgodjena);
    nePostojiRedak(redci, Rezervacija.naCekanju);
    nePostojiRedak(redci, Rezervacija.otkazana);

    redci = agencija.izvrsiKomandu("IRTA 16 Č");
    postojiRedak(redci, "Nikola", Rezervacija.naCekanju);
    nePostojiRedak(redci, Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);
    nePostojiRedak(redci, Rezervacija.odgodjena);
    nePostojiRedak(redci, Rezervacija.otkazana);

    redci = agencija.izvrsiKomandu("IRTA 16 O");
    postojiRedak(redci, "Marko", Rezervacija.otkazana);
    nePostojiRedak(redci, Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);
    nePostojiRedak(redci, Rezervacija.odgodjena);
    nePostojiRedak(redci, Rezervacija.naCekanju);

    redci = agencija.izvrsiKomandu("IRTA 16 OD");
    postojiRedak(redci, "Ante", Rezervacija.odgodjena);
    nePostojiRedak(redci, Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);
    nePostojiRedak(redci, Rezervacija.naCekanju);
    nePostojiRedak(redci, Rezervacija.otkazana);

    redci = agencija.izvrsiKomandu("IRTA 16 ODO");
    postojiRedak(redci, "Ante", Rezervacija.odgodjena);
    postojiRedak(redci, "Marko", Rezervacija.otkazana);
    nePostojiRedak(redci, Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);
    nePostojiRedak(redci, Rezervacija.naCekanju);

  }

  @Test
  public void komandaIRO() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 15 " + datumVrijeme("1.9.2025 8:00"));

    List<String> redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    postojiRedak(redci, "Putovanje 1 Azija", Rezervacija.primljena);
    postojiRedak(redci, "Putovanje 3 Južna Amerika", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 10", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 15", Rezervacija.aktivna);

  }

  @Test
  public void komandaOTA() {

    agencija.izvrsiKomandu("OTA 1");

    provjeriStanjeAranzmana(agencija, "1", Aranzman.otkazan);

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Bruno", Rezervacija.otkazana);
    nePostojiRedak(redci, Rezervacija.primljena);

  }

  @Test
  public void komandaIP() {

    List<String> redci = agencija.izvrsiKomandu("ITAK");
    assertTrue(dajRedak(redci, datum("10.06.2025")) < dajRedak(redci, datum("14.07.2025")));

    redci = agencija.izvrsiKomandu("IRTA 1 PA");
    assertTrue(dajRedak(redci, "Bruno") < dajRedak(redci, datum("Maja")));

    redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    assertTrue(dajRedak(redci, datum("1.06.2025")) < dajRedak(redci, datum("1.08.2025")));

    redci = agencija.izvrsiKomandu("ITAS");
    assertTrue(dajRedak(redci, "Putovanje 1 Azija") < dajRedak(redci, datum("Putovanje 2 Afrika")));

    agencija.izvrsiKomandu("IP N"); // kronološki

    redci = agencija.izvrsiKomandu("ITAK");
    assertTrue(dajRedak(redci, datum("10.06.2025")) < dajRedak(redci, datum("14.07.2025")));

    redci = agencija.izvrsiKomandu("IRTA 1 PA");
    assertTrue(dajRedak(redci, "Bruno") < dajRedak(redci, datum("Maja")));

    redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    assertTrue(dajRedak(redci, datum("1.06.2025")) < dajRedak(redci, datum("1.08.2025")));

    redci = agencija.izvrsiKomandu("ITAS");
    assertTrue(dajRedak(redci, "Putovanje 1 Azija") < dajRedak(redci, datum("Putovanje 2 Afrika")));

    agencija.izvrsiKomandu("IP S"); // obrnuto kronološki

    redci = agencija.izvrsiKomandu("ITAK");
    assertTrue(dajRedak(redci, datum("10.06.2025")) > dajRedak(redci, datum("14.07.2025")));

    redci = agencija.izvrsiKomandu("IRTA 1 PA");
    assertTrue(dajRedak(redci, "Bruno") > dajRedak(redci, datum("Maja")));

    redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    assertTrue(dajRedak(redci, datum("1.06.2025")) > dajRedak(redci, datum("1.08.2025")));

    redci = agencija.izvrsiKomandu("ITAS");
    assertTrue(dajRedak(redci, "Putovanje 1 Azija") > dajRedak(redci, datum("Putovanje 2 Afrika")));

    agencija.izvrsiKomandu("IP N");

  }

  @Test
  public void komandaITAS() {

    List<String> redci = agencija.izvrsiKomandu("ITAS");
    postojiRedak(redci, "Putovanje 3 Južna Amerika", "6.000,00");
    postojiRedak(redci, "Putovanje 10", "8.000,00");

  }

  @Test
  public void komandaPPTAR() {

    List<String> redci = agencija.izvrsiKomandu("PPTAR A Azija");
    postojiRedak(redci, "Putovanje 1 Azija");
    nePostojiRedak(redci, "Putovanje 2 Afrika");

    redci = agencija.izvrsiKomandu("PPTAR A planinarenje");
    postojiRedak(redci, "Putovanje 1 Azija");
    nePostojiRedak(redci, "Putovanje 2 Afrika");

    agencija.izvrsiKomandu("DRTA Bruno Brunić 2 " + datumVrijeme("2.6.2025. 10:00"));

    redci = agencija.izvrsiKomandu("PPTAR R Bruno");
    postojiRedak(redci, "Putovanje 1 Azija");
    postojiRedak(redci, "Putovanje 2 Afrika");
    postojiRedak(redci, "Putovanje 3 Južna Amerika");
    nePostojiRedak(redci, "Putovanje 4 Australija");

    redci = agencija.izvrsiKomandu("PPTAR R Brunić");
    postojiRedak(redci, "Putovanje 2 Afrika");
    nePostojiRedak(redci, "Putovanje 1 Azija");
    nePostojiRedak(redci, "Putovanje 3 Južna Amerika");

  }

  @Test
  public void komandaPTAR() {

    agencija.izvrsiKomandu("PTAR Bruno Bruno 3");
    agencija.izvrsiKomandu("PTAR Maja Maja 4");
    agencija.izvrsiKomandu("PTAR Bruno Bruno 4");

    List<String> redci = agencija.izvrsiKomandu("DRTA Bruno Bruno 4 " + datumVrijeme("2.6.2025 8:00"));
    // postaje odgođena

    postojiRedak(redci, "Bruno", "4", Rezervacija.odgodjena);

    redci = agencija.izvrsiKomandu("DRTA Nikola Nikola 4 " + datumVrijeme("2.6.2025 8:00"));
    // postaje aktivna

    postojiRedak(redci, "Bruno", "Nikola", "4", Rezervacija.aktivna);
    postojiRedak(redci, "Bruno", "4", Aranzman.aktivan);

    redci = agencija.izvrsiKomandu("DRTA Zoran Zoran 4 " + datumVrijeme("2.6.2025 11:00"));
    // postaje odgođena

    postojiRedak(redci, "Bruno", "Zoran", "4", Rezervacija.odgodjena);

    redci = agencija.izvrsiKomandu("ORTA Bruno Bruno 3");

    postojiRedak(redci, "Bruno", "3", Rezervacija.otkazana);
    postojiRedak(redci, "Bruno", "4", Rezervacija.aktivna);
    postojiRedak(redci, "Maja", "Bruno", "4", Rezervacija.aktivna);

  }

  @Test
  public void komandaUPTAR() {

    agencija.izvrsiKomandu("PTAR Maja Maja 15");

    List<String> redci = agencija.izvrsiKomandu("DRTA Matej Matej 15 " + datumVrijeme("1.9.2025 8:00"));
    // postaje aktivna

    postojiRedak(redci, "Maja", "Matej", "15", Rezervacija.aktivna);

    agencija.izvrsiKomandu("UPTAR Maja Maja 15");

    redci = agencija.izvrsiKomandu("DRTA Zoran Zoran 15 " + datumVrijeme("1.9.2025 8:00"));
    // postaje aktivna

    nePostojiRedak(redci, "Maja", "Zoran", "15", Rezervacija.aktivna);

    agencija.izvrsiKomandu("PTAR Tanja Tanja 15");
    agencija.izvrsiKomandu("UPTAR 15");

    redci = agencija.izvrsiKomandu("DRTA Nikola Nikola 15 " + datumVrijeme("1.9.2025 8:00"));
    // postaje aktivna

    nePostojiRedak(redci, "Tanja", "Nikola", "15", Rezervacija.aktivna);

  }

  // endregion

  // region Zaprimi rezervaciju


  @Test
  public void rezervacije_postaju_aktivne() {

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);

    agencija.izvrsiKomandu("DRTA Zoran Zoran 1 " + datumVrijeme("2.6.2025 10:42"));

    redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Bruno", Rezervacija.aktivna);
    postojiRedak(redci, "Maja", Rezervacija.aktivna);
    postojiRedak(redci, "Zoran", Rezervacija.aktivna);
    nePostojiRedak(redci, Rezervacija.primljena);
    provjeriStanjeAranzmana(agencija, "1", Aranzman.aktivan);

    agencija.izvrsiKomandu("DRTA Matej Matej 1 " + datumVrijeme("2.6.2025 11:00"));

    redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Matej", Rezervacija.aktivna);

  }

  @Test
  public void primljeni_duplikat_aranzman_u_pripremi() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 5 " + datumVrijeme("1.7.2025 8:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Bruno Bruno 5 " + datumVrijeme("1.7.2025 10:00"));
    // postaje odgođena

    List<String> redci = rezervacijeAranzmana(agencija, "5");
    postojiRedak(redci, "Bruno", "8:00", Rezervacija.primljena);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.odgodjena);
    nePostojiRedak(redci, Rezervacija.aktivna);

  }

  @Test
  public void primljeni_duplikat_aranzman_ne_moze_postati_aktivan() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 1 " + datumVrijeme("1.6.2025 10:00"));
    // postaje odgođena

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Bruno", "8:10", Rezervacija.primljena);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.odgodjena);
    nePostojiRedak(redci, Rezervacija.aktivna);

    provjeriStanjeAranzmana(agencija, "1", Aranzman.uPripremi);

  }

  @Test
  public void primljeni_duplikat_aranzman_aktivan() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 3 " + datumVrijeme("1.6.2025 10:00"));
    // postaje odgođena

    List<String> redci = rezervacijeAranzmana(agencija, "3");
    postojiRedak(redci, "Bruno", "8:10", Rezervacija.aktivna);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.odgodjena);

  }

  @Test
  public void rezervacije_ide_na_cekanje() {

    agencija.izvrsiKomandu("DRTA Darko Darko 10 " + datumVrijeme("2.8.2025 8:00"));

    List<String> redci = rezervacijeAranzmana(agencija, "10");
    postojiRedak(redci, "Darko", Rezervacija.naCekanju);
    nePostojiRedak(redci, Rezervacija.primljena);

    provjeriStanjeAranzmana(agencija, "10", Aranzman.popunjen);

  }

  @Test
  public void preklapanje_1() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 4 " + datumVrijeme("2.6.2025 8:00"));
    // postaje odgođena

    List<String> redci = rezervacijeAranzmana(agencija, "4");
    postojiRedak(redci, "Bruno", Rezervacija.odgodjena);
    postojiRedak(redci, Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);

    agencija.izvrsiKomandu("DRTA Nikola Nikola 4 " + datumVrijeme("2.6.2025 8:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Matej Matej 4 " + datumVrijeme("2.6.2025 10:00"));
    // postaje odgođena

    agencija.izvrsiKomandu("DRTA Zoran Zoran 4 " + datumVrijeme("2.6.2025 11:00"));
    // postaje odgođena

    redci = rezervacijeAranzmana(agencija, "4");
    postojiRedak(redci, "Bruno", Rezervacija.odgodjena);
    postojiRedak(redci, "Matej", Rezervacija.odgodjena);
    postojiRedak(redci, "Zoran", Rezervacija.odgodjena);
    postojiRedak(redci, "Nikola", Rezervacija.aktivna);
    nePostojiRedak(redci, Rezervacija.primljena);

  }

  @Test
  public void zaprimi_rezervaciju_otkazani_aranzman() {

    agencija.izvrsiKomandu("OTA 1");
    agencija.izvrsiKomandu("DRTA Zoran Zoran 1 " + datumVrijeme("2.6.2025 10:00"));

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    nePostojiRedak(redci, "Zoran", Rezervacija.primljena);
    nePostojiRedak(redci, "Zoran", Rezervacija.aktivna);

  }

  // endregion

  // region Otkaži rezervaciju

  @Test
  public void otkazi_primljenu() {

    agencija.izvrsiKomandu("ORTA Bruno Bruno 1");

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Bruno", Rezervacija.otkazana);
    postojiRedak(redci, Rezervacija.primljena);

  }

  @Test
  public void otkazi_primljenu_postoji_odgodjena() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 1 " + datumVrijeme("1.6.2025. 10:00"));
    // postaje odgođena

    agencija.izvrsiKomandu("ORTA Bruno Bruno 1");

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Bruno", "8:10", Rezervacija.otkazana);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.primljena);

  }

  @Test
  public void otkazi_aktivnu() {

    agencija.izvrsiKomandu("ORTA Bruno Bruno 3");

    List<String> redci = rezervacijeAranzmana(agencija, "3");
    postojiRedak(redci, "Bruno", Rezervacija.otkazana);
    postojiRedak(redci, "Maja", Rezervacija.aktivna);

  }

  @Test
  public void otkazi_aktivnu_aranzman_postaje_u_pripremi() {

    agencija.izvrsiKomandu("DRTA Zoran Zoran 1 " + datumVrijeme("3.6.2025 10:00"));
    provjeriStanjeAranzmana(agencija, "1", Aranzman.aktivan);

    agencija.izvrsiKomandu("ORTA Zoran Zoran 1");

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Zoran", Rezervacija.otkazana);
    postojiRedak(redci, "Maja", Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);

    provjeriStanjeAranzmana(agencija, "1", Aranzman.uPripremi);
  }

  @Test
  public void otkazi_aktivnu_postoji_odgodjena() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 3 " + datumVrijeme("1.6.2025 10:00"));
    // postaje odgođena

    agencija.izvrsiKomandu("ORTA Bruno Bruno 3");

    List<String> redci = rezervacijeAranzmana(agencija, "3");
    postojiRedak(redci, "Bruno", "8:10", Rezervacija.otkazana);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.aktivna);
    nePostojiRedak(redci, "Bruno", Rezervacija.odgodjena);

  }

  @Test
  public void otkazi_aktivnu_postoji_na_cekanju() {

    agencija.izvrsiKomandu("DRTA Darko Darko 10 " + datumVrijeme("2.8.2025 8:00"));
    // na cekanju

    List<String> redci = rezervacijeAranzmana(agencija, "10");
    postojiRedak(redci, "Darko", Rezervacija.naCekanju);

    agencija.izvrsiKomandu("ORTA Bruno Bruno 10");

    redci = rezervacijeAranzmana(agencija, "10");
    postojiRedak(redci, "Bruno", Rezervacija.otkazana);
    postojiRedak(redci, "Darko", Rezervacija.aktivna);

  }

  @Test
  public void otkazi_aktivnu_na_drugom_aranzmanu_odgodjena_postaje_aktivna() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 16 " + datumVrijeme("1.9.2025 8:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Bruno Bruno 17 " + datumVrijeme("1.9.2025 8:10"));
    // postaje odgođena

    agencija.izvrsiKomandu("DRTA Bruno Bruno 18 " + datumVrijeme("1.9.2025 8:20"));
    // postaje odgođena

    agencija.izvrsiKomandu("ORTA Bruno Bruno 16");

    List<String> redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    postojiRedak(redci, "Putovanje 16", Rezervacija.otkazana);
    postojiRedak(redci, "Putovanje 17", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 18", Rezervacija.odgodjena);

  }

  @Test
  public void otkazi_aktivnu_na_drugim_aranzmanima_2_odgodjene_postaju_aktivne() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 16 " + datumVrijeme("1.9.2025 8:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Bruno Bruno 15 " + datumVrijeme("1.9.2025 8:10"));
    // postaje odgođena

    agencija.izvrsiKomandu("DRTA Bruno Bruno 17 " + datumVrijeme("1.9.2025 8:12"));
    // postaje odgođena

    agencija.izvrsiKomandu("DRTA Darko Darko 16 " + datumVrijeme("1.9.2025 8:21"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Matej Matej 16 " + datumVrijeme("1.9.2025 8:23"));
    //  na čekanju

    List<String> redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    postojiRedak(redci, "Putovanje 16", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 15", Rezervacija.odgodjena);
    postojiRedak(redci, "Putovanje 17", Rezervacija.odgodjena);

    redci = rezervacijeAranzmana(agencija, "16");
    postojiRedak(redci, "Matej", Rezervacija.naCekanju);

    agencija.izvrsiKomandu("ORTA Bruno Bruno 16");

    redci = rezervacijeAranzmana(agencija, "16");
    postojiRedak(redci, "Bruno", Rezervacija.otkazana);
    postojiRedak(redci, "Matej", Rezervacija.aktivna);

    redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    postojiRedak(redci, "Putovanje 15", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 17", Rezervacija.aktivna);

  }

  @Test
  public void otkazi_na_cekanju() {

    agencija.izvrsiKomandu("DRTA Nikola Nikola 10 " + datumVrijeme("2.8.2025 8:00"));
    agencija.izvrsiKomandu("ORTA Nikola Nikola 10");

    List<String> redci = rezervacijeAranzmana(agencija, "10");
    postojiRedak(redci, "Nikola", Rezervacija.otkazana);

  }

  @Test
  public void otkazi_odgodjenu() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 4 " + datumVrijeme("2.6.2025 8:00"));
    // postaje odgođena

    agencija.izvrsiKomandu("ORTA Bruno Bruno 4");

    List<String> redci = rezervacijeAranzmana(agencija, "4");
    postojiRedak(redci, "Bruno", Rezervacija.otkazana);

  }

  @Test
  public void otkazi_aktivnu_aranzman_popunjen() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 15 " + datumVrijeme("1.9.2025 9:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Bruno Bruno 16 " + datumVrijeme("1.9.2025 10:00"));
    // postaje odgođena

    agencija.izvrsiKomandu("DRTA Zoran Zoran 16 " + datumVrijeme("1.9.2025 10:00"));
    agencija.izvrsiKomandu("DRTA Matej Matej 16 " + datumVrijeme("1.9.2025 10:00"));

    agencija.izvrsiKomandu("ORTA Bruno Bruno 15");

    List<String> redci = rezervacijeAranzmana(agencija, "16");
    nePostojiRedak(redci, "Bruno", Rezervacija.aktivna);

  }

  @Test
  public void otkazi_aranzman() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 16 " + datumVrijeme("1.9.2025 8:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Bruno Bruno 15 " + datumVrijeme("1.9.2025 8:10"));
    // postaje odgođena

    agencija.izvrsiKomandu("DRTA Bruno Bruno 17 " + datumVrijeme("1.9.2025 8:12"));
    // postaje odgođena

    agencija.izvrsiKomandu("OTA 16");

    List<String> redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    postojiRedak(redci, "Putovanje 16", Rezervacija.otkazana);
    postojiRedak(redci, "Putovanje 15", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 17", Rezervacija.aktivna);

  }

  // endregion

  // region Memento - Spremanje i vraćanje stanja

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_dodaj_rezervaciju_ne_mijenja_stanje() {

    agencija.izvrsiKomandu("PSTAR 3");

    agencija.izvrsiKomandu("DRTA Anja Anja 3 " + datumVrijeme("1.6.2025. 10:00"));
    // postaje aktivna

    List<String> redci = rezervacijeAranzmana(agencija, "3");
    postojiRedak(redci, "Anja", Rezervacija.aktivna);

    agencija.izvrsiKomandu("VSTAR 3");

    redci = rezervacijeAranzmana(agencija, "3");
    postojiRedak(redci, "Maja", Rezervacija.aktivna);
    nePostojiRedak(redci, "Anja");

    provjeriStanjeAranzmana(agencija, "3", Aranzman.aktivan);

  }

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_dodaj_rezervaciju_mijenja_stanje() {

    agencija.izvrsiKomandu("PSTAR 1");

    agencija.izvrsiKomandu("DRTA Matej Matej 1 " + datumVrijeme("1.6.2025. 10:00"));
    // postaje aktivna

    List<String> redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Matej", Rezervacija.aktivna);

    agencija.izvrsiKomandu("VSTAR 1");

    redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Maja", Rezervacija.primljena);
    nePostojiRedak(redci, "Matej");

    provjeriStanjeAranzmana(agencija, "1", Aranzman.uPripremi);

  }

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_dodaj_rezervaciju_na_drugom_postaje_odgodjena() {

    agencija.izvrsiKomandu("PSTAR 15");

    agencija.izvrsiKomandu("DRTA Bruno Bruno 15 " + datumVrijeme("1.9.2025. 10:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Bruno Bruno 16 " + datumVrijeme("1.9.2025. 10:30"));
    // postaje odgođena

    List<String> redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    postojiRedak(redci, "Putovanje 15", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 16", Rezervacija.odgodjena);

    agencija.izvrsiKomandu("VSTAR 15");

    redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    nePostojiRedak(redci, "Putovanje 15");
    postojiRedak(redci, "Putovanje 16", Rezervacija.aktivna);

  }

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_otkazi_rezervaciju_ne_mijenja_stanje() {

    agencija.izvrsiKomandu("PSTAR 3");

    agencija.izvrsiKomandu("ORTA Zoran Zoran 3");
    // postaje otkazana

    List<String> redci = rezervacijeAranzmana(agencija, "3");
    postojiRedak(redci, "Zoran", Rezervacija.otkazana);

    agencija.izvrsiKomandu("VSTAR 3");

    redci = rezervacijeAranzmana(agencija, "3");
    postojiRedak(redci, "Zoran", Rezervacija.aktivna);
    nePostojiRedak(redci, "Zoran", Rezervacija.otkazana);

    provjeriStanjeAranzmana(agencija, "3", Aranzman.aktivan);

  }

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_otkazi_rezervaciju_mijenja_stanje() {

    agencija.izvrsiKomandu("PSTAR 3");

    agencija.izvrsiKomandu("ORTA Zoran Zoran 3");
    agencija.izvrsiKomandu("ORTA Lana Lana 3");
    agencija.izvrsiKomandu("ORTA Marko Marko 3");

    provjeriStanjeAranzmana(agencija, "3", Aranzman.uPripremi);

    agencija.izvrsiKomandu("VSTAR 3");

    List<String> redci = rezervacijeAranzmana(agencija, "3");
    nePostojiRedak(redci, Rezervacija.otkazana);

    provjeriStanjeAranzmana(agencija, "3", Aranzman.aktivan);

  }

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_otkazi_rezervaciju_na_drugom_postaje_aktivna() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 15 " + datumVrijeme("1.9.2025. 10:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Bruno Bruno 16 " + datumVrijeme("1.9.2025. 10:30"));
    // postaje odgođena

    agencija.izvrsiKomandu("PSTAR 15");

    agencija.izvrsiKomandu("ORTA Bruno Bruno 15");

    List<String> redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    postojiRedak(redci, "Putovanje 15", Rezervacija.otkazana);
    postojiRedak(redci, "Putovanje 16", Rezervacija.aktivna);

    agencija.izvrsiKomandu("VSTAR 15");

    redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    postojiRedak(redci, "Putovanje 15", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 16", Rezervacija.odgodjena);
    nePostojiRedak(redci, Rezervacija.otkazana);

  }

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_komanda_bp() {

    agencija.izvrsiKomandu("DRTA Anja Anja 1 " + datumVrijeme("1.6.2025. 10:00"));

    agencija.izvrsiKomandu("PSTAR 1");

    agencija.izvrsiKomandu("BP A");

    List<String> redci = agencija.izvrsiKomandu("ITAK");
    nePostojiRedak(redci, "Putovanje 1 Azija");

    agencija.izvrsiKomandu("VSTAR 1");

    redci = agencija.izvrsiKomandu("ITAK");
    postojiRedak(redci, "Putovanje 1 Azija");

    redci = agencija.izvrsiKomandu("ITAP 1");
    postojiRedak(redci, "Putovanje u Aziju, planinarenje na Himalaju");
    postojiRedak(redci, "helikopter");

    redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Bruno", Rezervacija.aktivna);
    postojiRedak(redci, "Anja", Rezervacija.aktivna);

  }

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_komanda_bp_up() {

    agencija.izvrsiKomandu("DRTA Anja Anja 1 " + datumVrijeme("1.6.2025. 10:00"));

    agencija.izvrsiKomandu("PSTAR 1");

    agencija.izvrsiKomandu("BP A");
    agencija.izvrsiKomandu("UP A aranzmani.csv");
    agencija.izvrsiKomandu("UP R rezervacije.csv");

    provjeriStanjeAranzmana(agencija, "1", Aranzman.uPripremi);

    agencija.izvrsiKomandu("VSTAR 1");

    List<String> redci = agencija.izvrsiKomandu("ITAK");
    postojiRedak(redci, "Putovanje 1 Azija");

    redci = rezervacijeAranzmana(agencija, "1");
    postojiRedak(redci, "Bruno", Rezervacija.aktivna);
    postojiRedak(redci, "Anja", Rezervacija.aktivna);

    provjeriStanjeAranzmana(agencija, "1", Aranzman.aktivan);

  }

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_odgodjena_moze_postati_aktivna() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 15 " + datumVrijeme("1.9.2025. 10:00"));
    // postaje aktivna

    agencija.izvrsiKomandu("DRTA Bruno Bruno 16 " + datumVrijeme("1.9.2025. 10:30"));
    // postaje odgođena

    agencija.izvrsiKomandu("PSTAR 16");

    agencija.izvrsiKomandu("ORTA Bruno Bruno 15");

    List<String> redci = rezervacijeAranzmana(agencija, "16");
    postojiRedak(redci, "Bruno", Rezervacija.aktivna);

    agencija.izvrsiKomandu("VSTAR 16");

    redci = rezervacijeAranzmana(agencija, "16");
    postojiRedak(redci, "Bruno", Rezervacija.aktivna);
 
  }

  @Test
  @Tag(TAG_MEMENTO)
  public void memento_pretplate() {

    agencija.izvrsiKomandu("PTAR Maja Majić 3");

    agencija.izvrsiKomandu("PSTAR 3");

    agencija.izvrsiKomandu("PTAR Lana Lanić 3");
    agencija.izvrsiKomandu("UPTAR Maja Majić 3");

    List<String> redci = agencija.izvrsiKomandu("ORTA Marko Marko 3");
    postojiRedak(redci, "Lanić", "Marko");
    nePostojiRedak(redci, "Majić", "Marko");

    agencija.izvrsiKomandu("VSTAR 3");

    redci = agencija.izvrsiKomandu("ORTA Marko Marko 3");
    postojiRedak(redci, "Majić", "Marko");
    nePostojiRedak(redci, "Lanić", "Marko");

  }

  // endregion

}
