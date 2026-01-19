package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;

public class RezervacijaOdgodjena extends RezervacijaStanje {

  @Override
  public void kadaRezervacijaNijeViseAktivna(Rezervacija trenutna, Rezervacija nijeAktivna) {

    if (trenutna.korisnik().equals(nijeAktivna.korisnik())) {
      Aranzman aranzman = trenutna.dajAranzman();
      aranzman.aktivirajRezervaciju(trenutna);
    }

  }

  @Override
  public StanjeId dajId() {
    return StanjeId.odgodjena;
  }

  @Override
  public String dajNaziv() {
    return "Odgođena";
  }

}
