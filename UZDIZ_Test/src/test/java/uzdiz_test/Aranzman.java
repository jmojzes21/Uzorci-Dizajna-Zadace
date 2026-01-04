package uzdiz_test;

import java.util.List;

public class Aranzman extends MultiValues {

  public static Aranzman uPripremi = new Aranzman(List.of("u pripremi"));
  public static Aranzman aktivan = new Aranzman(List.of("aktivan"));
  public static Aranzman popunjen = new Aranzman(List.of("popunjen"));
  public static Aranzman otkazan = new Aranzman(List.of("otkazan"));

  public Aranzman(List<String> nazivi) {
    super(nazivi);
  }

}
