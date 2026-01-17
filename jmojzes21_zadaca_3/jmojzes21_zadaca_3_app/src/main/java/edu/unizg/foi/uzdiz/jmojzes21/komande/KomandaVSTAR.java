package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.logika.memento.StanjeAranzmanaCaretaker;
import edu.unizg.foi.uzdiz.jmojzes21.logika.memento.StanjeAranzmanaMemento;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaVSTAR implements IKomanda {

  private final int oznaka;

  public KomandaVSTAR(int oznaka) {
    this.oznaka = oznaka;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    StanjeAranzmanaCaretaker spremnik = agencija.stanjeAranzmanaSpremnik();

    try {
      StanjeAranzmanaMemento memento = spremnik.dajMemento(oznaka);

      Aranzman aranzman = agencija.dajAranzman(oznaka);

      if (aranzman == null) {
        aranzman = new Aranzman(memento.oznaka(), memento.naziv());
        agencija.dodajAranzman(aranzman);
      }

      aranzman.obnoviStanje(memento);

      System.out.println("Stanje aranžmana je uspješno vraćeno.");


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }


  }

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var uzorak = new RegexKomandeGraditelj()
          .dodajBroj("oznaka")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "VSTAR oznaka";
        throw new NeispravnaKomandaGreska(opis);
      }

      int oznaka = Integer.parseInt(matcher.group("oznaka"));

      return new KomandaVSTAR(oznaka);
    }
  }

}
