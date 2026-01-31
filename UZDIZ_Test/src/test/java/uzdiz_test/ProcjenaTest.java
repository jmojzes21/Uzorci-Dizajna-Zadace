package uzdiz_test;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class ProcjenaTest {

  // region Početak

  private static final TuristickaAgencija agencija = new TuristickaAgencija();

  private static final String ARANZMANI_1 = "DZ_3_aranzmani_3.csv";
  private static final String REZERVACIJE_1 = "DZ_3_rezervacije_3.csv";

  @BeforeEach
  public void prijeSvakoga() {}

  @AfterEach
  public void nakonSvakoga() throws Exception {
    agencija.zaustavi();
    System.out.println();
  }

  // endregion

  // region Procjena

  @Test
  @Order(100)
  public void kriterij_4_opcije_pokretanja() throws Exception {

    pokreniAgenciju(List.of("--jdr"));
    agencija.provjeriStatus();
    agencija.zaustavi();
    System.out.println();

    pokreniAgenciju(List.of("--t", ARANZMANI_1, "--r", REZERVACIJE_1));
    agencija.provjeriStatus();
    agencija.zaustavi();
    System.out.println();

    pokreniAgenciju(List.of("--ta", ARANZMANI_1, "--rta", REZERVACIJE_1, "--jdr", "--vdr"));
    agencija.provjeriStatus();
    agencija.zaustavi();
    System.out.println();

    pokreniAgenciju(List.of("--ta", "nema.csv", "--rta", "nema.csv", "--jdr"));
    agencija.provjeriStatus();
    agencija.zaustavi();
    System.out.println();

  }

  @Test
  @Order(101)
  public void kriterij_4_ucitavanje_greska() throws Exception {
    pokreniAgenciju(List.of("--ta", "DZ_3_aranzmani_3_greska.csv", "--rta", "DZ_3_rezervacije_3_greska.csv", "--jdr"));
    agencija.zaustavi();
  }

  @Test
  @Order(102)
  public void ostali_kriteriji() throws Exception {

    pokreniAgencijuJdr();

    kriterij(5);
    agencija.izvrsiKomandu("ITAK");

    kriterij(6);
    agencija.izvrsiKomandu("ITAK 01.01.2026. 31.01.2026.");

    kriterij(7);
    agencija.izvrsiKomandu("ITAP");
    agencija.izvrsiKomandu("ITAP 99");
    agencija.izvrsiKomandu("ITAP 1");

    kriterij(8);
    agencija.izvrsiKomandu("IRTA 2 PA");
    agencija.izvrsiKomandu("IRTA 2 ODO");
    agencija.izvrsiKomandu("IRTA ODPAČO");
    agencija.izvrsiKomandu("IRTA 2 PAŽO");

    kriterij(9);
    agencija.izvrsiKomandu("IRO Ema Grgić");
    agencija.izvrsiKomandu("IRO Ema Maja Grgić");
    agencija.izvrsiKomandu("IRO");

    kriterij(10);
    agencija.izvrsiKomandu("ORTA Karlo Novak 1");
    agencija.izvrsiKomandu("ORTA Karlo Novak 2");
    agencija.izvrsiKomandu("ORTA Karlo Novak");
    agencija.izvrsiKomandu("ORTA");

    kriterij(11);
    agencija.izvrsiKomandu("DRTA Damir Zurub 1 22.01.2026 09:00:00");
    agencija.izvrsiKomandu("DRTA Damir Zurub 1");
    agencija.izvrsiKomandu("DRTA Damir Zurub");
    agencija.izvrsiKomandu("DRTA");

    kriterij(12);
    agencija.izvrsiKomandu("OTA 3");
    agencija.izvrsiKomandu("OTA 99");
    agencija.izvrsiKomandu("OTA");
    agencija.izvrsiKomandu("OTA 3");

    agencija.zaustavi();
    pokreniAgencijuJdr();

    kriterij(13);
    agencija.izvrsiKomandu("ITAK");
    agencija.izvrsiKomandu("IP N");
    agencija.izvrsiKomandu("ITAK");
    agencija.izvrsiKomandu("IP");

    kriterij(14);
    agencija.izvrsiKomandu("ITAK");
    agencija.izvrsiKomandu("IP S");
    agencija.izvrsiKomandu("ITAK");

    kriterij(15);
    agencija.izvrsiKomandu("ITAS");

    kriterij(16);
    agencija.izvrsiKomandu("ITAS 01.01.2026. 31.01.2026.");

    kriterij(17);
    agencija.izvrsiKomandu("PPTAR A skijanje");
    agencija.izvrsiKomandu("PPTAR A Davos");
    agencija.izvrsiKomandu("PPTAR A");

    kriterij(18);
    agencija.izvrsiKomandu("PPTAR R Kovač");
    agencija.izvrsiKomandu("PPTAR R Donald");
    agencija.izvrsiKomandu("PPTAR R");

    kriterij(19);
    agencija.izvrsiKomandu("PTAR Dalibor Andres 3");
    agencija.izvrsiKomandu("PTAR Anton Prekalj");
    agencija.izvrsiKomandu("ORTA Luka Buljan 3");
    agencija.izvrsiKomandu("DRTA Damir Zurub 3 22.01.2026 09:10:00");

    kriterij(20);
    agencija.izvrsiKomandu("IRTA 3 PAČO");
    agencija.izvrsiKomandu("PSTAR 3");
    agencija.izvrsiKomandu("OTA 3");

    kriterij(21);
    agencija.izvrsiKomandu("IRTA 3 PAČO");
    agencija.izvrsiKomandu("VSTAR 3");
    agencija.izvrsiKomandu("IRTA 3 PAČO");

    kriterij(22);
    agencija.izvrsiKomandu("UPTAR Dalibor Andres 3");
    agencija.izvrsiKomandu("UPTAR Dalibor Andres 4");
    agencija.izvrsiKomandu("UPTAR Anton Prekalj");

    kriterij(23);
    agencija.izvrsiKomandu("ITAK");
    agencija.izvrsiKomandu("UP A DZ_3_aranzmani_3.csv");
    agencija.izvrsiKomandu("ITAK");
    agencija.izvrsiKomandu("UP A DZ_3_aranzmani_3_1.csv");

    kriterij(24);
    agencija.izvrsiKomandu("IRTA 8 PAČO");
    agencija.izvrsiKomandu("UP R DZ_3_rezervacije_3.1.csv");
    agencija.izvrsiKomandu("IRTA 8 PAČO");

    kriterij(25);
    agencija.izvrsiKomandu("IRTA 8 PAČO");
    agencija.izvrsiKomandu("BP R");
    agencija.izvrsiKomandu("IRTA 8 PAČO");

    kriterij(26);
    agencija.izvrsiKomandu("ITAK");
    agencija.izvrsiKomandu("BP A");
    agencija.izvrsiKomandu("ITAK");
    agencija.izvrsiKomandu("IRTA 1 PAČO");

    kriterij("IRO jdr");
    agencija.zaustavi();
    pokreniAgenciju(List.of("--ta", ARANZMANI_1, "--rta", REZERVACIJE_1, "--jdr"));
    agencija.izvrsiKomandu("IRO Ema Grgić");

    kriterij("IRO vdr");
    agencija.zaustavi();
    pokreniAgenciju(List.of("--ta", ARANZMANI_1, "--rta", REZERVACIJE_1, "--vdr"));
    agencija.izvrsiKomandu("IRO Ema Grgić");

    kriterij("IRO null");
    agencija.zaustavi();
    pokreniAgenciju(List.of("--ta", ARANZMANI_1, "--rta", REZERVACIJE_1));
    agencija.izvrsiKomandu("IRO Ema Grgić");

  }

  // endregion

  // region Ostalo

  private void kriterij(String naziv) {
    System.out.println();
    System.out.printf("--- Kriterij %s ---\n", naziv);
    System.out.println();
  }


  private void kriterij(int n) {
    System.out.println();
    System.out.printf("--- Kriterij %d ---\n", n);
    System.out.println();
  }

  private void pokreniAgencijuJdr() {
    try {
      pokreniAgenciju(List.of("--ta", ARANZMANI_1, "--rta", REZERVACIJE_1, "--jdr"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void pokreniAgenciju(List<String> args) {
    try {
      agencija.pokreni(args);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // endregion

}
