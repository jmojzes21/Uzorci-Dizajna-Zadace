package edu.unizg.foi.uzdiz.jmojzes21.zadaca;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.AranzmanDirektor;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.KreatorPrimljeneRezervacije;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.TuristickiAgent;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TuristickiAgentTest {

  private TuristickiAgent agent;
  private Map<Integer, Aranzman> aranzmani;

  @BeforeEach
  public void prijeSvakoga() {
    var a1 = dajTestAranzman(1, 5, 10);
    aranzmani = Map.ofEntries(
        Map.entry(1, a1)
    );

    agent = new TuristickiAgent(aranzmani);
  }

  @Test
  public void zaprimiRezervaciju_rezervacijaPrimljena() throws Exception {

    var a1 = aranzmani.get(1);
    var r1 = dajRezervaciju("K1", 1);

    agent.zaprimiRezervaciju(a1, r1);

    var rezervacije = a1.rezervacije();
    assertEquals(1, a1.brojRezervacija());

  }

  private Rezervacija dajRezervaciju(String korisnik, int oznaka) {
    var kreatorRezervacije = new KreatorPrimljeneRezervacije();
    LocalDateTime vrijeme = LocalDateTime.of(2025, 10, 1, 10, 0, 0);
    return kreatorRezervacije.napraviRezervaciju(korisnik, korisnik, oznaka, vrijeme);
  }

  private Aranzman dajTestAranzman(int oznaka, int minPutnika, int maxPutnika) {
    String naziv = "Aranžman " + oznaka;
    LocalDate pocetniDatum = LocalDate.of(2025, 10, 1);
    LocalDate zavrsniDatum = LocalDate.of(2025, 10, 10);
    var direktor = new AranzmanDirektor();
    return direktor.napraviAranzman(oznaka, naziv, pocetniDatum, zavrsniDatum, minPutnika, maxPutnika);
  }

}
