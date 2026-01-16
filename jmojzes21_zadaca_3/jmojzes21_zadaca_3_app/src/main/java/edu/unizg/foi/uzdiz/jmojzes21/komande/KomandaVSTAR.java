package edu.unizg.foi.uzdiz.jmojzes21.komande;

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
