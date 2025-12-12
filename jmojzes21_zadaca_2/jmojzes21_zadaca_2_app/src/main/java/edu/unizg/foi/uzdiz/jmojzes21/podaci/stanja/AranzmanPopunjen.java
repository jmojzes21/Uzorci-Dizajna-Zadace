package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import java.util.List;

public class AranzmanPopunjen implements AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    aranzman.dodaj(rezervacija);

    List<Rezervacija> aktivne = aranzman.aktivneRezervacije();
    Rezervacija najnovijaAktivna = aranzman.dajNajnovijuRezervaciju(aktivne);

    if (rezervacija.vrijemePrijema().isBefore(najnovijaAktivna.vrijemePrijema())) {
      najnovijaAktivna.staviNaCekanje();
      rezervacija.zaprimi();
      rezervacija.aktiviraj();
      return;
    }

    rezervacija.staviNaCekanje();

  }

  @Override
  public void provjeriAktivneRezervacije(Aranzman aranzman) {
    List<Rezervacija> aktivneRezervacije = aranzman.aktivneRezervacije();
    int brojAktivnih = aktivneRezervacije.size();

    if (brojAktivnih < aranzman.maxBrojPutnika()) {
      aranzman.postaviStanje(new AranzmanAktivan());
    } else if (brojAktivnih < aranzman.minBrojPutnika()) {
      for (var r : aktivneRezervacije) {
        r.zaprimi();
      }
      aranzman.postaviStanje(new AranzmanUPripremi());
    }
  }

  @Override
  public String dajNaziv() {
    return "Popunjen";
  }


}
