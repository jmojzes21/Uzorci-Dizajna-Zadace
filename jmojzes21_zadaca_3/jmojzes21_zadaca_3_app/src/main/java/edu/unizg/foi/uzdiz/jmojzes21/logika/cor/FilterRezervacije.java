package edu.unizg.foi.uzdiz.jmojzes21.logika.cor;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import java.util.ArrayList;
import java.util.List;

public abstract class FilterRezervacije {

  protected FilterRezervacije sljedeci;

  public FilterRezervacije() {}

  public boolean zadovoljava(Rezervacija r) {
    if (sljedeci == null) {return true;}
    return sljedeci.zadovoljava(r);
  }

  public FilterRezervacije sljedeci() {
    return sljedeci;
  }

  public void setSljedeci(FilterRezervacije sljedeci) {
    this.sljedeci = sljedeci;
  }

  public static class Graditelj {

    List<FilterRezervacije> lanac = new ArrayList<>();

    public Graditelj() {}

    public Graditelj dodaj(FilterRezervacije filter) {
      lanac.add(filter);
      return this;
    }

    public FilterRezervacije napraviLanac() {
      if (lanac.isEmpty()) {
        throw new RuntimeException("Potrebno je dodati barem jedan " + FilterRezervacije.class.getName());
      }

      for (int i = 0; i < lanac.size() - 1; i++) {
        lanac.get(i).setSljedeci(lanac.get(i + 1));
      }

      return lanac.getFirst();
    }

  }

}
