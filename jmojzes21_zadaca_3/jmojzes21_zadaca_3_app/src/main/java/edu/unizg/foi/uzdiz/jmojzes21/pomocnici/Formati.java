package edu.unizg.foi.uzdiz.jmojzes21.pomocnici;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Omogućuje formatiranje datuma zadanog formata.
 */
public class Formati {

  private static Formati formati;

  public static Formati dajInstancu() {
    if (formati == null) {
      formati = new Formati();
    }
    return formati;
  }

  private final DateTimeFormatter datumFormator;
  private final DateTimeFormatter vrijemeFormator;
  private final DateTimeFormatter datumVrijemeFormator;

  private final DateTimeFormatter datumFormatorIspis;
  private final DateTimeFormatter vrijemeFormatorIspis;
  private final DateTimeFormatter vrijemeSekundeFormatorIspis;
  private final DateTimeFormatter datumVrijemeFormatorIspis;
  private final DateTimeFormatter datumVrijemeSekundeFormatorIspis;

  private final DecimalFormat decimalFormat;

  private Formati() {
    datumFormator = DateTimeFormatter.ofPattern("d.M.yyyy[.]");
    vrijemeFormator = DateTimeFormatter.ofPattern("H:m[:s]");
    datumVrijemeFormator = DateTimeFormatter.ofPattern("d.M.yyyy[.] H:m[:s]");

    datumFormatorIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    vrijemeFormatorIspis = DateTimeFormatter.ofPattern("HH:mm");
    vrijemeSekundeFormatorIspis = DateTimeFormatter.ofPattern("HH:mm:ss");
    datumVrijemeFormatorIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
    datumVrijemeSekundeFormatorIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");

    var symbols = DecimalFormatSymbols.getInstance();
    symbols.setDecimalSeparator(',');
    symbols.setGroupingSeparator('.');
    decimalFormat = new DecimalFormat("#,##0.00", symbols);
  }

  /**
   * Formatiraj datum.
   *
   * @param datum datum
   * @return datum tekst ili prazno ako je null
   */
  public String formatiraj(LocalDate datum) {
    if (datum == null) {return "";}
    return datum.format(datumFormatorIspis);
  }

  /**
   * Formatiraj vrijeme, ne prikaži sekunde.
   *
   * @param vrijeme vrijeme
   * @return vrijeme tekst ili prazno ako je null
   */
  public String formatiraj(LocalTime vrijeme) {
    return formatiraj(vrijeme, false);
  }

  /**
   * Formatiraj vrijeme.
   *
   * @param vrijeme vrijeme
   * @param sekunde prikaži sekunde
   * @return vrijeme tekst ili prazno ako je null
   */
  public String formatiraj(LocalTime vrijeme, boolean sekunde) {
    if (vrijeme == null) {return "";}
    var formator = sekunde ? vrijemeSekundeFormatorIspis : vrijemeFormatorIspis;
    return vrijeme.format(formator);
  }

  /**
   * Formatiraj datum i vrijeme, prikaži sekunde.
   *
   * @param datumVrijeme datum i vrijeme
   * @return datum i vrijeme tekst ili prazno ako je null
   */
  public String formatiraj(LocalDateTime datumVrijeme) {
    return formatiraj(datumVrijeme, true);
  }

  /**
   * Formatiraj datum i vrijeme.
   *
   * @param datumVrijeme datum i vrijeme
   * @param sekunde      prikaži sekunde
   * @return datum i vrijeme tekst ili prazno ako je null
   */
  public String formatiraj(LocalDateTime datumVrijeme, boolean sekunde) {
    if (datumVrijeme == null) {return "";}
    var formator = sekunde ? datumVrijemeSekundeFormatorIspis : datumVrijemeFormatorIspis;
    return datumVrijeme.format(formator);
  }

  /**
   * Formatiraj decimalni broj.
   *
   * @param broj decimalni broj
   * @return formatirani decimalni broj
   */
  public String formatiraj(double broj) {
    return decimalFormat.format(broj);
  }

  /**
   * Parsiraj datum.
   *
   * @param datum datum teskt
   * @return datum
   * @throws Exception greška u formatu
   */
  public LocalDate parsirajDatum(String datum) throws Exception {
    try {
      return LocalDate.parse(datum, datumFormator);
    } catch (DateTimeParseException e) {
      throw new Exception(
          String.format("Nije moguće parsirati datum %s! Točan format: dd.MM.yyyy.", datum));
    }
  }

  /**
   * Parsiraj vrijeme.
   *
   * @param vrijeme vrijeme tekst
   * @return vrijeme
   * @throws Exception greška u formatu
   */
  public LocalTime parsirajVrijeme(String vrijeme) throws Exception {
    try {
      return LocalTime.parse(vrijeme, vrijemeFormator);
    } catch (DateTimeParseException e) {
      throw new Exception(
          String.format("Nije moguće parsirati vrijeme %s! Točan format: HH:mm", vrijeme));
    }
  }

  /**
   * Parsiraj datum i vrijeme
   *
   * @param datumVrijeme datum i vrijeme tekst
   * @return datum i vrijeme
   * @throws Exception greška u formatu
   */
  public LocalDateTime parsirajDatumVrijeme(String datumVrijeme) throws Exception {
    try {
      return LocalDateTime.parse(datumVrijeme, datumVrijemeFormator);
    } catch (DateTimeParseException e) {
      throw new Exception(
          String.format("Nije moguće parsirati %s! Točan format: dd.MM.yyyy. HH:mm", datumVrijeme));
    }
  }

  /**
   * Parsiraj datum i vrijeme
   *
   * @param datumTekst   datum tekst
   * @param vrijemeTekst vrijeme tekst
   * @return datum i vrijeme
   * @throws Exception greška u formatu
   */
  public LocalDateTime parsirajDatumVrijeme(String datumTekst, String vrijemeTekst)
      throws Exception {
    var datum = parsirajDatum(datumTekst);
    var vrijeme = parsirajVrijeme(vrijemeTekst);
    return LocalDateTime.of(datum, vrijeme);
  }

}
