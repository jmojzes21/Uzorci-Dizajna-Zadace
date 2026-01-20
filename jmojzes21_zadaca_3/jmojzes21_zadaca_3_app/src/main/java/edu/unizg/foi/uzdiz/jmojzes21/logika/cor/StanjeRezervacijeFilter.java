package edu.unizg.foi.uzdiz.jmojzes21.logika.cor;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import java.util.List;

public class StanjeRezervacijeFilter extends FilterRezervacije {

  private final List<StanjeId> stanja;

  public StanjeRezervacijeFilter(List<StanjeId> stanja) {
    this.stanja = stanja;
  }

  @Override
  public boolean zadovoljava(Rezervacija r) {
    return stanja.contains(r.idStanja()) && super.zadovoljava(r);
  }

}
