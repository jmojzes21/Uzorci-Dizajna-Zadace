package edu.unizg.foi.uzdiz.jmojzes21.komande.glavno;

import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaBP;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaDRTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaIP;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaIRO;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaIRTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaITAK;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaITAP;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaITAS;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaORTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaOTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaPOTI;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaPPTAR;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaPSTAR;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaPTAR;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaUP;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaUPTAR;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaVSTAR;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NepoznataKomanda;
import java.util.HashMap;
import java.util.Map;

public class ObradaKomandi {

  private final Map<String, KomandaKreator> kreatori;

  public ObradaKomandi() {
    kreatori = new HashMap<>();
    kreatori.put("ITAK", new KomandaITAK.Kreator());
    kreatori.put("ITAP", new KomandaITAP.Kreator());
    kreatori.put("IRTA", new KomandaIRTA.Kreator());
    kreatori.put("IRO", new KomandaIRO.Kreator());
    kreatori.put("DRTA", new KomandaDRTA.Kreator());
    kreatori.put("ORTA", new KomandaORTA.Kreator());
    kreatori.put("OTA", new KomandaOTA.Kreator());
    kreatori.put("IP", new KomandaIP.Kreator());
    kreatori.put("BP", new KomandaBP.Kreator());
    kreatori.put("UP", new KomandaUP.Kreator());
    kreatori.put("ITAS", new KomandaITAS.Kreator());
    kreatori.put("POTI", new KomandaPOTI.Kreator());
    kreatori.put("PPTAR", new KomandaPPTAR.Kreator());
    kreatori.put("PTAR", new KomandaPTAR.Kreator());
    kreatori.put("UPTAR", new KomandaUPTAR.Kreator());
    kreatori.put("PSTAR", new KomandaPSTAR.Kreator());
    kreatori.put("VSTAR", new KomandaVSTAR.Kreator());
  }

  public KomandaKreator dajKreatora(String naziv) {
    var kreator = kreatori.get(naziv);
    if (kreator == null) {
      throw new NepoznataKomanda();
    }
    return kreator;
  }

  public void izvrsiKomandu(IKomanda komanda, TuristickaAgencija agencija) {
    komanda.izvrsi(agencija);
  }

}
