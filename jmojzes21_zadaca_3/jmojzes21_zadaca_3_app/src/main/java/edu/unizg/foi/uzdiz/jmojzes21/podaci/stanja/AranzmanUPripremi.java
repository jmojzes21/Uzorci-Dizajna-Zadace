package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AranzmanUPripremi implements AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    aranzman.dodaj(rezervacija);
    rezervacija.zaprimi();

    provjeriStanje(aranzman);

  }

  @Override
  public void aktiviraj(Aranzman aranzman) {

    List<Rezervacija> rezervacije = aranzman.primljeneRezervacije();

    List<Rezervacija> neispravne = new ArrayList<>();
    filtrirajDuplikate(rezervacije, neispravne);

    for (Rezervacija neispravna : neispravne) {
      neispravna.odgodi();
    }

    rezervacije = aranzman.primljeneRezervacije();
    int brojPrimljenih = aranzman.brojPrimljenih();

    if (brojPrimljenih < aranzman.minBrojPutnika()) {
      return;
    }

    for (Rezervacija rezervacija : rezervacije) {
      boolean mozePostatiAktivna = aranzman.obavijestiRezervacijaPostajeAktivna(rezervacija);
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
  public void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik) throws Exception {

    Rezervacija rezervacija = dajRezervacijuKorisnika(aranzman, korisnik);

    if (rezervacija == null) {
      String opis = String.format("Korisnik %s nema rezervaciju za aranžman %d!", korisnik.punoIme(),
          aranzman.oznaka());
      throw new Exception(opis);
    }

    if (rezervacija.jePrimljena()) {
      otkaziPrimljenu(aranzman, rezervacija);
      return;
    }

    rezervacija.otkazi();

  }

  private void otkaziPrimljenu(Aranzman aranzman, Rezervacija primljena) {

    primljena.otkazi();

    Rezervacija odgodjena = dajOdgodjenuRezervacijuKorisnika(aranzman, primljena.korisnik());
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
      try {
        aranzman.aktiviraj();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

  }

  @Override
  public StanjeId dajId() {
    return StanjeId.uPripremi;
  }

  @Override
  public String dajNaziv() {
    return "U pripremi";
  }

  /**
   * Filtriraj rezervacije korisnika tako da korisnik ima samo jednu rezervaciju. Ostale rezervacije postaju
   * neispravne.
   *
   * @param rezervacije rezervacije
   * @param neispravne  neispravne rezervacije
   */
  private void filtrirajDuplikate(List<Rezervacija> rezervacije, List<Rezervacija> neispravne) {

    Map<Korisnik, Rezervacija> ispravneRezervacije = new HashMap<>();

    for (Rezervacija rezervacija : rezervacije) {

      Korisnik korisnik = rezervacija.korisnik();

      if (!ispravneRezervacije.containsKey(korisnik)) {
        ispravneRezervacije.put(korisnik, rezervacija);
        continue;
      }

      Rezervacija ispravnaRezervacija = ispravneRezervacije.get(korisnik);

      if (rezervacija.vrijemePrijema().isAfter(ispravnaRezervacija.vrijemePrijema())) {
        neispravne.add(rezervacija);
      } else {
        neispravne.add(ispravnaRezervacija);
        ispravneRezervacije.put(korisnik, rezervacija);
      }

    }

  }

  private Rezervacija dajRezervacijuKorisnika(Aranzman aranzman, Korisnik korisnik) {
    List<Rezervacija> rezervacije = aranzman.primljeneRezervacije();
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
