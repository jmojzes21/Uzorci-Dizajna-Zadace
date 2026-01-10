package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaPOTI implements IKomanda {

  private final String naziv;
  private final String vrijednost;

  public KomandaPOTI(String naziv, String vrijednost) {
    this.naziv = naziv;
    this.vrijednost = vrijednost;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    var postavke = PostavkeSustava.dajInstancu();

    switch (naziv) {
      case "PT": {
        postavke.postaviPrelamanjeTeksta(dajBooleanVrijednost(vrijednost));
        break;
      }
      case "DC": {
        postavke.postaviIspisDodatnihCrta(dajBooleanVrijednost(vrijednost));
        break;
      }
      default: {
        String opis = String.format("Nepoznata postavka tabličnog ispisa %s!", naziv);
        throw new RuntimeException(opis);
      }
    }

    System.out.println("Postavka uspješno postavljena.");

  }

  private boolean dajBooleanVrijednost(String vrijednost) {
    return switch (vrijednost) {
      case "0" -> false;
      case "1" -> true;
      default -> throw new RuntimeException(String.format("Vrijednost %s nije valjana!", vrijednost));
    };
  }


  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var uzorak = new RegexKomandeGraditelj()
          .dodajTekst("naziv")
          .dodajTekst("vrijednost")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "POTI naziv vrijednost";
        throw new NeispravnaKomandaGreska(opis);
      }

      String naziv = matcher.group("naziv");
      String vrijednost = matcher.group("vrijednost");

      return new KomandaPOTI(naziv, vrijednost);
    }
  }

}
