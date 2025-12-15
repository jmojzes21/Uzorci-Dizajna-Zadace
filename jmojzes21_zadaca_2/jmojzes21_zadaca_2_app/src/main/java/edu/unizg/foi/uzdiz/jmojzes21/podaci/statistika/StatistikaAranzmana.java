package edu.unizg.foi.uzdiz.jmojzes21.podaci.statistika;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija.StanjeId;
import java.util.List;

public class StatistikaAranzmana {

  private int oznaka = 0;

  private int ukupanBrojRezervacija = 0;
  private int brojAktivnihRezervacija = 0;
  private int brojRezervacijaNaCekanju = 0;
  private int brojOdgodjenihRezervacija = 0;
  private int brojOtkazanihRezervacija = 0;

  private double ukupanPrihod = 0;

  public StatistikaAranzmana(Aranzman aranzman) {

    oznaka = aranzman.oznaka();

    List<Rezervacija> rezervacije = aranzman.rezervacije();
    ukupanBrojRezervacija = rezervacije.size();

    for (var r : rezervacije) {
      switch (r.idStanja()) {
        case StanjeId.aktivna:
          brojAktivnihRezervacija++;
          break;
        case StanjeId.naCekanju:
          brojRezervacijaNaCekanju++;
          break;
        case StanjeId.odgodjena:
          brojOdgodjenihRezervacija++;
          break;
        case StanjeId.otkazana:
          brojOtkazanihRezervacija++;
          break;
      }
    }

    ukupanPrihod = brojAktivnihRezervacija * aranzman.cijena();

  }

  public int oznaka() {
    return oznaka;
  }

  public int ukupanBrojRezervacija() {
    return ukupanBrojRezervacija;
  }

  public int brojAktivnihRezervacija() {
    return brojAktivnihRezervacija;
  }

  public int brojRezervacijaNaCekanju() {
    return brojRezervacijaNaCekanju;
  }

  public int brojOdgodjenihRezervacija() {
    return brojOdgodjenihRezervacija;
  }

  public int brojOtkazanihRezervacija() {
    return brojOtkazanihRezervacija;
  }

  public double ukupanPrihod() {
    return ukupanPrihod;
  }

}
