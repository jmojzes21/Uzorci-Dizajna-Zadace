package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import java.util.List;

public class AranzmanUPripremi extends AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {

    Rezervacija postojeca = dajRezervacijuKorisnika(aranzman, rezervacija.korisnik(), List.of(StanjeId.primljena));

    if (postojeca != null) {
      aranzman.dodaj(rezervacija);
      rezervacija.odgodi();
      return;
    }

    aranzman.dodaj(rezervacija);
    rezervacija.zaprimi();

    provjeriStanje(aranzman);

  }

  @Override
  public void aktiviraj(Aranzman aranzman) {

    List<Rezervacija> rezervacije = aranzman.primljeneRezervacije();
    int brojPrimljenih = rezervacije.size();

    if (brojPrimljenih < aranzman.minBrojPutnika()) {
      return;
    }

    for (Rezervacija rezervacija : rezervacije) {
      boolean mozePostatiAktivna = aranzman.dajAgenciju().rezervacijaMozePostatiAktivna(rezervacija);
      if (!mozePostatiAktivna) {
        rezervacija.odgodi();
      }
    }

    brojPrimljenih = aranzman.brojPrimljenih();
    if (brojPrimljenih < aranzman.minBrojPutnika()) {
      return;
    }

    for (Rezervacija rezervacija : rezervacije) {
      rezervacija.aktiviraj();
      aranzman.obavijestiRezervacijaPostalaAktivna(rezervacija);
    }

    aranzman.postaviStanje(new AranzmanAktivan());

  }

  @Override
  public void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik) {

    Rezervacija rezervacija = dajRezervacijuKorisnika(aranzman, korisnik,
        List.of(StanjeId.primljena, StanjeId.odgodjena));

    if (rezervacija == null) {
      String opis = String.format("Korisnik %s nema rezervaciju za aranžman %d!", korisnik.punoIme(),
          aranzman.oznaka());
      throw new RuntimeException(opis);
    }

    if (rezervacija.jePrimljena()) {
      otkaziPrimljenu(aranzman, rezervacija);
      return;
    }

    rezervacija.otkazi();

  }

  private void otkaziPrimljenu(Aranzman aranzman, Rezervacija primljena) {

    primljena.otkazi();

    Rezervacija odgodjena = dajRezervacijuKorisnika(aranzman, primljena.korisnik(), List.of(StanjeId.odgodjena));
    if (odgodjena != null) {
      odgodjena.zaprimi();
    }

  }

  @Override
  public void aktivirajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {}

  @Override
  public void provjeriStanje(Aranzman aranzman) {

    int brojPrimljenih = aranzman.primljeneRezervacije().size();

    if (brojPrimljenih >= aranzman.minBrojPutnika()) {
      aranzman.aktiviraj();
    }

  }

  @Override
  public Aranzman.StanjeId dajId() {
    return Aranzman.StanjeId.uPripremi;
  }

  @Override
  public String dajNaziv() {
    return "U pripremi";
  }

}
