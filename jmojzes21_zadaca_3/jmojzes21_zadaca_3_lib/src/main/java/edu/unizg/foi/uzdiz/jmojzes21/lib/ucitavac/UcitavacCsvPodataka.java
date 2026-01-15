package edu.unizg.foi.uzdiz.jmojzes21.lib.ucitavac;

import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvCitac;
import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvFormatGreska;
import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvRedak;
import edu.unizg.foi.uzdiz.jmojzes21.lib.csv.CsvRedakIterator;
import edu.unizg.foi.uzdiz.jmojzes21.lib.pomocnici.EvidencijaGresaka;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class UcitavacCsvPodataka {

  private static final char ZNAK_ODVAJANJA = ',';

  public List<List<String>> ucitaj(Path putanja) throws IOException {

    String csv = dajSadrzajDatoteke(putanja);

    var csvCitac = new CsvCitac(ZNAK_ODVAJANJA);
    csvCitac.ucitajCsv(csv);

    CsvRedakIterator redci = csvCitac.csvRedci();
    List<List<String>> rezultat = new ArrayList<>();

    if (!redci.imaSljedeci()) {
      String opis = "Ne postoji informativni redak!";
      EvidencijaGresaka.dajInstancu().evidentiraj(opis);
      return rezultat;
    }

    try {
      var infoRedak = redci.sljedeci();
      provjeriInfoRedak(infoRedak);
    } catch (Exception e) {
      redci.prvi();
      EvidencijaGresaka.dajInstancu().evidentiraj(e);
    }

    while (redci.imaSljedeci()) {
      var redak = redci.sljedeci();

      try {
        provjeriRedak(redak);

        List<String> elementi = new ArrayList<>();
        elementi.add(redak.linija());
        elementi.addAll(redak.elementi());
        rezultat.add(elementi);
      } catch (Exception e) {
        EvidencijaGresaka.dajInstancu().evidentiraj(e);
      }
    }

    return rezultat;
  }

  protected abstract void provjeriInfoRedak(CsvRedak infoRedak) throws CsvFormatGreska;

  protected abstract void provjeriRedak(CsvRedak redak) throws CsvFormatGreska;

  protected boolean jeInt(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "-?\\d+";
    return Pattern.matches(regex, element);
  }

  protected boolean jeFloat(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "-?\\d+(\\.\\d+)?";
    return Pattern.matches(regex, element);
  }

  protected boolean jeDatum(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "(\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.?)";
    return Pattern.matches(regex, element);
  }

  protected boolean jeVrijeme(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "(\\d{1,2}:\\d{1,2}(:\\d{1,2})?)";
    return Pattern.matches(regex, element);
  }

  protected boolean jeDatumVrijeme(String element, boolean moraPostojati) {
    if (element == null) {return !moraPostojati;}

    String regex = "(\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.?)\\s+(\\d{1,2}:\\d{1,2}(:\\d{1,2})?)";
    return Pattern.matches(regex, element);
  }

  protected void stupacPostoji(String stupac, CsvRedak redak, String naziv) throws CsvFormatGreska {
    if (stupac == null) {
      String opis = String.format("Stupac %s mora postojati!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  protected void stupacJeInt(String stupac, boolean moraPostojati, CsvRedak redak, String naziv)
      throws CsvFormatGreska {
    if (!jeInt(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti broj!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  protected void stupacJeFloat(String stupac, boolean moraPostojati, CsvRedak redak, String naziv)
      throws CsvFormatGreska {
    if (!jeFloat(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti decimalni broj!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  protected void stupacJeDatum(String stupac, boolean moraPostojati, CsvRedak redak, String naziv)
      throws CsvFormatGreska {
    if (!jeDatum(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti datum!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  protected void stupacJeVrijeme(String stupac, boolean moraPostojati, CsvRedak redak, String naziv)
      throws CsvFormatGreska {
    if (!jeVrijeme(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti vrijeme!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  protected void stupacJeDatumVrijeme(String stupac, boolean moraPostojati, CsvRedak redak, String naziv)
      throws CsvFormatGreska {
    if (!jeDatumVrijeme(stupac, moraPostojati)) {
      String opis = String.format("Stupac %s mora biti datum vrijeme!", naziv);
      throw new CsvFormatGreska(opis, redak);
    }
  }

  protected void provjeriInfoRedakStupce(CsvRedak infoRedak, String[] stupci) throws CsvFormatGreska {

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

  protected String dajSadrzajDatoteke(Path putanja) throws IOException {
    if (Files.notExists(putanja)) {
      String opis = String.format("Datoteka ne postoji! Putanja: %s", putanja);
      throw new IOException(opis);
    }

    return Files.readString(putanja, StandardCharsets.UTF_8);
  }

}
