package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.FormatDatuma;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.tablicni_ispis.TablicniIspisGraditelj;
import java.util.List;

public class KomandaIRO {

  private final TuristickaAgencija agencija;

  public KomandaIRO(TuristickaAgencija agencija) {
    this.agencija = agencija;
  }

  public void obradiKomanduPregledRezervacijaKorisnika(String args) throws Exception {

    var uzorak = new RegexKomandeGraditelj()
        .dodajTekst("ime")
        .dodajTekst("prezime")
        .dajUzorak();

    var matcher = uzorak.matcher(args);
    if (!matcher.matches()) {
      String opis = "IRO ime prezime";
      throw new NeispravnaKomandaGreska(opis);
    }

    String ime = matcher.group("ime");
    String prezime = matcher.group("prezime");

    List<Rezervacija> rezervacije = agencija.dajRezervacijeKorisnika(ime, prezime);
    if (rezervacije.isEmpty()) {
      System.out.println("Korisnik nema rezervacija.");
      return;
    }

    prikaziRezervacijeKorisnika(rezervacije);
  }

  private void prikaziRezervacijeKorisnika(List<Rezervacija> rezervacije) {

    var formatDatuma = FormatDatuma.dajInstancu();

    var tablicniIspis = new TablicniIspisGraditelj<Rezervacija>()
        .dodajStupac("Datum i vrijeme", 24, e -> formatDatuma.formatiraj(e.vrijemePrijema()))
        .dodajStupac("Oznaka aranžmana", 16, e -> Integer.toString(e.oznakaAranzmana()))
        .dodajStupac("Naziv aranžmana", 20, e -> agencija.dajAranzman(e.oznakaAranzmana()).naziv())
        .dodajStupac("Status", 18, e -> e.nazivStanja())
        .napravi();

    tablicniIspis.ispisiZaglavlje();
    tablicniIspis.ispisi(rezervacije);

  }

}
