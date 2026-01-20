package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.IKomanda;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.KomandaKreator;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaORTA implements IKomanda {

  private final String ime;
  private final String prezime;
  private final int oznaka;

  public KomandaORTA(String ime, String prezime, int oznaka) {
    this.ime = ime;
    this.prezime = prezime;
    this.oznaka = oznaka;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    try {
      agencija.otkaziRezervaciju(ime, prezime, oznaka);
      System.out.println("Rezervacija je uspješno otkazana.");
    } catch (Exception e) {
      System.out.println("Otkazivanje rezervacije nije uspjelo. " + e.getMessage());
    }

  }


  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var uzorak = new RegexKomandeGraditelj()
          .dodajTekst("ime")
          .dodajTekst("prezime")
          .dodajBroj("oznaka")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "ORTA ime prezime oznaka";
        throw new NeispravnaKomandaGreska(opis);
      }

      String ime = matcher.group("ime");
      String prezime = matcher.group("prezime");
      int oznaka = Integer.parseInt(matcher.group("oznaka"));

      return new KomandaORTA(ime, prezime, oznaka);
    }
  }

}
