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

  private FormatDatuma() {
    datumFormator = DateTimeFormatter.ofPattern("d.M.yyyy[.]");
    vrijemeFormator = DateTimeFormatter.ofPattern("H:m[:s]");
    datumVrijemeFormator = DateTimeFormatter.ofPattern("d.M.yyyy[.] H:m[:s]");
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

}
