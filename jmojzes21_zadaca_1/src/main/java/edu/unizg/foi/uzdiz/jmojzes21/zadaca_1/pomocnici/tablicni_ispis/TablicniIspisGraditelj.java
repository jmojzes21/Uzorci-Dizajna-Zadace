package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.tablicni_ispis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TablicniIspisGraditelj<T> {

  private final List<StupacTablice<T>> stupci;

  public TablicniIspisGraditelj() {
    stupci = new ArrayList<>();
  }

  public TablicniIspisGraditelj<T> dodajStupac(String naziv, int sirina,
      Function<T, String> dajVrijednost) {
    stupci.add(new StupacTablice<>(naziv, sirina, dajVrijednost, Poravnanje.lijevo));
    return this;
  }

  public TablicniIspisGraditelj<T> poravnajLijevo() {
    setPoravnanje(Poravnanje.lijevo);
    return this;
  }

  public TablicniIspisGraditelj<T> poravnajDesno() {
    setPoravnanje(Poravnanje.desno);
    return this;
  }

  public TablicniIspisGraditelj<T> setPoravnanje(Poravnanje poravnanje) {
    if (stupci.isEmpty()) {
      throw new IllegalStateException("Nema stupaca!");
    }
    stupci.getLast().setPoravnanje(poravnanje);
    return this;
  }

  public TablicniIspisGraditelj<T> prikazujStupac(boolean prikazi) {
    if (stupci.isEmpty()) {
      throw new IllegalStateException("Nema stupaca!");
    }
    stupci.getLast().setPrikaziStupac(prikazi);
    return this;
  }

  public TablicniIspis<T> napravi() {
    return new TablicniIspis<T>(stupci);
  }

}
