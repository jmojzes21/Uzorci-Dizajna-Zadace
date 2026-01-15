package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import java.util.List;

public class AranzmanAktivan extends AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {

    Rezervacija postojeca = dajRezervacijuKorisnika(aranzman, rezervacija.korisnik(),
        List.of(Rezervacija.StanjeId.aktivna));

    if (postojeca != null) {
      aranzman.dodaj(rezervacija);
      rezervacija.odgodi();
      return;
    }

    aranzman.dodaj(rezervacija);
    rezervacija.zaprimi();

    boolean mozePostatiAktivna = aranzman.dajAgenciju().rezervacijaMozePostatiAktivna(rezervacija);
    if (!mozePostatiAktivna) {
      rezervacija.odgodi();
      return;
    }

    rezervacija.aktiviraj();
    aranzman.obavijestiRezervacijaPostalaAktivna(rezervacija);

    provjeriStanje(aranzman);

  }

  @Override
  public void aktiviraj(Aranzman aranzman) {}

  @Override
  public void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik) {

    Rezervacija rezervacija = dajRezervacijuKorisnika(aranzman, korisnik,
        List.of(Rezervacija.StanjeId.aktivna, Rezervacija.StanjeId.odgodjena));

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
    aranzman.obavijestiRezervacijaPostalaOtkazana(rezervacija);

    Rezervacija odgodjena = dajRezervacijuKorisnika(aranzman, rezervacija.korisnik(),
        List.of(Rezervacija.StanjeId.odgodjena));

    if (odgodjena != null) {
      boolean mozePostatiAktivna = aranzman.dajAgenciju().rezervacijaMozePostatiAktivna(odgodjena);
      if (mozePostatiAktivna) {
        odgodjena.aktiviraj();
        if (odgodjena.jeAktivna()) {
          aranzman.obavijestiRezervacijaPostalaAktivna(odgodjena);
        }
      }
    }

    provjeriStanje(aranzman);

  }

  @Override
  public void aktivirajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {

    boolean mozePostatiAktivna = aranzman.dajAgenciju().rezervacijaMozePostatiAktivna(rezervacija);

    if (mozePostatiAktivna) {
      rezervacija.aktiviraj();
      aranzman.obavijestiRezervacijaPostalaAktivna(rezervacija);
      aranzman.provjeriStanje();
    }

  }

  @Override
  public void provjeriStanje(Aranzman aranzman) {

    List<Rezervacija> aktivneRezervacije = aranzman.aktivneRezervacije();
    int brojAktivnih = aktivneRezervacije.size();

    if (brojAktivnih < aranzman.minBrojPutnika()) {
      for (var r : aktivneRezervacije) {
        r.zaprimi();
      }
      aranzman.postaviStanje(new AranzmanUPripremi());
    }

    if (brojAktivnih >= aranzman.maxBrojPutnika()) {
      aranzman.postaviStanje(new AranzmanPopunjen());
    }

  }

  @Override
  public StanjeId dajId() {
    return StanjeId.aktivan;
  }

  @Override
  public String dajNaziv() {
    return "Aktivan";
  }

}
