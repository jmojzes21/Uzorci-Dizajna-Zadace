package edu.unizg.foi.uzdiz.jmojzes21.lib.ucitavac;

import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvFormatGreska;
import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvRedak;
import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvStupacIterator;

public class UcitavacRezervacija extends UcitavacCsvPodataka {

  @Override
  protected void provjeriInfoRedak(CsvRedak infoRedak) throws CsvFormatGreska {
    var stupci = new String[]{"Ime", "Prezime", "Oznaka aranžmana", "Datum i vrijeme"};
    provjeriInfoRedakStupce(infoRedak, stupci);
  }

  @Override
  protected void provjeriRedak(CsvRedak redak) throws CsvFormatGreska {

    if (redak.brojElemenata() != 4) {
      String opis = String.format("Csv redak za rezervacije treba imati 4 stupca, trenutno: %d!",
          redak.brojElemenata());
      throw new CsvFormatGreska(opis, redak);
    }

    CsvStupacIterator stupci = redak.dajIteratorStupaca();

    stupacPostoji(stupci.sljedeci(), redak, "Ime");
    stupacPostoji(stupci.sljedeci(), redak, "Prezime");
    stupacJeInt(stupci.sljedeci(), true, redak, "Oznaka aranžmana");
    stupacJeDatumVrijeme(stupci.sljedeci(), true, redak, "Datum i vrijeme");

  }

}
