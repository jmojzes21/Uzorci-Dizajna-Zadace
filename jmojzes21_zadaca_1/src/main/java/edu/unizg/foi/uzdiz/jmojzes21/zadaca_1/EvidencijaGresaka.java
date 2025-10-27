package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

public class EvidencijaGresaka {

  private static EvidencijaGresaka evidencijaGresaka;

  public static EvidencijaGresaka dajInstancu() {
    if (evidencijaGresaka == null) {
      evidencijaGresaka = new EvidencijaGresaka();
    }
    return evidencijaGresaka;
  }

  private int brojGresaka = 0;

  private EvidencijaGresaka() {}

  public void evidentiraj(Exception e) {
    brojGresaka++;

    System.out.printf("Greška %d\n", brojGresaka);
    System.out.println(e.getMessage());
  }

}
