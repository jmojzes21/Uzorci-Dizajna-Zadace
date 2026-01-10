package edu.unizg.foi.uzdiz.jmojzes21.modeli.statistika;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import java.util.List;

public class StatistikaAranzmana {

  private final Aranzman aranzman;

  private int ukupanBrojRezervacija = 0;
  private int brojAktivnihRezervacija = 0;
  private int brojRezervacijaNaCekanju = 0;
  private int brojOdgodjenihRezervacija = 0;
  private int brojOtkazanihRezervacija = 0;

  private double ukupanPrihod = 0;

  public StatistikaAranzmana(Aranzman aranzman) {

    this.aranzman = aranzman;

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

  public Aranzman aranzman() {
    return this.aranzman;
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
