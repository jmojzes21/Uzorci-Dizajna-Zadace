package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.util.List;

public class TuristickiAgent {

  private final Aranzman aranzman;

  public TuristickiAgent(Aranzman aranzman) {
    this.aranzman = aranzman;
  }

  public void zaprimiRezervaciju(Rezervacija rezervacija) {

    int brojAktivnihRezervacija = aranzman.brojAktivnihRezervacija();
    int minPutnika = aranzman.minBrojPutnika();
    int maxPutnika = aranzman.maxBrojPutnika();

    if (brojAktivnihRezervacija >= maxPutnika) {
      dodajRezervacijuNaCekanje(rezervacija);
      return;
    }

    if (brojAktivnihRezervacija >= minPutnika) {
      dodajAktivnuRezervaciju(rezervacija);
      return;
    }

    dodajPrimljenuRezervaciju(rezervacija);

  }

  private void dodajPrimljenuRezervaciju(Rezervacija rezervacija) {

    List<Rezervacija> primljeneRezervacije = aranzman.primljeneRezervacije();
    List<Rezervacija> aktivneRezervacije = aranzman.aktivneRezervacije();

    int brojPrimljenihRezervacija = aranzman.brojPrimljenihRezervacija();
    int minPutnika = aranzman.minBrojPutnika();

    if (brojPrimljenihRezervacija + 1 >= minPutnika) {
      KreatorRezervacije kreatorRezervacije = new KreatorAktivneRezervacije();

      aktivneRezervacije.addAll(primljeneRezervacije.stream()
          .map(r -> kreatorRezervacije.promijeniVrstu(r))
          .toList());
      primljeneRezervacije.clear();

      var novaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);
      aktivneRezervacije.add(novaRezervacija);

    } else {
      KreatorRezervacije kreatorRezervacije = new KreatorPrimljeneRezervacije();

      var novaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);
      primljeneRezervacije.add(novaRezervacija);
    }

  }

  private void dodajAktivnuRezervaciju(Rezervacija rezervacija) {
    KreatorRezervacije kreatorRezervacije = new KreatorAktivneRezervacije();

    var novaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);
    aranzman.aktivneRezervacije().add(novaRezervacija);
  }

  private void dodajRezervacijuNaCekanje(Rezervacija rezervacija) {
    KreatorRezervacije kreatorRezervacije = new KreatorRezervacijeNaCekanju();

    var novaRezervacija = kreatorRezervacije.promijeniVrstu(rezervacija);
    aranzman.rezervacijeNaCekanju().add(novaRezervacija);

  }

}
