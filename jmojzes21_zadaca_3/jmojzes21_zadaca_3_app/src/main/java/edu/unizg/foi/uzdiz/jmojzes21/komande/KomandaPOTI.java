package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaPOTI {

  public KomandaPOTI() {}

  public void obradiKomandu(String args) throws Exception {

    var uzorak = new RegexKomandeGraditelj()
        .dodajTekst("naziv")
        .dodajTekst("vrijednost")
        .dajUzorak();

    var matcher = uzorak.matcher(args);
    if (!matcher.matches()) {
      String opis = "POTI naziv vrijednost";
      throw new NeispravnaKomandaGreska(opis);
    }

    var postavke = PostavkeSustava.dajInstancu();

    String naziv = matcher.group("naziv");
    String vrijednostTekst = matcher.group("vrijednost");

    switch (naziv) {
      case "PT": {
        postavke.postaviPrelamanjeTeksta(dajBooleanVrijednost(vrijednostTekst));
        break;
      }
      case "DC": {
        postavke.postaviIspisDodatnihCrta(dajBooleanVrijednost(vrijednostTekst));
        break;
      }
      default: {
        String opis = String.format("Nepoznata postavka tabličnog ispisa %s!", naziv);
        throw new Exception(opis);
      }
    }

    System.out.println("Postavka uspješno postavljena.");

  }

  private boolean dajBooleanVrijednost(String vrijednost) throws Exception {
    return switch (vrijednost) {
      case "0" -> false;
      case "1" -> true;
      default -> throw new Exception(String.format("Vrijednost %s nije valjana!", vrijednost));
    };
  }

}
