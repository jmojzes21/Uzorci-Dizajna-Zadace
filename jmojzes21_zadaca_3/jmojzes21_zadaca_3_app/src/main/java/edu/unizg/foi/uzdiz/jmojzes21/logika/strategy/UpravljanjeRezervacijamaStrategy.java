package edu.unizg.foi.uzdiz.jmojzes21.logika.strategy;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;

public abstract class UpravljanjeRezervacijamaStrategy {

  /**
   * Provjeri može li aranžman koji je u pripremi zaprimiti rezervaciju.
   *
   * @param aranzman
   * @param rezervacija
   * @return
   */
  public abstract boolean mozeZaprimiti(Aranzman aranzman, Rezervacija rezervacija);

  /**
   * Provjeri može li rezervacija postati aktivna.
   *
   * @param aranzman
   * @param rezervacija
   * @return
   */
  public abstract boolean mozePostatiAktivna(Aranzman aranzman, Rezervacija rezervacija);

  /**
   * Obradi događaj kada je neka rezervacija korisnika postala aktivna.
   *
   * @param rezervacija rezervacija koja obrađuje događaj
   * @param aktivirana  rezervacija koja je postala aktivna
   */
  public abstract void kadaRezervacijaKorisnikaPostalaAktivna(Rezervacija rezervacija, Rezervacija aktivirana);

}
