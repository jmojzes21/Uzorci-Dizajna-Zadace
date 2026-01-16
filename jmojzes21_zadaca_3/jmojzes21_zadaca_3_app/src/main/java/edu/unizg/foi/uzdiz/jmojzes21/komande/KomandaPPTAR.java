package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.logika.visitor.PretrazivanjePutovanjaVisitor;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.TablicniIspisGraditelj;
import java.util.List;

public class KomandaPPTAR implements IKomanda {

  private final String odabir;
  private final String rijec;

  public KomandaPPTAR(String odabir, String rijec) {
    this.odabir = odabir;
    this.rijec = rijec;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    switch (odabir) {
      case "A":
        pretraziAranzmane(agencija);
        break;
      case "R":
        pretraziRezervacije(agencija);
        break;
    }

  }

  private void pretraziAranzmane(TuristickaAgencija agencija) {

    var aranzmani = agencija.dajAranzmane();
    var pretrazivanje = new PretrazivanjePutovanjaVisitor(rijec);

    for (var aranzman : aranzmani) {
      aranzman.prihvati(pretrazivanje);
    }

    List<Aranzman> rezultat = pretrazivanje.aranzmani();
    prikaziAranzmane(rezultat);
  }

  private void pretraziRezervacije(TuristickaAgencija agencija) {

    var aranzmani = agencija.dajAranzmane();
    var pretrazivanje = new PretrazivanjePutovanjaVisitor(rijec);

    for (var aranzman : aranzmani) {
      for (var rezervacija : aranzman.rezervacije()) {
        rezervacija.prihvati(pretrazivanje);
      }
    }

    List<Rezervacija> rezultat = pretrazivanje.rezervacije();
    prikaziRezervacije(rezultat);
  }

  private void prikaziAranzmane(List<Aranzman> aranzmani) {

    var postavke = PostavkeSustava.dajInstancu();
    aranzmani = Aranzman.sortiraj(aranzmani, postavke.sortirajUzlazno());

    var tablicniIspis = new TablicniIspisGraditelj()
        .koristiPrelamanjeTeksta(postavke.koristiPrelamanjeTeksta())
        .postaviIspisDodatnihCrta(postavke.ispisDodatnihCrta())
        .dodajStupac("Oznaka", 6)
        .poravnajDesno()
        .dodajStupac("Naziv", 30)
        .napravi();

    System.out.println("Rezultat pretrage turističkih aranžmana");
    tablicniIspis.ispisiZaglavlje();

    for (Aranzman e : aranzmani) {
      List<String> podaci = List.of(Integer.toString(e.oznaka()), e.naziv());
      tablicniIspis.ispisi(podaci);
    }
    tablicniIspis.ispisiCrtu();

  }

  private void prikaziRezervacije(List<Rezervacija> rezervacije) {

    var postavke = PostavkeSustava.dajInstancu();
    var f = Formati.dajInstancu();

    rezervacije = Rezervacija.sortiraj(rezervacije, postavke.sortirajUzlazno());

    var tablicniIspis = new TablicniIspisGraditelj()
        .koristiPrelamanjeTeksta(postavke.koristiPrelamanjeTeksta())
        .postaviIspisDodatnihCrta(postavke.ispisDodatnihCrta())
        .dodajStupac("Oznaka", 6)
        .poravnajDesno()
        .dodajStupac("Naziv aranžmana", 30)
        .dodajStupac("Ime", 18)
        .dodajStupac("Prezime", 18)
        .dodajStupac("Vrijeme prijema", 24)
        .dodajStupac("Vrsta", 14)
        .napravi();

    System.out.println("Rezultat pretrage rezervacija");
    tablicniIspis.ispisiZaglavlje();

    for (Rezervacija e : rezervacije) {
      List<String> podaci = List.of(Integer.toString(e.dajAranzman().oznaka()), e.dajAranzman().naziv(),
          e.korisnik().ime(), e.korisnik().prezime(), f.formatiraj(e.vrijemePrijema()), e.nazivStanja());
      tablicniIspis.ispisi(podaci);
    }
    tablicniIspis.ispisiCrtu();

  }

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var uzorak = new RegexKomandeGraditelj()
          .dodajIzraz("odabir", "(A)|(R)")
          .dodajTekst("rijec")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "PPTAR [A|R] riječ";
        throw new NeispravnaKomandaGreska(opis);
      }

      String odabir = matcher.group("odabir");
      String rijec = matcher.group("rijec");

      return new KomandaPPTAR(odabir, rijec);
    }
  }

}
