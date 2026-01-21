package edu.unizg.foi.uzdiz.jmojzes21.logika.strategy;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;

public class UpravljanjeRezervacijamaNull extends UpravljanjeRezervacijamaStrategy {

  @Override
  public boolean mozeZaprimiti(Aranzman aranzman, Rezervacija rezervacija) {
    return true;
  }

  @Override
  public boolean mozePostatiAktivna(Aranzman aranzman, Rezervacija rezervacija) {
    return true;
  }

  @Override
  public void kadaRezervacijaKorisnikaPostalaAktivna(Rezervacija rezervacija, Rezervacija aktivirana) {

  }


}
