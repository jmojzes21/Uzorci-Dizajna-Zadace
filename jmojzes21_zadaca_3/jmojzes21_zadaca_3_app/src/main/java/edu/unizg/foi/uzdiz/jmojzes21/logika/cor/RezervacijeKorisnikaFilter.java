package edu.unizg.foi.uzdiz.jmojzes21.logika.cor;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;

public class RezervacijeKorisnikaFilter extends FilterRezervacije {

  private final Korisnik korisnik;

  public RezervacijeKorisnikaFilter(Korisnik korisnik) {
    this.korisnik = korisnik;
  }

  @Override
  public boolean zadovoljava(Rezervacija r) {
    return r.korisnik().equals(korisnik) && super.zadovoljava(r);
  }

}
