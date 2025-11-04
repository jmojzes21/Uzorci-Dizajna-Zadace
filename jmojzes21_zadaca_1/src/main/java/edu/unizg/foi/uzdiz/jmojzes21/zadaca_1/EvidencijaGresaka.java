package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

/**
 * Omogućuje evidentiranje grešaka.
 */
public class EvidencijaGresaka {

  private static EvidencijaGresaka evidencijaGresaka;

  public static EvidencijaGresaka dajInstancu() {
    if (evidencijaGresaka == null) {
      evidencijaGresaka = new EvidencijaGresaka();
    }
    return evidencijaGresaka;
  }

  /**
   * Trenutni broj grešaka.
   */
  private int brojGresaka = 0;

  private EvidencijaGresaka() {}

  /**
   * Evidentiraj grešku.
   *
   * @param e greška
   */
  public void evidentiraj(Exception e) {
    evidentiraj(e.getMessage());
  }

  /**
   * Evidentiraj grešku.
   *
   * @param poruka poruka
   */
  public void evidentiraj(String poruka) {
    brojGresaka++;

    System.out.printf("[Greška %d] %s\n", brojGresaka, poruka);
  }

}
