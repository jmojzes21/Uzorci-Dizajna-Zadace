package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija.StanjeId;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.TablicniIspisGraditelj;
import java.util.ArrayList;
import java.util.List;

public class KomandaIRTA implements IKomanda {


  private final int oznaka;
  private final String filter;

  public KomandaIRTA(int oznaka, String filter) {
    this.oznaka = oznaka;
    this.filter = filter != null ? filter : "PAČODO";
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    String filter = this.filter;

    boolean prikaziPrimljeneAktivne = filter.contains("PA");
    boolean prikaziNaCekanju = filter.contains("Č");
    boolean prikaziOdgodjene = false;
    if (filter.contains("OD")) {
      prikaziOdgodjene = true;
      filter = filter.replace("OD", "");
    }
    boolean prikaziOtkazane = filter.contains("O");

    List<StanjeId> prikaziStanja = new ArrayList<>();

    if (prikaziPrimljeneAktivne) {
      prikaziStanja.add(StanjeId.primljena);
      prikaziStanja.add(StanjeId.aktivna);
    }

    if (prikaziNaCekanju) {
      prikaziStanja.add(StanjeId.naCekanju);
    }

    if (prikaziOdgodjene) {
      prikaziStanja.add(StanjeId.odgodjena);
    }

    if (prikaziOtkazane) {
      prikaziStanja.add(StanjeId.otkazana);
    }

    List<Rezervacija> rezervacije = agencija.dajRezervacijeAranzmana(oznaka, prikaziStanja);

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

    var postavke = PostavkeSustava.dajInstancu();
    var f = Formati.dajInstancu();

    rezervacije = Rezervacija.sortiraj(rezervacije, postavke.sortirajUzlazno());

    var tablicniIspis = new TablicniIspisGraditelj()
        .koristiPrelamanjeTeksta(postavke.koristiPrelamanjeTeksta())
        .postaviIspisDodatnihCrta(postavke.ispisDodatnihCrta())
        .dodajStupac("Ime", 18)
        .dodajStupac("Prezime", 18)
        .dodajStupac("Vrijeme prijema", 24)
        .dodajStupac("Vrsta", 14)
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

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

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

      return new KomandaIRTA(oznaka, filter);
    }
  }

}
