package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.lib.facade.UcitavacPodatakaFacade;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RezervacijaRepozitorij {

  public List<Rezervacija> ucitajRezervacije(Path putanja) throws IOException {

    var ucitavacPodataka = new UcitavacPodatakaFacade();
    List<List<String>> csvRedci = ucitavacPodataka.ucitajRezervacije(putanja);

    List<Rezervacija> rezervacije = new ArrayList<>();

    for (List<String> redci : csvRedci) {
      String csvRedak = redci.getFirst();
      List<String> stupci = redci.subList(1, redci.size());

      try {
        Rezervacija rezervacija = parsirajRezervaciju(stupci);
        rezervacije.add(rezervacija);
      } catch (Exception e) {
        System.out.printf("[Greška] %s\n", e.getMessage());
        System.out.println(csvRedak);
      }
    }

    return rezervacije;
  }

  private Rezervacija parsirajRezervaciju(List<String> stupci) throws Exception {

    if (stupci.size() != 4) {
      String opis = String.format("Broj stupaca rezervacije mora biti 4! Trenutno: %d", stupci.size());
      throw new Exception(opis);
    }

    var f = Formati.dajInstancu();

    int index = 0;
    String ime = stupci.get(index++);
    String prezime = stupci.get(index++);
    int oznaka = Integer.parseInt(stupci.get(index++));
    LocalDateTime vrijemePrijema = f.parsirajDatumVrijeme(stupci.get(index));

    var korisnik = new Korisnik(ime, prezime);
    return new Rezervacija(korisnik, oznaka, vrijemePrijema);
  }

}
