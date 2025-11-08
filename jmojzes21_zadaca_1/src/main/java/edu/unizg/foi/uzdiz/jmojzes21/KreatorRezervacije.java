package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import java.time.LocalDateTime;

public abstract class KreatorRezervacije {

  /**
   * Napravi novi objekt rezervacije.
   *
   * @param korisnik     korisnik rezervacije
   * @param oznaka       oznaka turističkog aranžmana
   * @param datumVrijeme datum i vrijeme rezervacije
   * @return rezervacija
   */
  public abstract Rezervacija napraviRezervaciju(Korisnik korisnik, int oznaka,
      LocalDateTime datumVrijeme);

  /**
   * Promijeni tip već postojećeg objekta rezervacije.
   *
   * @param r rezervacija
   * @return rezervacija određenog tipa
   */
  public abstract Rezervacija promijeniVrstu(Rezervacija r);

}
