package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.TablicniIspisGraditelj;
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

    var f = Formati.dajInstancu();

    var tablicniIspis = new TablicniIspisGraditelj()
        .dodajStupac("Vrijeme prijema", 24)
        .dodajStupac("Oznaka aranžmana", 16)
        .poravnajDesno()
        .dodajStupac("Naziv aranžmana", 20)
        .dodajStupac("Status", 18)
        .napravi();

    System.out.println("Pregled rezervacija za osobu");
    tablicniIspis.ispisiZaglavlje();

    for (Rezervacija e : rezervacije) {
      Aranzman aranzman = e.dajAranzman();
      List<String> podaci = List.of(
          f.formatiraj(e.vrijemePrijema()), Integer.toString(e.oznakaAranzmana()), aranzman.naziv(), e.nazivStanja()
      );
      tablicniIspis.ispisi(podaci);
    }
    tablicniIspis.ispisiCrtu();

  }

}
