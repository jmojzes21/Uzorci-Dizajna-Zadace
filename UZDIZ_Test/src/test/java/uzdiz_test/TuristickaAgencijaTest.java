package uzdiz_test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TuristickaAgencijaTest {

  // region Početak

  private static final TuristickaAgencija agencija = new TuristickaAgencija();

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

  // region Komande

  @Test
  public void komandaITAK() {

    List<String> redci = agencija.izvrsiKomandu("ITAK");

    postojiRedak(redci, "Putovanje 1", Aranzman.uPripremi);
    postojiRedak(redci, "Putovanje 2", Aranzman.uPripremi);
    postojiRedak(redci, "Putovanje 3", Aranzman.aktivan);
    postojiRedak(redci, "Putovanje 4", Aranzman.uPripremi);

    postojiRedak(redci, "Putovanje 10", Aranzman.popunjen);
    postojiRedak(redci, "Putovanje 15", Aranzman.aktivan);
    postojiRedak(redci, "Putovanje 16", Aranzman.aktivan);
    postojiRedak(redci, "Putovanje 17", Aranzman.aktivan);
    postojiRedak(redci, "Putovanje 18", Aranzman.aktivan);

    redci = agencija.izvrsiKomandu(String.format("ITAK %s %s", datum("1.7.2025"), datum("30.7.2025")));

    nePostojiRedak(redci, "Putovanje 1");
    nePostojiRedak(redci, "Putovanje 2");

    postojiRedak(redci, "Putovanje 3");
    postojiRedak(redci, "Putovanje 4");

    nePostojiRedak(redci, "Putovanje 10");
    nePostojiRedak(redci, "Putovanje 15");
    nePostojiRedak(redci, "Putovanje 16");
    nePostojiRedak(redci, "Putovanje 17");
    nePostojiRedak(redci, "Putovanje 18");

  }

  @Test
  public void komandaITAP() {

    List<String> redci = agencija.izvrsiKomandu("ITAP 3");
    postojiRedak(redci, "Putovanje 3");
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
    postojiRedak(redci, "Putovanje 1", Rezervacija.primljena);
    postojiRedak(redci, "Putovanje 3", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 10", Rezervacija.aktivna);
    postojiRedak(redci, "Putovanje 15", Rezervacija.aktivna);

  }

  @Test
  public void komandaOTA() {

    agencija.izvrsiKomandu("OTA 1");

    provjeriStanjeAranzmana("1", Aranzman.otkazan);

    List<String> redci = rezervacijeAranzmana("1");
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
    assertTrue(dajRedak(redci, "Putovanje 1 ") < dajRedak(redci, datum("Putovanje 2 ")));

    agencija.izvrsiKomandu("IP N"); // kronološki

    redci = agencija.izvrsiKomandu("ITAK");
    assertTrue(dajRedak(redci, datum("10.06.2025")) < dajRedak(redci, datum("14.07.2025")));

    redci = agencija.izvrsiKomandu("IRTA 1 PA");
    assertTrue(dajRedak(redci, "Bruno") < dajRedak(redci, datum("Maja")));

    redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    assertTrue(dajRedak(redci, datum("1.06.2025")) < dajRedak(redci, datum("1.08.2025")));

    redci = agencija.izvrsiKomandu("ITAS");
    assertTrue(dajRedak(redci, "Putovanje 1 ") < dajRedak(redci, datum("Putovanje 2 ")));

    agencija.izvrsiKomandu("IP S"); // obrnuto kronološki

    redci = agencija.izvrsiKomandu("ITAK");
    assertTrue(dajRedak(redci, datum("10.06.2025")) > dajRedak(redci, datum("14.07.2025")));

    redci = agencija.izvrsiKomandu("IRTA 1 PA");
    assertTrue(dajRedak(redci, "Bruno") > dajRedak(redci, datum("Maja")));

    redci = agencija.izvrsiKomandu("IRO Bruno Bruno");
    assertTrue(dajRedak(redci, datum("1.06.2025")) > dajRedak(redci, datum("1.08.2025")));

    redci = agencija.izvrsiKomandu("ITAS");
    assertTrue(dajRedak(redci, "Putovanje 1 ") > dajRedak(redci, datum("Putovanje 2 ")));

    agencija.izvrsiKomandu("IP N");

  }

  @Test
  public void komandaITAS() {

    List<String> redci = agencija.izvrsiKomandu("ITAS");
    postojiRedak(redci, "8", "8.000,00");

  }

  // endregion

  // region Zaprimi rezervaciju


  @Test
  public void rezervacije_postaju_aktivne() {

    List<String> redci = rezervacijeAranzmana("1");
    postojiRedak(redci, Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);

    agencija.izvrsiKomandu("DRTA Zoran Zoran 1 " + datumVrijeme("2.6.2025 10:42"));

    redci = rezervacijeAranzmana("1");
    postojiRedak(redci, "Bruno", Rezervacija.aktivna);
    postojiRedak(redci, "Maja", Rezervacija.aktivna);
    postojiRedak(redci, "Zoran", Rezervacija.aktivna);
    nePostojiRedak(redci, Rezervacija.primljena);
    provjeriStanjeAranzmana("1", Aranzman.aktivan);

    agencija.izvrsiKomandu("DRTA Matej Matej 1 " + datumVrijeme("2.6.2025 11:00"));

    redci = rezervacijeAranzmana("1");
    postojiRedak(redci, "Matej", Rezervacija.aktivna);

  }

  @Test
  public void primljeni_duplikat() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 1 " + datumVrijeme("1.6.2025 10:00"));
    // postaje odgođena

    List<String> redci = rezervacijeAranzmana("1");
    postojiRedak(redci, "Bruno", "8:10", Rezervacija.primljena);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.odgodjena);
    nePostojiRedak(redci, Rezervacija.aktivna);

    provjeriStanjeAranzmana("1", Aranzman.uPripremi);

  }

  @Test
  public void rezervacije_ide_na_cekanje() {

    agencija.izvrsiKomandu("DRTA Darko Darko 10 " + datumVrijeme("2.8.2025 8:00"));

    List<String> redci = rezervacijeAranzmana("10");
    postojiRedak(redci, "Darko", Rezervacija.naCekanju);
    nePostojiRedak(redci, Rezervacija.primljena);

    provjeriStanjeAranzmana("10", Aranzman.popunjen);

  }

  @Test
  public void preklapanje_1() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 4 " + datumVrijeme("2.6.2025 8:00"));
    // postaje odgođena

    List<String> redci = rezervacijeAranzmana("4");
    postojiRedak(redci, "Bruno", Rezervacija.odgodjena);
    postojiRedak(redci, Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);

    agencija.izvrsiKomandu("DRTA Nikola Nikola 4 " + datumVrijeme("2.6.2025 8:00"));
    // rezervacije postaju aktivne

    agencija.izvrsiKomandu("DRTA Matej Matej 4 " + datumVrijeme("2.6.2025 10:00"));
    // postaje odgođena

    agencija.izvrsiKomandu("DRTA Zoran Zoran 4 " + datumVrijeme("2.6.2025 11:00"));
    // postaje odgođena

    redci = rezervacijeAranzmana("4");
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

    List<String> redci = rezervacijeAranzmana("1");
    nePostojiRedak(redci, "Zoran", Rezervacija.primljena);
    nePostojiRedak(redci, "Zoran", Rezervacija.aktivna);

  }

  // endregion

  // region Otkaži rezervaciju

  @Test
  public void otkazi_primljenu() {

    agencija.izvrsiKomandu("ORTA Bruno Bruno 1");

    List<String> redci = rezervacijeAranzmana("1");
    postojiRedak(redci, "Bruno", Rezervacija.otkazana);
    postojiRedak(redci, Rezervacija.primljena);

  }

  @Test
  public void otkazi_primljenu_postoji_odgodjena() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 1 " + datumVrijeme("1.6.2025. 10:00"));
    // postaje odgođena

    agencija.izvrsiKomandu("ORTA Bruno Bruno 1");

    List<String> redci = rezervacijeAranzmana("1");
    postojiRedak(redci, "Bruno", "8:10", Rezervacija.otkazana);
    postojiRedak(redci, "Bruno", "10:00", Rezervacija.primljena);

  }

  @Test
  public void otkazi_aktivnu() {

    agencija.izvrsiKomandu("ORTA Bruno Bruno 3");

    List<String> redci = rezervacijeAranzmana("3");
    postojiRedak(redci, "Bruno", Rezervacija.otkazana);
    postojiRedak(redci, "Maja", Rezervacija.aktivna);

  }

  @Test
  public void otkazi_aktivnu_aranzman_postaje_u_pripremi() {

    agencija.izvrsiKomandu("DRTA Zoran Zoran 1 " + datumVrijeme("3.6.2025 10:00"));
    provjeriStanjeAranzmana("1", Aranzman.aktivan);

    agencija.izvrsiKomandu("ORTA Zoran Zoran 1");

    List<String> redci = rezervacijeAranzmana("1");
    postojiRedak(redci, "Zoran", Rezervacija.otkazana);
    postojiRedak(redci, "Maja", Rezervacija.primljena);
    nePostojiRedak(redci, Rezervacija.aktivna);

    provjeriStanjeAranzmana("1", Aranzman.uPripremi);
  }

  @Test
  public void otkazi_aktivnu_postoji_na_cekanju() {

    agencija.izvrsiKomandu("DRTA Darko Darko 10 " + datumVrijeme("2.8.2025 8:00"));
    // na cekanju

    List<String> redci = rezervacijeAranzmana("10");
    postojiRedak(redci, "Darko", Rezervacija.naCekanju);

    agencija.izvrsiKomandu("ORTA Bruno Bruno 10");

    redci = rezervacijeAranzmana("10");
    postojiRedak(redci, "Bruno", Rezervacija.otkazana);
    postojiRedak(redci, "Darko", Rezervacija.aktivna);

  }

  @Test
  public void otkazi_aktivnu_odgodjena_postaje_aktivna() {

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
  public void otkazi_aktivnu_2_odgodjene_postaju_aktivne() {

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

    redci = rezervacijeAranzmana("16");
    postojiRedak(redci, "Matej", Rezervacija.naCekanju);

    agencija.izvrsiKomandu("ORTA Bruno Bruno 16");

    redci = rezervacijeAranzmana("16");
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

    List<String> redci = rezervacijeAranzmana("10");
    postojiRedak(redci, "Nikola", Rezervacija.otkazana);

  }

  @Test
  public void otkazi_odgodjenu() {

    agencija.izvrsiKomandu("DRTA Bruno Bruno 4 " + datumVrijeme("2.6.2025 8:00"));
    // postaje odgođena

    agencija.izvrsiKomandu("ORTA Bruno Bruno 4");

    List<String> redci = rezervacijeAranzmana("4");
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

    List<String> redci = rezervacijeAranzmana("16");
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

  // region Ostalo

  private List<String> rezervacijeAranzmana(String oznaka) {
    return agencija.izvrsiKomandu("IRTA " + oznaka + " PAČODO");
  }

  private int dajRedak(List<String> redci, Object... elementi) {
    var redak = redci.stream().filter(e -> redakSadrziElemente(e, elementi)).findFirst().orElse(null);
    if (redak == null) {return -1;}
    return redci.indexOf(redak);
  }

  private void postojiRedak(List<String> redci, Object... elementi) {
    assertTrue(redci.stream().anyMatch(e -> redakSadrziElemente(e, elementi)));
  }

  private void nePostojiRedak(List<String> redci, Object... elementi) {
    assertFalse(redci.stream().anyMatch(e -> redakSadrziElemente(e, elementi)));
  }

  private boolean redakSadrziElemente(String redak, Object... elementi) {
    return Arrays.stream(elementi).allMatch(e -> switch (e) {
      case String tekst -> redak.contains(tekst);
      case MultiValues mv -> mv.contains(redak.toLowerCase());
      default -> true;
    });
  }

  private void provjeriStanjeAranzmana(String oznaka, Aranzman aranzman) {
    List<String> redci = agencija.izvrsiKomandu("ITAP " + oznaka);

    assertTrue(redci.stream().anyMatch(e -> {
      String redak = e.trim().toLowerCase();
      if (redak.startsWith("status")) {
        return aranzman.contains(redak);
      }
      return false;
    }));

  }

  private String datum(String datum) {
    return datum;
  }

  private String datumVrijeme(String vrijeme) {
    return vrijeme;
  }

  private void reset() {
    agencija.izvrsiKomandu("BP A");
    agencija.izvrsiKomandu("UP A aranzmani.csv");
    agencija.izvrsiKomandu("UP R rezervacije.csv");
    agencija.readLines();
  }

  // endregion

}
