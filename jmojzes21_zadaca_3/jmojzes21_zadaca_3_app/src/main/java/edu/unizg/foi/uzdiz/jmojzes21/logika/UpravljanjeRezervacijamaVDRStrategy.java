package edu.unizg.foi.uzdiz.jmojzes21.logika;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;

public class UpravljanjeRezervacijamaVDRStrategy extends UpravljanjeRezervacijamaStrategy {

  @Override
  public boolean mozePostatiAktivna(TuristickaAgencija agencija, Rezervacija rezervacija) {
    return true;
  }

}
