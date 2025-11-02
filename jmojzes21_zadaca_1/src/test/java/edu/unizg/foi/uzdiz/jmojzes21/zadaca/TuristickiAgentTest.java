package edu.unizg.foi.uzdiz.jmojzes21.zadaca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.AranzmanDirektor;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.KreatorAktivneRezervacije;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.KreatorPrimljeneRezervacije;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.KreatorRezervacijeNaCekanju;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.TuristickiAgent;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.AktivnaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.OtkazanaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.PrimljenaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.RezervacijaNaCekanju;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TuristickiAgentTest {

  private TuristickiAgent agent;
  private Map<Integer, Aranzman> aranzmani;

  @BeforeEach
  public void prijeSvakoga() {
    var a1 = dajAranzman(1, 3, 5);
    var a2 = dajAranzman(2, 3, 5);

    aranzmani = Map.ofEntries(
        Map.entry(1, a1),
        Map.entry(2, a2)
    );

    agent = new TuristickiAgent(aranzmani);
  }

  @Test
  public void zaprimiRezervaciju_nemaRezervacija_rezervacijaPrimljena() throws Exception {

    var a = aranzmani.get(1);
    agent.zaprimiRezervaciju(a, dajRezervaciju("K1", a.oznaka()));
    agent.zaprimiRezervaciju(a, dajRezervaciju("K2", a.oznaka()));

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 2, List.of("K1", "K2"));

  }

  @Test
  public void zaprimiRezervaciju_imaPrimljenih_aktivneRezervacije() throws Exception {

    var a = aranzmani.get(1);
    dodajPrimljeneRezervacije(a, List.of("K1", "K2"));

    agent.zaprimiRezervaciju(a, dajRezervaciju("K3", a.oznaka()));

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 3, List.of("K1", "K2", "K3"));

  }

  @Test
  public void zaprimiRezervaciju_maxPutnika_rezervacijaNaCekanju() throws Exception {

    var a = aranzmani.get(1);
    dodajAktivneRezervacije(a, List.of("K1", "K2", "K3", "K4", "K5"));

    agent.zaprimiRezervaciju(a, dajRezervaciju("K6", a.oznaka()));

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 5, List.of("K1", "K2", "K3", "K4", "K5"));
    provjeriRezervacije(a.rezervacijeNaCekanju(), 1, List.of("K6"));

  }

  @Test
  public void otkaziRezervaciju_otkaziPrimljenu_rezervacijaOtkazana() throws Exception {

    var a = aranzmani.get(1);
    dodajAktivneRezervacije(a, List.of("K1", "K2"));

    agent.otkaziRezervaciju(a, "K2", "K2");

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 1, List.of("K1"));
    provjeriRezervacije(a.otkazaneRezervacije(), 1, List.of("K2"));

  }

  @Test
  public void otkaziRezervaciju_otkaziAktivnu_rezervacijaOtkazana() throws Exception {

    var a = aranzmani.get(1);
    dodajAktivneRezervacije(a, List.of("K1", "K2", "K3", "K4"));

    agent.otkaziRezervaciju(a, "K4", "K4");

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 3, List.of("K1", "K2", "K3"));
    provjeriRezervacije(a.otkazaneRezervacije(), 1, List.of("K4"));

  }

  @Test
  public void otkaziRezervaciju_otkaziAktivnuMinPutnika_rezervacijaOtkazana() throws Exception {

    var a = aranzmani.get(1);
    dodajAktivneRezervacije(a, List.of("K1", "K2", "K3"));

    agent.otkaziRezervaciju(a, "K3", "K3");

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 2, List.of("K1", "K2"));
    provjeriRezervacije(a.otkazaneRezervacije(), 1, List.of("K3"));

  }

  @Test
  public void otkaziRezervaciju_otkaziNaCekanju_rezervacijaOtkazana() throws Exception {

    var a = aranzmani.get(1);
    dodajAktivneRezervacije(a, List.of("K1", "K2", "K3", "K4", "K5"));
    dodajRezervacijeNaCekanju(a, List.of("K6", "K7"));

    agent.otkaziRezervaciju(a, "K7", "K7");

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 5, List.of("K1", "K2", "K3", "K4", "K5"));
    provjeriRezervacije(a.rezervacijeNaCekanju(), 1, List.of("K6"));
    provjeriRezervacije(a.otkazaneRezervacije(), 1, List.of("K7"));

  }

  @Test
  public void otkaziRezervaciju_otkaziAktivnuImaNaCekanju_rezervacijaOtkazana() throws Exception {

    var a = aranzmani.get(1);
    dodajAktivneRezervacije(a, List.of("K1", "K2", "K3", "K4", "K5"));
    dodajRezervacijeNaCekanju(a, List.of("K6", "K7"));

    agent.otkaziRezervaciju(a, "K5", "K5");

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 5, List.of("K1", "K2", "K3", "K4", "K6"));
    provjeriRezervacije(a.rezervacijeNaCekanju(), 1, List.of("K7"));
    provjeriRezervacije(a.otkazaneRezervacije(), 1, List.of("K5"));

  }

  @Test
  public void zaprimiRezervaciju_otkaziRezervaciju_scenarij1() throws Exception {

    var a = aranzmani.get(1);
    dodajAktivneRezervacije(a, List.of("K1", "K2"));

    agent.zaprimiRezervaciju(a, dajRezervaciju("K3", a.oznaka()));
    agent.zaprimiRezervaciju(a, dajRezervaciju("K4", a.oznaka()));

    agent.otkaziRezervaciju(a, "K1", "K1");
    agent.otkaziRezervaciju(a, "K2", "K2");

    agent.zaprimiRezervaciju(a, dajRezervaciju("K5", a.oznaka()));
    agent.zaprimiRezervaciju(a, dajRezervaciju("K6", a.oznaka()));
    agent.zaprimiRezervaciju(a, dajRezervaciju("K7", a.oznaka()));
    agent.zaprimiRezervaciju(a, dajRezervaciju("K8", a.oznaka()));
    agent.zaprimiRezervaciju(a, dajRezervaciju("K9", a.oznaka()));
    agent.zaprimiRezervaciju(a, dajRezervaciju("K10", a.oznaka()));

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 5, List.of("K3", "K4", "K5", "K6", "K7"));
    provjeriRezervacije(a.rezervacijeNaCekanju(), 3, List.of("K8", "K9", "K10"));
    provjeriRezervacije(a.otkazaneRezervacije(), 2, List.of("K1", "K2"));

    agent.otkaziRezervaciju(a, "K9", "K9");
    agent.otkaziRezervaciju(a, "K4", "K4");
    agent.otkaziRezervaciju(a, "K6", "K6");
    agent.zaprimiRezervaciju(a, dajRezervaciju("K11", a.oznaka()));
    agent.otkaziRezervaciju(a, "K3", "K3");

    provjeriVrstuRezervacija(a);
    provjeriRezervacije(a.rezervacije(), 5, List.of("K5", "K7", "K8", "K10", "K11"));
    provjeriRezervacije(a.rezervacijeNaCekanju(), 0, List.of());
    provjeriRezervacije(a.otkazaneRezervacije(), 6, List.of("K1", "K2", "K9", "K4", "K6", "K3"));

  }

  private void provjeriRezervacije(Collection<Rezervacija> rezervacije, int brojRezervacija, List<String> korisnici) {
    assertEquals(brojRezervacija, rezervacije.size());
    for (var korisnik : korisnici) {
      boolean postoji = rezervacije.stream().anyMatch(e -> e.ime().equals(korisnik) && e.prezime().equals(korisnik));
      assertTrue(postoji);
    }
  }

  private void provjeriVrstuRezervacija(Aranzman a) {

    boolean aktivneRezervacije = a.rezervacije().size() >= a.minBrojPutnika();
    for (var r : a.rezervacije()) {
      if (aktivneRezervacije) {
        assertTrue(r instanceof AktivnaRezervacija);
      } else {
        assertTrue(r instanceof PrimljenaRezervacija);
      }
    }

    for (var r : a.rezervacijeNaCekanju()) {
      assertTrue(r instanceof RezervacijaNaCekanju);
    }

    for (var r : a.otkazaneRezervacije()) {
      assertTrue(r instanceof OtkazanaRezervacija);
    }
  }

  private void dodajPrimljeneRezervacije(Aranzman a, List<String> korisnici) {
    var kreatorRezervacije = new KreatorPrimljeneRezervacije();
    for (var korisnik : korisnici) {
      var r = dajRezervaciju(korisnik, a.oznaka());
      a.rezervacije().add(kreatorRezervacije.promijeniVrstu(r));
    }
  }

  private void dodajAktivneRezervacije(Aranzman a, List<String> korisnici) {
    var kreatorRezervacije = new KreatorAktivneRezervacije();
    for (var korisnik : korisnici) {
      var r = dajRezervaciju(korisnik, a.oznaka());
      a.rezervacije().add(kreatorRezervacije.promijeniVrstu(r));
    }
  }

  private void dodajRezervacijeNaCekanju(Aranzman a, List<String> korisnici) {
    var kreatorRezervacije = new KreatorRezervacijeNaCekanju();
    for (var korisnik : korisnici) {
      var r = dajRezervaciju(korisnik, a.oznaka());
      a.rezervacijeNaCekanju().add(kreatorRezervacije.promijeniVrstu(r));
    }
  }

  private Rezervacija dajRezervaciju(String korisnik, int oznaka) {
    var kreatorRezervacije = new KreatorPrimljeneRezervacije();
    LocalDateTime vrijeme = LocalDateTime.of(2025, 10, 1, 10, 0, 0);
    return kreatorRezervacije.napraviRezervaciju(korisnik, korisnik, oznaka, vrijeme);
  }

  private Aranzman dajAranzman(int oznaka, int minPutnika, int maxPutnika) {
    String naziv = "A" + oznaka;
    LocalDate pocetniDatum = LocalDate.of(2025, 10, 1);
    LocalDate zavrsniDatum = LocalDate.of(2025, 10, 10);
    var direktor = new AranzmanDirektor();
    return direktor.napraviAranzman(oznaka, naziv, "", pocetniDatum, zavrsniDatum, 0, minPutnika, maxPutnika);
  }

}
