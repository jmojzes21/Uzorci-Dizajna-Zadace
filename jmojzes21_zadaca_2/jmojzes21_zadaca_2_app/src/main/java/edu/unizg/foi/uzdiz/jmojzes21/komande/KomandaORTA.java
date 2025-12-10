package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.FormatDatuma;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaORTA {

  private final TuristickaAgencija agencija;

  public KomandaORTA(TuristickaAgencija agencija) {
    this.agencija = agencija;
  }

  public void obradiKomanduOtkaziRezervaciju(String komanda) throws Exception {

    FormatDatuma formatDatuma = FormatDatuma.dajInstancu();

    var uzorak = new RegexKomandeGraditelj("ORTA")
        .dodajTekst("ime")
        .dodajTekst("prezime")
        .dodajBroj("oznaka")
        .dajUzorak();

    var matcher = uzorak.matcher(komanda);
    if (!matcher.matches()) {
      String opis = "ORTA ime prezime oznaka";
      throw new NeispravnaKomandaGreska(opis);
    }

    String ime = matcher.group("ime");
    String prezime = matcher.group("prezime");
    int oznaka = Integer.parseInt(matcher.group("oznaka"));

    try {
      agencija.otkaziRezervaciju(ime, prezime, oznaka);
      System.out.println("Rezervacija je uspješno otkazana.");
    } catch (Exception e) {
      System.out.println("Otkazivanje rezervacije nije uspjelo. " + e.getMessage());
    }

  }

}
