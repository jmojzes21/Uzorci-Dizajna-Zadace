package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuristickaAgencija {

  private static TuristickaAgencija turistickaAgencija;

  public static TuristickaAgencija dajInstancu() {
    if (turistickaAgencija == null) {
      turistickaAgencija = new TuristickaAgencija();
    }
    return turistickaAgencija;
  }

  private final Map<Integer, Aranzman> aranzmani = new HashMap<>();

  public TuristickaAgencija() {

  }

  public void zaprimiRezervaciju(Rezervacija rezervacija) throws Exception {

    Aranzman aranzman = aranzmani.get(rezervacija.oznakaAranzmana);
    if (aranzman == null) {
      throw new Exception("Nije moguće zaprimiti rezervaciju!");
    }

    var agent = new TuristickiAgent(aranzman);
    agent.zaprimiRezervaciju(rezervacija);

  }

  public void ucitajAranzmane(List<Aranzman> aranzmani) {
    this.aranzmani.clear();

    for (Aranzman aranzman : aranzmani) {
      this.aranzmani.put(aranzman.oznaka(), aranzman);
    }
  }

}
