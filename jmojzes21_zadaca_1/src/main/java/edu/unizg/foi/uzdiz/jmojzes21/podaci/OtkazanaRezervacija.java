package edu.unizg.foi.uzdiz.jmojzes21.podaci;

import java.time.LocalDateTime;

public class OtkazanaRezervacija extends Rezervacija {

  private LocalDateTime datumVrijemeOtkaza;

  public OtkazanaRezervacija(Korisnik korisnik, int oznakaAranzmana, LocalDateTime datumVrijeme,
      LocalDateTime datumVrijemeOtkaza) {
    super(korisnik, oznakaAranzmana, datumVrijeme);
    this.datumVrijemeOtkaza = datumVrijemeOtkaza;
  }

  public OtkazanaRezervacija(Rezervacija r, LocalDateTime datumVrijemeOtkaza) {
    super(r);
    this.datumVrijemeOtkaza = datumVrijemeOtkaza;
  }

  @Override
  public String vrsta() {
    return "Otkazana";
  }

  public LocalDateTime datumVrijemeOtkaza() {return datumVrijemeOtkaza;}

  public void setDatumVrijemeOtkaza(LocalDateTime datumVrijemeOtkaza) {
    this.datumVrijemeOtkaza = datumVrijemeOtkaza;
  }

}
