package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FormatDatuma {

  private static FormatDatuma formatDatuma;

  public static FormatDatuma dajInstancu() {
    if (formatDatuma == null) {
      formatDatuma = new FormatDatuma();
    }
    return formatDatuma;
  }

  private final DateTimeFormatter datumFormator;
  private final DateTimeFormatter vrijemeFormator;
  private final DateTimeFormatter datumVrijemeFormator;

  private final DateTimeFormatter datumFormatorIspis;
  private final DateTimeFormatter vrijemeFormatorIspis;
  private final DateTimeFormatter datumVrijemeFormatorIspis;

  private FormatDatuma() {
    datumFormator = DateTimeFormatter.ofPattern("d.M.yyyy[.]");
    vrijemeFormator = DateTimeFormatter.ofPattern("H:m[:s]");
    datumVrijemeFormator = DateTimeFormatter.ofPattern("d.M.yyyy[.] H:m[:s]");

    datumFormatorIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    vrijemeFormatorIspis = DateTimeFormatter.ofPattern("HH:mm");
    datumVrijemeFormatorIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
  }

  public String formatiraj(LocalDate datum) {
    if (datum == null) {return "";}
    return datum.format(datumFormatorIspis);
  }

  public String formatiraj(LocalTime vrijeme) {
    if (vrijeme == null) {return "";}
    return vrijeme.format(vrijemeFormatorIspis);
  }

  public String formatiraj(LocalDateTime datumVrijeme) {
    if (datumVrijeme == null) {return "";}
    return datumVrijeme.format(datumVrijemeFormatorIspis);
  }

  public LocalDate parsirajDatum(String datum) throws Exception {
    try {
      return LocalDate.parse(datum, datumFormator);
    } catch (DateTimeParseException e) {
      throw new Exception(
          String.format("Nije moguće parsirati datum %s! Točan format: dd.MM.yyyy.", datum));
    }
  }

  public LocalTime parsirajVrijeme(String vrijeme) throws Exception {
    try {
      return LocalTime.parse(vrijeme, vrijemeFormator);
    } catch (DateTimeParseException e) {
      throw new Exception(
          String.format("Nije moguće parsirati vrijeme %s! Točan format: HH:mm", vrijeme));
    }
  }

  public LocalDateTime parsirajDatumVrijeme(String datumVrijeme) throws Exception {
    try {
      return LocalDateTime.parse(datumVrijeme, datumVrijemeFormator);
    } catch (DateTimeParseException e) {
      throw new Exception(
          String.format("Nije moguće parsirati %s! Točan format: dd.MM.yyyy. HH:mm", datumVrijeme));
    }
  }

  public LocalDateTime parsirajDatumVrijeme(String datumTekst, String vrijemeTekst)
      throws Exception {
    var datum = parsirajDatum(datumTekst);
    var vrijeme = parsirajVrijeme(vrijemeTekst);
    return LocalDateTime.of(datum, vrijeme);
  }

}
