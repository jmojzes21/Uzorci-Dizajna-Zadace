package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    datumVrijemeFormatorIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
  }

  public String formatirajDatum(LocalDate datum) {
    if (datum == null) {return "";}
    return datum.format(datumFormatorIspis);
  }

  public String formatirajVrijeme(LocalTime vrijeme) {
    if (vrijeme == null) {return "";}
    return vrijeme.format(vrijemeFormatorIspis);
  }

  public String formatirajDatumVrijeme(LocalDateTime datumVrijeme) {
    if (datumVrijeme == null) {return "";}
    return datumVrijeme.format(datumVrijemeFormatorIspis);
  }

  public LocalDate parsirajDatum(String datum) {
    return LocalDate.parse(datum, datumFormator);
  }

  public LocalTime parsirajVrijeme(String vrijeme) {
    return LocalTime.parse(vrijeme, vrijemeFormator);
  }

  public LocalDateTime parsirajDatumVrijeme(String datumVrijeme) {
    return LocalDateTime.parse(datumVrijeme, datumVrijemeFormator);
  }

  public LocalDateTime parsirajDatumVrijeme(String datumTekst, String vrijemeTekst) {
    var datum = parsirajDatum(datumTekst);
    var vrijeme = parsirajVrijeme(vrijemeTekst);
    return LocalDateTime.of(datum, vrijeme);
  }

}
