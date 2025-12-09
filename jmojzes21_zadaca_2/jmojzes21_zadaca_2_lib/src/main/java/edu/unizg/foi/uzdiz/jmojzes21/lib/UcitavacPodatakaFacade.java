package edu.unizg.foi.uzdiz.jmojzes21.lib;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UcitavacPodatakaFacade {

  private static final char ZNAK_ODVAJANJA = ',';

  public List<List<String>> ucitajAranzmane(Path putanja) throws IOException {

    String csv = dajSadrzajDatoteke(putanja);

    var csvCitac = new CsvCitac(ZNAK_ODVAJANJA);
    csvCitac.ucitajCsv(csv);

    CsvRedakIterator redci = csvCitac.csvRedci();

    List<List<String>> rezultat = new ArrayList<>();

    if (!redci.imaSljedeci()) {
      String opis = "Ne postoji informativni redak za aranžmane!";
      EvidencijaGresaka.dajInstancu().evidentiraj(opis);
      return rezultat;
    }

    try {
      CsvRedak infoRedak = redci.sljedeci();
      provjeriCsvInfoRedak(infoRedak,
          new String[]{"Oznaka", "Naziv", "Program", "Početni datum", "Završni datum", "Vrijeme kretanja",
              "Vrijeme povratka", "Cijena", "Min broj putnika", "Maks broj putnika", "Broj noćenja",
              "Doplata za jednokrevetnu sobu", "Prijevoz", "Broj doručka", "Broj ručkova", "Broj večera"});
    } catch (CsvFormatGreska e) {
      EvidencijaGresaka.dajInstancu().evidentiraj(e);
    }

    while (redci.imaSljedeci()) {
      CsvRedak redak = redci.sljedeci();

      try {
        provjeriCsvRedakAranzmana(redak);
        rezultat.add(redak.elementi());
      } catch (CsvFormatGreska e) {
        EvidencijaGresaka.dajInstancu().evidentiraj(e);
      }
    }

    return rezultat;
  }

  public List<List<String>> ucitajRezervacije(Path putanja) throws IOException {

    String csv = dajSadrzajDatoteke(putanja);

    var csvCitac = new CsvCitac(ZNAK_ODVAJANJA);
    csvCitac.ucitajCsv(csv);

    CsvRedakIterator redci = csvCitac.csvRedci();

    List<List<String>> rezultat = new ArrayList<>();

    if (!redci.imaSljedeci()) {
      String opis = "Ne postoji informativni redak za rezervacije!";
      EvidencijaGresaka.dajInstancu().evidentiraj(opis);
      return rezultat;
    }

    try {
      CsvRedak infoRedak = redci.sljedeci();
      provjeriCsvInfoRedak(infoRedak, new String[]{"Ime", "Prezime", "Oznaka aranžmana", "Datum i vrijeme"});
    } catch (CsvFormatGreska e) {
      EvidencijaGresaka.dajInstancu().evidentiraj(e);
    }

    while (redci.imaSljedeci()) {
      CsvRedak redak = redci.sljedeci();

      try {
        provjeriCsvRedakRezervacije(redak);
        rezultat.add(redak.elementi());
      } catch (CsvFormatGreska e) {
        EvidencijaGresaka.dajInstancu().evidentiraj(e);
      }
    }

    return rezultat;
  }

  private void provjeriCsvRedakAranzmana(CsvRedak redak) throws CsvFormatGreska {

    if (redak.brojElemenata() != 16) {
      String opis = String.format("Csv redak za aranžmane treba imati 16 stupaca, trenutno: %d!",
          redak.brojElemenata());
      throw new CsvFormatGreska(opis, redak);
    }

    CsvStupacIterator stupci = new CsvStupacIterator(redak.elementi());

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

  private void provjeriCsvRedakRezervacije(CsvRedak redak) throws CsvFormatGreska {

    if (redak.brojElemenata() != 4) {
      String opis = String.format("Csv redak za rezervacije treba imati 4 stupca, trenutno: %d!",
          redak.brojElemenata());
      throw new CsvFormatGreska(opis, redak);
    }

    CsvStupacIterator stupci = new CsvStupacIterator(redak.elementi());

    stupacPostoji(stupci.sljedeci(), redak, "Ime");
    stupacPostoji(stupci.sljedeci(), redak, "Prezime");
    stupacJeInt(stupci.sljedeci(), true, redak, "Oznaka aranžmana");
    stupacJeDatumVrijeme(stupci.sljedeci(), true, redak, "Datum i vrijeme");

  }

  public boolean jeInt(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "-?\\d+";
    return Pattern.matches(regex, element);
  }

  public boolean jeFloat(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "-?\\d+(\\.\\d+)?";
    return Pattern.matches(regex, element);
  }

  public boolean jeDatum(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "(\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.?)";
    return Pattern.matches(regex, element);
  }

  public boolean jeVrijeme(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "(\\d{1,2}:\\d{1,2}(:\\d{1,2})?)";
    return Pattern.matches(regex, element);
  }

  public boolean jeDatumVrijeme(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "(\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.?)\\s+(\\d{1,2}:\\d{1,2}(:\\d{1,2})?)";
    return Pattern.matches(regex, element);
  }

  public void stupacPostoji(String stupac, CsvRedak redak, String naziv) throws CsvFormatGreska {
    if (stupac == null) {
      String opis = String.format("Stupac %s mora postojati!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  public void stupacJeInt(String stupac, boolean moraPostojati, CsvRedak redak, String naziv) throws CsvFormatGreska {
    if (!jeInt(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti broj!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  public void stupacJeFloat(String stupac, boolean moraPostojati, CsvRedak redak, String naziv) throws CsvFormatGreska {
    if (!jeFloat(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti decimalni broj!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  public void stupacJeDatum(String stupac, boolean moraPostojati, CsvRedak redak, String naziv) throws CsvFormatGreska {
    if (!jeDatum(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti datum!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  public void stupacJeVrijeme(String stupac, boolean moraPostojati, CsvRedak redak, String naziv)
      throws CsvFormatGreska {
    if (!jeVrijeme(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti vrijeme!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  public void stupacJeDatumVrijeme(String stupac, boolean moraPostojati, CsvRedak redak, String naziv)
      throws CsvFormatGreska {
    if (!jeDatumVrijeme(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti datum vrijeme!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  private void provjeriCsvInfoRedak(CsvRedak infoRedak, String[] stupci) throws CsvFormatGreska {

    if (!infoRedak.elementi().getFirst().startsWith(stupci[0])) {
      String opis = "Ne postoji informativni redak za aranžmane!";
      throw new CsvFormatGreska(opis, infoRedak);
    }

    if (infoRedak.brojElemenata() != stupci.length) {
      String opis = String.format(
          "Informacijski redak ne sadrži točan broj stupaca! Očekivano: %d, stvarno: %d",
          stupci.length, infoRedak.brojElemenata());
      throw new CsvFormatGreska(opis, infoRedak);
    }

    for (int i = 0; i < stupci.length; i++) {
      String stupac = stupci[i].trim();
      String infoStupac = infoRedak.elementi().get(i);
      if (infoStupac == null) {infoStupac = "";}

      if (!stupac.equalsIgnoreCase(infoStupac)) {
        String opis = String.format(
            "Informacijski redak ne sadrži točan stupac! Očekivano: %s, stvarno: %s", stupac,
            infoStupac);
        throw new CsvFormatGreska(opis, infoRedak);
      }
    }

  }

  private String dajSadrzajDatoteke(Path putanja) throws IOException {
    if (Files.notExists(putanja)) {
      String opis = String.format("Datoteka ne postoji! Putanja: %s\n", putanja);
      throw new IOException(opis);
    }

    return Files.readString(putanja, StandardCharsets.UTF_8);
  }

}
