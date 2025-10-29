package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci;

import java.time.LocalDateTime;

public class OtkazanaRezervacija extends Rezervacija {

  private LocalDateTime datumVrijemeOtkaza;

  public OtkazanaRezervacija(Rezervacija r) {
    super(r);
  }

  public LocalDateTime datumVrijemeOtkaza() {return datumVrijemeOtkaza;}

  public void setDatumVrijemeOtkaza(LocalDateTime datumVrijemeOtkaza) {
    this.datumVrijemeOtkaza = datumVrijemeOtkaza;
  }

  @Override
  public String vrsta() {
    return "Otkazana";
  }

}
