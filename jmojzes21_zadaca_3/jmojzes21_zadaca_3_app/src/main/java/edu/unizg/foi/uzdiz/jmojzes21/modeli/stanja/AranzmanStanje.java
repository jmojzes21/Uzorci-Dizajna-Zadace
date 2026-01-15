package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import java.util.List;

public abstract class AranzmanStanje {

  public abstract void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija);

  public abstract void aktiviraj(Aranzman aranzman);

  public abstract void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik);

  public abstract void aktivirajRezervaciju(Aranzman aranzman, Rezervacija rezervacija);

  public abstract void provjeriStanje(Aranzman aranzman);

  public abstract StanjeId dajId();

  public abstract String dajNaziv();

  public Rezervacija dajRezervacijuKorisnika(Aranzman aranzman, Korisnik korisnik,
      List<Rezervacija.StanjeId> prioritet) {
    var rezervacije = aranzman.rezervacije().stream()
        .filter(e -> e.korisnik().equals(korisnik))
        .toList();

    for (Rezervacija.StanjeId stanjeId : prioritet) {
      Rezervacija r = rezervacije.stream()
          .filter(e -> e.idStanja() == stanjeId)
          .findFirst().orElse(null);

      if (r != null) {
        return r;
      }
    }

    return null;
  }

}
