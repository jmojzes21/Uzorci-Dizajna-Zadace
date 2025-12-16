package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AranzmanPopunjen implements AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    if (korisnikImaAktivnuRezervaciju(aranzman, rezervacija)) {
      String opis = String.format("Korisnik %s već ima aktivnu rezervaciju za aranžman %d.", rezervacija.korisnik(),
          aranzman.oznaka());
      throw new Exception(opis);
    }

    aranzman.dodaj(rezervacija);

    List<Rezervacija> aktivne = aranzman.aktivneRezervacije();
    Rezervacija najnovijaAktivna = aranzman.dajNajnovijuRezervaciju(aktivne);

    if (rezervacija.vrijemePrijema().isBefore(najnovijaAktivna.vrijemePrijema())) {
      boolean mozePostatiAktivna = aranzman.obavijestiRezervacijaPostajeAktivna(rezervacija);
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

    List<Rezervacija> kandidati = new ArrayList<>();
    kandidati.addAll(aranzman.rezervacijeNaCekanju());
    kandidati.addAll(aranzman.odgodjeneRezervacije());
    Rezervacija.sortiraj(kandidati, true);

    for (Rezervacija kandidat : kandidati) {
      boolean mozePostatiAktivna = aranzman.obavijestiRezervacijaPostajeAktivna(kandidat);
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

  private boolean korisnikImaAktivnuRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
    List<Rezervacija> rezervacije = aranzman.aktivneRezervacije();
    return rezervacije.stream()
        .anyMatch(e -> e.korisnik().equals(rezervacija.korisnik()));
  }

  private Rezervacija dajRezervacijuKorisnika(Aranzman aranzman, Korisnik korisnik) {
    List<Rezervacija> rezervacije = aranzman.aktivneRezervacije();
    Rezervacija rezervacija = rezervacije.stream()
        .filter(e -> e.korisnik().equals(korisnik))
        .findFirst().orElse(null);

    if (rezervacija == null) {
      rezervacije = new ArrayList<>();
      rezervacije.addAll(aranzman.rezervacijeNaCekanju());
      rezervacije.addAll(aranzman.odgodjeneRezervacije());
      rezervacija = rezervacije.stream()
          .filter(e -> e.korisnik().equals(korisnik))
          .min(Comparator.comparing(Rezervacija::vrijemePrijema))
          .orElse(null);
    }

    return rezervacija;
  }

}
