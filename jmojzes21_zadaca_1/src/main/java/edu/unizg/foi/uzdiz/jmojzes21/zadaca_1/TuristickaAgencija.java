package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuristickaAgencija {

  private final Map<Integer, Aranzman> aranzmani = new HashMap<>();
 
  public TuristickaAgencija() {

  }

  public void ucitajAranzmane(List<Aranzman> aranzmani) {
    this.aranzmani.clear();

    for (Aranzman aranzman : aranzmani) {
      this.aranzmani.put(aranzman.oznaka(), aranzman);
    }
  }

}
