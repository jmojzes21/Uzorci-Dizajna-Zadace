package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;

import java.util.regex.Pattern;

/**
 * Omogućuje izgradnju regularno izraza koji se koristi za provjeru valjanosti komandi i za
 * parsiranje argumenata komande.
 */
public class RegexKomandeGraditelj {

  private final StringBuilder regex;
  private final String razmakArgumenata = "\\s+";

  /**
   *
   * @param nazivKomande naziv komande
   */
  public RegexKomandeGraditelj(String nazivKomande) {
    regex = new StringBuilder();
    regex.append(String.format("(%s)", nazivKomande));
  }

  /**
   * Dodaj sljedeći argument koji je tipa broj.
   *
   * @param naziv naziv matcher grupe
   * @return graditelj
   */
  public RegexKomandeGraditelj dodajBroj(String naziv) {
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\d+)", naziv));
    return this;
  }

  /**
   * Dodaj sljedeći argument koji je tipa tekst.
   *
   * @param naziv naziv matcher grupe
   * @return graditelj
   */
  public RegexKomandeGraditelj dodajTekst(String naziv) {
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\w+)", naziv));
    return this;
  }

  /**
   * Dodaj sljedeći argument koji je tipa tekst koji je opcionalan.
   *
   * @param naziv naziv matcher grupe
   * @return graditelj
   */
  public RegexKomandeGraditelj dodajTekstOpcionalno(String naziv) {
    regex.append("(");
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\w+)", naziv));
    regex.append(")?");
    return this;
  }

  /**
   * Dodaj sljedeći argument koji je tipa datum.
   *
   * @param naziv naziv matcher grupe
   * @return graditelj
   */
  public RegexKomandeGraditelj dodajDatum(String naziv) {
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\d+\\.\\d+\\.\\d+\\.?)", naziv));
    return this;
  }

  /**
   * Dodaj sljedeći argument koji je tipa vrijeme.
   *
   * @param naziv naziv matcher grupe
   * @return graditelj
   */
  public RegexKomandeGraditelj dodajVrijeme(String naziv) {
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\d+\\:\\d+(\\:\\d+)?)", naziv));
    return this;
  }

  /**
   * Kreiraj regularni izraz.
   *
   * @return regularni izraz
   */
  public Pattern dajUzorak() {
    return Pattern.compile(regex.toString(), Pattern.UNICODE_CHARACTER_CLASS);
  }

}
