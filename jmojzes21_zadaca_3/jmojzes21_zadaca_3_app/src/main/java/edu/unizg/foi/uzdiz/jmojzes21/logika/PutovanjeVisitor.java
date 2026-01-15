package edu.unizg.foi.uzdiz.jmojzes21.logika;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;

public interface PutovanjeVisitor {

  void posjeti(Aranzman aranzman);

  void posjeti(Rezervacija rezervacija);

}
