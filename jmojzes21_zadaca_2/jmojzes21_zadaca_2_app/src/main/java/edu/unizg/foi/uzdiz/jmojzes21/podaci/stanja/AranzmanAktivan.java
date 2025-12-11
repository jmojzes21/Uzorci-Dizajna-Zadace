package edu.unizg.foi.uzdiz.jmojzes21.podaci.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;

public class AranzmanAktivan implements AranzmanStanje {

  @Override
  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    aranzman.dodaj(rezervacija);
    rezervacija.zaprimi();

    rezervacija.aktiviraj();
    aranzman.obavijestiAktiviranjeRezervacije(rezervacija);
  }

}
