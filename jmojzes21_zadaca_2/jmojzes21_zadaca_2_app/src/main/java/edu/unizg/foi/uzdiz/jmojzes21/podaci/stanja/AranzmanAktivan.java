package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import java.util.List;

public class AranzmanAktivan implements AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    if (korisnikImaAktivnuRezervaciju(aranzman, rezervacija)) {
      String opis = String.format("Korisnik %s već ima aktivnu rezervaciju za aranžman %d.", rezervacija.korisnik(),
          aranzman.oznaka());
      throw new Exception(opis);
    }

    aranzman.dodaj(rezervacija);
    rezervacija.zaprimi();

    int brojAktivnih = aranzman.brojAktivnih();

    if (brojAktivnih >= aranzman.maxBrojPutnika()) {
      rezervacija.staviNaCekanje();
      aranzman.postaviStanje(new AranzmanPopunjen());
      return;
    }

    rezervacija.aktiviraj();
    aranzman.obavijestiAktiviranjeRezervacije(rezervacija);

  }

  private boolean korisnikImaAktivnuRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
    List<Rezervacija> rezervacije = aranzman.aktivneRezervacije();
    return rezervacije.stream()
        .anyMatch(e -> e.korisnik().equals(rezervacija.korisnik()));
  }

}
