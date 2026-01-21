package edu.unizg.foi.uzdiz.jmojzes21.logika.strategy;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import java.util.List;

public class UpravljanjeRezervacijamaJDR extends UpravljanjeRezervacijamaStrategy {

  @Override
  public boolean mozeZaprimiti(Aranzman aranzman, Rezervacija rezervacija) {
    Rezervacija postojeca = aranzman.dajRezervacijuKorisnika(rezervacija.korisnik(), List.of(StanjeId.primljena));
    return postojeca == null;
  }

  @Override
  public boolean mozePostatiAktivna(Aranzman aranzman, Rezervacija postajeAktivna) {

    Rezervacija postojeca = aranzman.dajRezervacijuKorisnika(postajeAktivna.korisnik(), List.of(StanjeId.aktivna));

    if (postojeca != null) {
      return false;
    }

    var agencija = aranzman.dajAgenciju();

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

  @Override
  public void kadaRezervacijaKorisnikaPostalaAktivna(Rezervacija rezervacija, Rezervacija aktivirana) {

    Aranzman aranzman1 = rezervacija.dajAranzman();
    Aranzman aranzman2 = aktivirana.dajAranzman();

    if (aranzman1.preklapaSe(aranzman2)) {
      rezervacija.odgodi();
      rezervacija.dajAranzman().provjeriStanje();
    }

  }

}
