package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.logika.memento.StanjeAranzmanaCaretaker;
import edu.unizg.foi.uzdiz.jmojzes21.logika.memento.StanjeAranzmanaMemento;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaPSTAR implements IKomanda {

  private final int oznaka;

  public KomandaPSTAR(int oznaka) {
    this.oznaka = oznaka;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    Aranzman aranzman = agencija.dajAranzman(oznaka);
    if (aranzman == null) {
      throw new RuntimeException("Aranžman s oznakom " + oznaka + " ne postoji!");
    }

    StanjeAranzmanaCaretaker spremnik = agencija.stanjeAranzmanaSpremnik();
    StanjeAranzmanaMemento memento = aranzman.spremiStanje();

    spremnik.spremiMemento(oznaka, memento);
    System.out.println("Stanje aranžmana je uspješno spremljeno.");

  }

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var uzorak = new RegexKomandeGraditelj()
          .dodajBroj("oznaka")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "PSTAR oznaka";
        throw new NeispravnaKomandaGreska(opis);
      }

      int oznaka = Integer.parseInt(matcher.group("oznaka"));

      return new KomandaPSTAR(oznaka);
    }
  }

}
