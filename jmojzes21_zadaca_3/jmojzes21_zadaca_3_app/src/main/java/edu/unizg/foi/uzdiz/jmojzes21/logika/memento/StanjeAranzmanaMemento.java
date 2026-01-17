package edu.unizg.foi.uzdiz.jmojzes21.logika.memento;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.AranzmanStanje;
import java.util.List;

public class StanjeAranzmanaMemento {

  private final AranzmanStanje stanje;
  private final List<Rezervacija> rezervacije;

  public StanjeAranzmanaMemento(Aranzman aranzman) {
    stanje = aranzman.stanje();
    rezervacije = aranzman.rezervacije().stream().map(e -> e.kopiraj()).toList();
  }

  public AranzmanStanje stanje() {return stanje;}

  public List<Rezervacija> rezervacije() {return rezervacije;}

}
