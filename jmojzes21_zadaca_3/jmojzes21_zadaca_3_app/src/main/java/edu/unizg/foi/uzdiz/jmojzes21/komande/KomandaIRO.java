package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.TablicniIspisGraditelj;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class KomandaIRO implements IKomanda {

  private final String ime;
  private final String prezime;
  private final Map<String, String> filter;

  public KomandaIRO(String ime, String prezime) {
    this(ime, prezime, null);
  }

  public KomandaIRO(String ime, String prezime, Map<String, String> filter) {
    this.ime = ime;
    this.prezime = prezime;
    this.filter = filter;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {
    prikaziRezervacijeKorisnika(agencija, ime, prezime);
  }

  private void prikaziRezervacijeKorisnika(TuristickaAgencija agencija, String ime, String prezime) {

    List<Rezervacija> rezervacije = agencija.dajRezervacijeKorisnika(ime, prezime);
    if (rezervacije.isEmpty()) {
      System.out.println("Korisnik nema rezervacija.");
      return;
    }

    prikaziRezervacije(rezervacije);
  }

  private void prikaziRezervacije(List<Rezervacija> rezervacije) {

    var postavke = PostavkeSustava.dajInstancu();
    var f = Formati.dajInstancu();

    rezervacije = Rezervacija.sortiraj(rezervacije, postavke.sortirajUzlazno());

    var tablicniIspis = new TablicniIspisGraditelj()
        .koristiPrelamanjeTeksta(postavke.koristiPrelamanjeTeksta())
        .postaviIspisDodatnihCrta(postavke.ispisDodatnihCrta())
        .dodajStupac("Vrijeme prijema", 24)
        .dodajStupac("Oznaka", 6)
        .poravnajDesno()
        .dodajStupac("Naziv aranžmana", 30)
        .dodajStupac("Vrsta", 14)
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

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var uzorak = new RegexKomandeGraditelj()
          .dodajTekst("ime")
          .dodajTekst("prezime")
          .dodajIzraz("filter", "(\\s?\\w+\\=\\w+)+", true)
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "IRO ime prezime [A=oznaka] | [R=PA|Č|O|OD]";
        throw new NeispravnaKomandaGreska(opis);
      }

      String ime = matcher.group("ime");
      String prezime = matcher.group("prezime");
      String filter = matcher.group("filter");

      if (filter != null) {
        var filterMap = parsirajFiltre(filter.trim());
        return new KomandaIRO(ime, prezime, filterMap);
      }

      return new KomandaIRO(ime, prezime);
    }

    private Map<String, String> parsirajFiltre(String filtri) {

      Map<String, String> filterMap = new TreeMap<>();

      String[] filtriDijelovi = filtri.split("\\s+");
      for (String filter : filtriDijelovi) {
        String[] v = filter.split("=");
        if (v.length != 2) {continue;}

        filterMap.put(v[0], v[1]);
      }

      return filterMap;
    }

  }

}
