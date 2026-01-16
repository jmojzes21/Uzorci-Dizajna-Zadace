package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaUPTAR implements IKomanda {

  private final String ime;
  private final String prezime;
  private final int oznaka;

  public KomandaUPTAR(String ime, String prezime, int oznaka) {
    this.ime = ime;
    this.prezime = prezime;
    this.oznaka = oznaka;
  }

  public KomandaUPTAR(int oznaka) {
    this.ime = null;
    this.prezime = null;
    this.oznaka = oznaka;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

  }

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      IKomanda komanda = parsirajZaKorisnika(args);
      if (komanda != null) {
        return komanda;
      }

      komanda = parsirajZaAranzman(args);
      if (komanda != null) {
        return komanda;
      }

      String opis = "UPTAR [ime prezime oznaka] | [oznaka]";
      throw new NeispravnaKomandaGreska(opis);

    }

    private IKomanda parsirajZaKorisnika(String args) {

      var uzorak = new RegexKomandeGraditelj()
          .dodajTekst("ime")
          .dodajTekst("prezime")
          .dodajBroj("oznaka")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        return null;
      }

      String ime = matcher.group("ime");
      String prezime = matcher.group("prezime");
      int oznaka = Integer.parseInt(matcher.group("oznaka"));

      return new KomandaUPTAR(ime, prezime, oznaka);
    }

    private IKomanda parsirajZaAranzman(String args) {

      var uzorak = new RegexKomandeGraditelj()
          .dodajBroj("oznaka")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        return null;
      }

      int oznaka = Integer.parseInt(matcher.group("oznaka"));
      return new KomandaUPTAR(oznaka);
    }

  }

}
