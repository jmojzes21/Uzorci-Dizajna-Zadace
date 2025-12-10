package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.FormatDatuma;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import java.time.LocalDateTime;

public class KomandaDRTA {

  private final TuristickaAgencija agencija;

  public KomandaDRTA(TuristickaAgencija agencija) {
    this.agencija = agencija;
  }

  public void obradiKomanduDodavanjeRezervacije(String args) throws Exception {

    FormatDatuma formatDatuma = FormatDatuma.dajInstancu();

    var uzorak = new RegexKomandeGraditelj()
        .dodajTekst("ime")
        .dodajTekst("prezime")
        .dodajBroj("oznaka")
        .dodajDatum("datum")
        .dodajVrijeme("vrijeme")
        .dajUzorak();

    var matcher = uzorak.matcher(args);
    if (!matcher.matches()) {
      String opis = "DRTA ime prezime oznaka datum vrijeme";
      throw new NeispravnaKomandaGreska(opis);
    }

    String ime = matcher.group("ime");
    String prezime = matcher.group("prezime");
    int oznaka = Integer.parseInt(matcher.group("oznaka"));
    String datum = matcher.group("datum");
    String vrijeme = matcher.group("vrijeme");
    LocalDateTime vrijemePrijema = formatDatuma.parsirajDatumVrijeme(datum, vrijeme);

    var korisnik = new Korisnik(ime, prezime);
    var rezervacija = new Rezervacija(korisnik, oznaka, vrijemePrijema);

    try {
      agencija.zaprimiRezervaciju(rezervacija);
      System.out.println("Rezervacija je uspješno zaprimljena.");
    } catch (Exception e) {
      System.out.println("Dodavanje rezervacije nije uspjelo. " + e.getMessage());
    }

  }

}
