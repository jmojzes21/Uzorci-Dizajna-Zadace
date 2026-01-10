package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaOTA implements IKomanda {

  private final int oznaka;

  public KomandaOTA(int oznaka) {
    this.oznaka = oznaka;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    Aranzman aranzman = agencija.dajAranzman(oznaka);

    if (aranzman == null) {
      System.out.println("Aranžman ne postoji.");
      return;
    }

    try {
      aranzman.otkazi();
      System.out.println("Aranžman je uspješno otkazan.");
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
        String opis = "OTA oznaka";
        throw new NeispravnaKomandaGreska(opis);
      }

      int oznaka = Integer.parseInt(matcher.group("oznaka"));
      return new KomandaOTA(oznaka);
    }
  }

}
