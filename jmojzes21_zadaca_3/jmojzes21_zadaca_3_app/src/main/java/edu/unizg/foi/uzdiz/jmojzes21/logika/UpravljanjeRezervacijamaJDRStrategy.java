package edu.unizg.foi.uzdiz.jmojzes21.logika;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import java.util.List;

public class UpravljanjeRezervacijamaJDRStrategy extends UpravljanjeRezervacijamaStrategy {

  @Override
  public boolean mozePostatiAktivna(TuristickaAgencija agencija, Rezervacija postajeAktivna) {

    var ime = postajeAktivna.korisnik().ime();
    var prezime = postajeAktivna.korisnik().prezime();
    var rezervacijeKorisnika = agencija.dajRezervacijeKorisnika(ime, prezime, List.of(StanjeId.aktivna));

    for (Rezervacija r : rezervacijeKorisnika) {
      Aranzman aranzman1 = r.dajAranzman();
      Aranzman aranzman2 = postajeAktivna.dajAranzman();

      if (aranzman1.preklapaSe(aranzman2)) {
        if (postajeAktivna.vrijemePrijema().isAfter(r.vrijemePrijema())) {
          return false;
        }
      }
    }

    return true;
  }

}
