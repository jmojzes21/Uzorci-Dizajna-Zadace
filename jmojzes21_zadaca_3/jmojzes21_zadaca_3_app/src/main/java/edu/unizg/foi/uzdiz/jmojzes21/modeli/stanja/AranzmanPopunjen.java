package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import java.util.ArrayList;
import java.util.List;

public class AranzmanPopunjen extends AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {

    aranzman.dodaj(rezervacija);

    List<Rezervacija> aktivne = aranzman.aktivneRezervacije();
    Rezervacija najnovijaAktivna = aranzman.dajNajnovijuRezervaciju(aktivne);

    if (rezervacija.vrijemePrijema().isBefore(najnovijaAktivna.vrijemePrijema())) {
      boolean mozePostatiAktivna = aranzman.dajAgenciju().rezervacijaMozePostatiAktivna(rezervacija);
      if (mozePostatiAktivna) {
        najnovijaAktivna.staviNaCekanje();
        rezervacija.zaprimi();
        rezervacija.aktiviraj();
        return;
      }
    }

    rezervacija.staviNaCekanje();

  }

  @Override
  public void aktiviraj(Aranzman aranzman) {}

  @Override
  public void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik) {

    Rezervacija rezervacija = dajRezervacijuKorisnika(aranzman, korisnik,
        List.of(Rezervacija.StanjeId.aktivna, Rezervacija.StanjeId.odgodjena, Rezervacija.StanjeId.naCekanju));

    if (rezervacija == null) {
      String opis = String.format("Korisnik %s nema rezervaciju za aranžman %d!", korisnik.punoIme(),
          aranzman.oznaka());
      throw new RuntimeException(opis);
    }

    if (rezervacija.jeAktivna()) {
      otkaziAktivnu(aranzman, rezervacija);
      return;
    }

    rezervacija.otkazi();

  }

  private void otkaziAktivnu(Aranzman aranzman, Rezervacija rezervacija) {

    rezervacija.otkazi();
    aranzman.obavijestiRezervacijaNijeViseAktivna(rezervacija);

    List<Rezervacija> kandidati = new ArrayList<>();
    kandidati.addAll(aranzman.rezervacijeNaCekanju());
    kandidati.addAll(aranzman.odgodjeneRezervacije());
    Rezervacija.sortiraj(kandidati, true);

    for (Rezervacija kandidat : kandidati) {
      boolean mozePostatiAktivna = aranzman.dajAgenciju().rezervacijaMozePostatiAktivna(kandidat);
      if (mozePostatiAktivna) {
        kandidat.aktiviraj();
        if (kandidat.jeAktivna()) {
          aranzman.obavijestiRezervacijaPostalaAktivna(kandidat);
          break;
        }
      }
    }

    provjeriStanje(aranzman);

  }

  @Override
  public void aktivirajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
    rezervacija.staviNaCekanje();
  }

  @Override
  public void provjeriStanje(Aranzman aranzman) {

    List<Rezervacija> aktivneRezervacije = aranzman.aktivneRezervacije();
    int brojAktivnih = aktivneRezervacije.size();

    if (brojAktivnih < aranzman.maxBrojPutnika()) {
      aranzman.postaviStanje(new AranzmanAktivan());
      aranzman.provjeriStanje();
    }

  }

  @Override
  public StanjeId dajId() {
    return StanjeId.popunjen;
  }

  @Override
  public String dajNaziv() {
    return "Popunjen";
  }

}
