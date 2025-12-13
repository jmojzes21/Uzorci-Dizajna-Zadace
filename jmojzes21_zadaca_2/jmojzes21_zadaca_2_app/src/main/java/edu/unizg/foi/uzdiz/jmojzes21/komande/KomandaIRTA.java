package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.TablicniIspisGraditelj;
import java.util.List;

public class KomandaIRTA {

  private final TuristickaAgencija agencija;

  public KomandaIRTA(TuristickaAgencija agencija) {
    this.agencija = agencija;
  }

  public void obradiKomandu(String args) throws Exception {

    var uzorak = new RegexKomandeGraditelj()
        .dodajBroj("oznaka")
        .dodajTekstOpcionalno("filter")
        .dajUzorak();

    var matcher = uzorak.matcher(args);
    if (!matcher.matches()) {
      String opis = "IRTA oznaka [PA|Č|O|OD]";
      throw new NeispravnaKomandaGreska(opis);
    }

    int oznaka = Integer.parseInt(matcher.group("oznaka"));
    String filter = matcher.group("filter");
    if (filter == null) {filter = "PAČODO";}

    prikaziRezervacijeAranzmana(oznaka, filter);
  }

  private void prikaziRezervacijeAranzmana(int oznaka, String filter) {

    boolean prikaziPrimljeneAktivne = filter.contains("PA");
    boolean prikaziNaCekanju = filter.contains("Č");
    boolean prikaziOdgodjene = false;
    if (filter.contains("OD")) {
      prikaziOdgodjene = true;
      filter = filter.replace("OD", "");
    }
    boolean prikaziOtkazane = filter.contains("O");

    List<Rezervacija> rezervacije = agencija.dajRezervacijeAranzmana(oznaka, prikaziPrimljeneAktivne, prikaziNaCekanju,
        prikaziOtkazane, prikaziOdgodjene);

    if (rezervacije == null) {
      System.out.println("Aranžman ne postoji.");
      return;
    }

    if (rezervacije.isEmpty()) {
      System.out.println("Nema rezervacija za prikaz.");
      return;
    }

    prikaziRezervacije(rezervacije, prikaziOtkazane);
  }

  private void prikaziRezervacije(List<Rezervacija> rezervacije, boolean prikaziOtkazane) {

    boolean sortirajUzlazno = PostavkeSustava.dajInstancu().sortirajUzlazno();
    rezervacije = Rezervacija.sortiraj(rezervacije, sortirajUzlazno);

    var f = Formati.dajInstancu();

    var tablicniIspis = new TablicniIspisGraditelj()
        .dodajStupac("Ime", 18)
        .dodajStupac("Prezime", 18)
        .dodajStupac("Vrijeme prijema", 24)
        .dodajStupac("Status", 18)
        .dodajStupac("Vrijeme otkaza", 24)
        .prikazujStupac(prikaziOtkazane)
        .napravi();

    System.out.println("Pregled rezervacija za turistički aranžman");
    tablicniIspis.ispisiZaglavlje();

    for (Rezervacija e : rezervacije) {
      List<String> podaci = List.of(
          e.korisnik().ime(), e.korisnik().prezime(), f.formatiraj(e.vrijemePrijema()), e.nazivStanja(),
          f.formatiraj(e.vrijemeOtkaza())
      );
      tablicniIspis.ispisi(podaci);
    }
    tablicniIspis.ispisiCrtu();

  }

}
