package edu.unizg.foi.uzdiz.jmojzes21.lib.ucitavac;

import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvFormatGreska;
import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvRedak;
import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvStupacIterator;

public class UcitavacAranzmana extends UcitavacCsvPodataka {

  @Override
  protected void provjeriInfoRedak(CsvRedak infoRedak) throws CsvFormatGreska {
    var stupci = new String[]{"Oznaka", "Naziv", "Program", "Početni datum", "Završni datum", "Vrijeme kretanja",
        "Vrijeme povratka", "Cijena", "Min broj putnika", "Maks broj putnika", "Broj noćenja",
        "Doplata za jednokrevetnu sobu", "Prijevoz", "Broj doručka", "Broj ručkova", "Broj večera"};
    provjeriInfoRedakStupce(infoRedak, stupci);
  }

  @Override
  protected void provjeriRedak(CsvRedak redak) throws CsvFormatGreska {
    if (redak.brojElemenata() < 16) {
      String opis = String.format("Csv redak za aranžmane treba imati 16 stupaca, trenutno: %d!",
          redak.brojElemenata());
      throw new CsvFormatGreska(opis, redak);
    }

    CsvStupacIterator stupci = redak.dajIteratorStupaca();

    stupacJeInt(stupci.sljedeci(), true, redak, "Oznaka");
    stupacPostoji(stupci.sljedeci(), redak, "Naziv");
    stupacPostoji(stupci.sljedeci(), redak, "Program");
    stupacJeDatum(stupci.sljedeci(), true, redak, "Početni datum");
    stupacJeDatum(stupci.sljedeci(), true, redak, "Završni datum");
    stupacJeVrijeme(stupci.sljedeci(), false, redak, "Vrijeme kretanja");
    stupacJeVrijeme(stupci.sljedeci(), false, redak, "Vrijeme povratka");
    stupacJeFloat(stupci.sljedeci(), true, redak, "Cijena");

    stupacJeInt(stupci.sljedeci(), true, redak, "Min broj putnika");
    stupacJeInt(stupci.sljedeci(), true, redak, "Maks broj putnika");
    stupacJeInt(stupci.sljedeci(), false, redak, "Broj noćenja");
    stupacJeFloat(stupci.sljedeci(), false, redak, "Doplata za jednokrevetnu sobu");
    stupci.sljedeci();
    stupacJeInt(stupci.sljedeci(), false, redak, "Broj doručka");
    stupacJeInt(stupci.sljedeci(), false, redak, "Broj ručkova");
    stupacJeInt(stupci.sljedeci(), false, redak, "Broj večera");

  }

}
