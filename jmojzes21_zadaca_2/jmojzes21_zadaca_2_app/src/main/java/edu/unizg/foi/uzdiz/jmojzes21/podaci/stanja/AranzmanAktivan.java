package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import java.util.Comparator;
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

    boolean mozePostatiAktivna = aranzman.obavijestiRezervacijaPostajeAktivna(rezervacija);
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
  public void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik) throws Exception {

    Rezervacija rezervacija = dajRezervacijuKorisnika(aranzman, korisnik);

    if (rezervacija == null) {
      String opis = String.format("Korisnik %s nema rezervaciju za aranžman %d!", korisnik.punoIme(),
          aranzman.oznaka());
      throw new Exception(opis);
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

    provjeriStanje(aranzman);

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
  public String dajNaziv() {
    return "Aktivan";
  }

  private boolean korisnikImaAktivnuRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
    List<Rezervacija> rezervacije = aranzman.aktivneRezervacije();
    return rezervacije.stream()
        .anyMatch(e -> e.korisnik().equals(rezervacija.korisnik()));
  }

  private Rezervacija dajRezervacijuKorisnika(Aranzman aranzman, Korisnik korisnik) {
    List<Rezervacija> rezervacije = aranzman.aktivneRezervacije();
    return rezervacije.stream()
        .filter(e -> e.korisnik().equals(korisnik))
        .findFirst()
        .orElse(dajOdgodjenuRezervacijuKorisnika(aranzman, korisnik));
  }

  private Rezervacija dajOdgodjenuRezervacijuKorisnika(Aranzman aranzman, Korisnik korisnik) {
    List<Rezervacija> rezervacije = aranzman.odgodjeneRezervacije();
    return rezervacije.stream()
        .filter(e -> e.korisnik().equals(korisnik))
        .min(Comparator.comparing(Rezervacija::vrijemePrijema))
        .orElse(null);
  }

}
