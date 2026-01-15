package edu.unizg.foi.uzdiz.jmojzes21.komande;


import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NepoznataKomanda;

public abstract class KomandaKreator {

  public static IKomanda parsirajKomandu(String naziv, String args) throws Exception {
    KomandaKreator kreator = dajKreatoraKomande(naziv);
    return kreator.parsiraj(args);
  }

  private static KomandaKreator dajKreatoraKomande(String naziv) {
    return switch (naziv) {
      case "ITAK" -> new KomandaITAK.Kreator();
      case "ITAP" -> new KomandaITAP.Kreator();
      case "IRTA" -> new KomandaIRTA.Kreator();
      case "IRO" -> new KomandaIRO.Kreator();
      case "DRTA" -> new KomandaDRTA.Kreator();
      case "ORTA" -> new KomandaORTA.Kreator();
      case "OTA" -> new KomandaOTA.Kreator();
      case "IP" -> new KomandaIP.Kreator();
      case "BP" -> new KomandaBP.Kreator();
      case "UP" -> new KomandaUP.Kreator();
      case "ITAS" -> new KomandaITAS.Kreator();
      case "POTI" -> new KomandaPOTI.Kreator();
      case "PPTAR" -> new KomandaPPTAR.Kreator();
      default -> throw new NepoznataKomanda();
    };
  }

  public abstract IKomanda parsiraj(String args) throws Exception;

}
