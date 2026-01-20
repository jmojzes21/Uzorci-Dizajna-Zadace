package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.IKomanda;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.KomandaKreator;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import java.time.LocalDateTime;

public class KomandaDRTA implements IKomanda {

  private final String ime;
  private final String prezime;
  private final int oznaka;
  private final LocalDateTime vrijemePrijema;

  public KomandaDRTA(String ime, String prezime, int oznaka, LocalDateTime vrijemePrijema) {
    this.ime = ime;
    this.prezime = prezime;
    this.oznaka = oznaka;
    this.vrijemePrijema = vrijemePrijema;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    var korisnik = new Korisnik(ime, prezime);
    var rezervacija = new Rezervacija(korisnik, oznaka, vrijemePrijema);

    try {
      agencija.zaprimiRezervaciju(rezervacija);
      System.out.println("Rezervacija je uspješno zaprimljena.");
    } catch (Exception e) {
      System.out.println("Dodavanje rezervacije nije uspjelo. " + e.getMessage());
    }

  }


  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var f = Formati.dajInstancu();

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
      LocalDateTime vrijemePrijema = f.parsirajDatumVrijeme(datum, vrijeme);

      return new KomandaDRTA(ime, prezime, oznaka, vrijemePrijema);
    }
  }

}
