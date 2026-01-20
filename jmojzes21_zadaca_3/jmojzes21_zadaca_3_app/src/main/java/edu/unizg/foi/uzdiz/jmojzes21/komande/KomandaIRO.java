package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.logika.cor.FilterRezervacije;
import edu.unizg.foi.uzdiz.jmojzes21.logika.cor.RezervacijeAranzmanaFilter;
import edu.unizg.foi.uzdiz.jmojzes21.logika.cor.RezervacijeKorisnikaFilter;
import edu.unizg.foi.uzdiz.jmojzes21.logika.cor.StanjeRezervacijeFilter;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.StanjeRezervacijeParser;
import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.TablicniIspisGraditelj;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KomandaIRO implements IKomanda {

  private final String ime;
  private final String prezime;

  private final Integer oznaka;
  private final String filterStanja;

  public KomandaIRO(String ime, String prezime) {
    this.ime = ime;
    this.prezime = prezime;
    this.oznaka = null;
    this.filterStanja = null;
  }

  public KomandaIRO(String ime, String prezime, Integer oznaka, String filterStanja) {
    this.ime = ime;
    this.prezime = prezime;
    this.oznaka = oznaka;
    this.filterStanja = filterStanja;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    FilterRezervacije lanacFiltera = napraviLanacFiltera();
    List<Rezervacija> rezervacije = new ArrayList<>();

    var aranzmani = agencija.dajAranzmane();
    for (var aranzman : aranzmani) {
      rezervacije.addAll(aranzman.rezervacije().stream()
          .filter(r -> lanacFiltera.zadovoljava(r))
          .toList());
    }

    if (rezervacije.isEmpty()) {
      System.out.println("Korisnik nema rezervacija.");
      return;
    }

    prikaziRezervacije(rezervacije);
  }

  private FilterRezervacije napraviLanacFiltera() {

    var korisnik = new Korisnik(ime, prezime);
    var graditelj = new FilterRezervacije.Graditelj();

    graditelj.dodaj(new RezervacijeKorisnikaFilter(korisnik));

    if (oznaka != null) {
      graditelj.dodaj(new RezervacijeAranzmanaFilter(oznaka));
    }

    if (filterStanja != null) {
      var sr = new StanjeRezervacijeParser();
      var stanja = sr.parsiraj(filterStanja);
      graditelj.dodaj(new StanjeRezervacijeFilter(stanja));
    }

    return graditelj.napraviLanac();

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

        Integer oznaka = filterMap.containsKey("A") ? Integer.parseInt(filterMap.get("A")) : null;
        String filterStanja = filterMap.get("R");

        return new KomandaIRO(ime, prezime, oznaka, filterStanja);
      }

      return new KomandaIRO(ime, prezime);
    }

    private Map<String, String> parsirajFiltre(String filtri) {

      Map<String, String> filterMap = new HashMap<>();

      String[] filtriDijelovi = filtri.split("\\s+");
      for (String filter : filtriDijelovi) {
        String[] v = filter.split("=");
        if (v.length != 2) {continue;}
        filterMap.put(v[0], v[1]);
      }

      for (var e : filterMap.entrySet()) {
        switch (e.getKey()) {
          case "A":
            if (!e.getValue().matches("\\d+")) {
              throw new RuntimeException("Filter A zahtjeva broj kao vrijednost!");
            }
            break;
          case "R":
            break;
          default:
            throw new NeispravnaKomandaGreska("Nepoznati filter " + e.getKey() + "!");
        }
      }
      return filterMap;
    }

  }

}
