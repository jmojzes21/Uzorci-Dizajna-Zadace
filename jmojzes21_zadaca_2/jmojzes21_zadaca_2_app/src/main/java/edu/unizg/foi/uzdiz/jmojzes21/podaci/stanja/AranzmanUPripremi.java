package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.FormatDatuma;
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
      try {
        aranzman.aktiviraj();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }

      var rezervacije = aranzman.primljeneAktivneRezervacije();
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
      var formatDatum = FormatDatuma.dajInstancu();
      System.out.printf(
          "Brisanje rezervacije korisnika %s, aranžman %d, vrijeme %s jer korisnik već ima primljenu rezervaciju.\n",
          neispravna.korisnik(), neispravna.oznakaAranzmana(), formatDatum.formatiraj(neispravna.vrijemePrijema()));
      aranzman.ukloni(neispravna);
    }

    int brojPrimljenih = aranzman.brojPrimljenih();

    if (brojPrimljenih < aranzman.minBrojPutnika()) {
      String opis = String.format("Aranžman %d ne može postati aktivan jer nema dovoljno primljenih rezervacija!",
          aranzman.oznaka());
      throw new Exception(opis);
    }

    for (Rezervacija rezervacija : rezervacije) {
      rezervacija.aktiviraj();
      aranzman.obavijestiAktiviranjeRezervacije(rezervacija);
    }

    aranzman.postaviStanje(new AranzmanAktivan());

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

}

