package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AranzmanUPripremi implements AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    aranzman.dodaj(rezervacija);
    rezervacija.zaprimi();

    int brojPrimljenih = aranzman.primljeneRezervacije().size();

    if (brojPrimljenih >= aranzman.minBrojPutnika()) {

      aranzman.aktiviraj();

      var rezervacije = aranzman.rezervacije();
      if (!rezervacije.contains(rezervacija)) {
        throw new Exception("Rezervacije je obrisana.");
      }

    }

  }

  @Override
  public void aktiviraj(Aranzman aranzman) throws Exception {

    List<Rezervacija> rezervacije = aranzman.primljeneRezervacije();

    List<Rezervacija> neispravne = new ArrayList<>();
    filtrirajDuplikate(rezervacije, neispravne);

    for (Rezervacija neispravna : neispravne) {
      var f = Formati.dajInstancu();
      System.out.printf(
          "Brisanje rezervacije korisnika %s, aranžman %d, vrijeme %s jer korisnik već ima primljenu rezervaciju.\n",
          neispravna.korisnik(), neispravna.oznakaAranzmana(), f.formatiraj(neispravna.vrijemePrijema()));
      aranzman.obrisi(neispravna);
    }

    int brojPrimljenih = aranzman.brojPrimljenih();

    if (brojPrimljenih < aranzman.minBrojPutnika()) {
      System.out.printf("Aranžman %d ne može postati aktivan jer nema dovoljno primljenih rezervacija!\n",
          aranzman.oznaka());
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
      System.out.printf("Aranžman %d ne može postati aktivan jer nema dovoljno primljenih rezervacija!\n",
          aranzman.oznaka());
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

    Rezervacija zaOtkazati = dajRezervacijuKorisnika(aranzman, korisnik);

    if (zaOtkazati == null) {
      String opis = String.format("Korisnik %s nema rezervaciju za aranžman %d!", korisnik.punoIme(),
          aranzman.oznaka());
      throw new Exception(opis);
    }

    zaOtkazati.otkazi();

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
        .findFirst().orElse(null);
  }

}

