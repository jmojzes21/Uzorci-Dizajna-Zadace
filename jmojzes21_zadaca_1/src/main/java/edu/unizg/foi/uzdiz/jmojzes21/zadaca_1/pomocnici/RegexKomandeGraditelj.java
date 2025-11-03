package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici;

import java.util.regex.Pattern;

public class RegexKomandeGraditelj {

  private StringBuilder regex;
  private final String razmakArgumenata = "\\s+";

  public RegexKomandeGraditelj(String nazivKomande) {
    regex = new StringBuilder();
    regex.append(String.format("(%s)", nazivKomande));
  }

  public RegexKomandeGraditelj dodajBroj(String naziv) {
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\d+)", naziv));
    return this;
  }

  public RegexKomandeGraditelj dodajTekst(String naziv) {
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\w+)", naziv));
    return this;
  }

  public RegexKomandeGraditelj dodajTekstOpcionalno(String naziv) {
    regex.append("(");
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\w+)", naziv));
    regex.append(")?");
    return this;
  }

  public RegexKomandeGraditelj dodajDatum(String naziv) {
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\d+\\.\\d+\\.\\d+\\.?)", naziv));
    return this;
  }

  public RegexKomandeGraditelj dodajVrijeme(String naziv) {
    regex.append(razmakArgumenata);
    regex.append(String.format("(?<%s>\\d+\\:\\d+(\\:\\d+)?)", naziv));
    return this;
  }

  public Pattern dajUzorak() {
    return Pattern.compile(regex.toString(), Pattern.UNICODE_CHARACTER_CLASS);
  }

}
