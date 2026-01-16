package edu.unizg.foi.uzdiz.jmojzes21.logika.strategy;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;

public abstract class UpravljanjeRezervacijamaStrategy {


  /**
   * Provjeri može li rezervacija postati aktivna.
   *
   * @param agencija
   * @param rezervacija
   * @return
   */
  public abstract boolean mozePostatiAktivna(TuristickaAgencija agencija, Rezervacija rezervacija);

  public abstract void kadaRezervacijaKorisnikaPostalaAktivna(TuristickaAgencija agencija, Rezervacija rezervacija,
      Rezervacija aktivirana);

}
