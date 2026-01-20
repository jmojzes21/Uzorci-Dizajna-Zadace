package edu.unizg.foi.uzdiz.jmojzes21.logika.cor;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;

public class RezervacijeAranzmanaFilter extends FilterRezervacije {

  private final int oznaka;

  public RezervacijeAranzmanaFilter(int oznaka) {
    this.oznaka = oznaka;
  }

  @Override
  public boolean zadovoljava(Rezervacija r) {
    return r.oznakaAranzmana() == oznaka && super.zadovoljava(r);
  }

}
