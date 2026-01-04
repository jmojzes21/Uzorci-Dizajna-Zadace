package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public class AranzmanOtkazan implements AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {
    String opis = String.format("Nije moguće zaprimi rezervaciju jer je aranžman %d otkazan.", aranzman.oznaka());
    throw new Exception(opis);
  }

  @Override
  public void aktiviraj(Aranzman aranzman) throws Exception {
    String opis = String.format("Nije moguće aktivirati aranžman %d jer je otkazan.", aranzman.oznaka());
    throw new Exception(opis);
  }

  @Override
  public void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik) throws Exception {
    String opis = String.format("Nije moguće otkazati rezervaciju za aranžman %d jer je otkazan.", aranzman.oznaka());
    throw new Exception(opis);
  }

  @Override
  public void aktivirajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {}

  @Override
  public void provjeriStanje(Aranzman aranzman) {}

  @Override
  public StanjeId dajId() {
    return StanjeId.otkazan;
  }

  @Override
  public String dajNaziv() {
    return "Otkazan";
  }

}
