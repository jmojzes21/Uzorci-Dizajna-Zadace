package edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import java.time.LocalDateTime;

public class RezervacijaAktivna extends RezervacijaStanje {

  @Override
  public void zaprimi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaPrimljena());
  }

  @Override
  public void staviNaCekanje(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaNaCekanju());
  }

  @Override
  public void odgodi(Rezervacija rezervacija) {
    rezervacija.postaviStanje(new RezervacijaOdgodjena());
  }

  @Override
  public void otkazi(Rezervacija rezervacija) {
    rezervacija.setVrijemeOtkaza(LocalDateTime.now());
    rezervacija.postaviStanje(new RezervacijaOtkazana());
  }

  @Override
  public void kadaRezervacijaPostalaAktivna(Rezervacija rezervacija, Rezervacija aktivirana) {
    if (rezervacija.korisnik().equals(aktivirana.korisnik())) {
      var agencija = rezervacija.dajAranzman().dajAgenciju();
      var ur = agencija.upravljanjeRezervacijama();
      ur.kadaRezervacijaKorisnikaPostalaAktivna(agencija, rezervacija, aktivirana);
    }
  }

  @Override
  public StanjeId dajId() {
    return StanjeId.aktivna;
  }

  @Override
  public String dajNaziv() {
    return "Aktivna";
  }

}
