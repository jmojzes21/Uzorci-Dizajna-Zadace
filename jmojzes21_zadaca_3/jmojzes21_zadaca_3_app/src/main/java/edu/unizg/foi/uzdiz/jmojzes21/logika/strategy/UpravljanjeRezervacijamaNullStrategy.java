package edu.unizg.foi.uzdiz.jmojzes21.logika.strategy;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;

public class UpravljanjeRezervacijamaNullStrategy extends UpravljanjeRezervacijamaStrategy {

  @Override
  public boolean mozePostatiAktivna(TuristickaAgencija agencija, Rezervacija rezervacija) {
    return true;
  }

  @Override
  public void kadaRezervacijaKorisnikaPostalaAktivna(TuristickaAgencija agencija, Rezervacija rezervacija,
      Rezervacija aktivirana) {}

}
