package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.FormatDatuma;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.TablicniIspisGraditelj;
import java.time.LocalDate;
import java.util.List;

public class KomandaITAK {

  private final TuristickaAgencija agencija;

  public KomandaITAK(TuristickaAgencija agencija) {
    this.agencija = agencija;
  }

  public void obradiKomanduPregledAranzmana(String args) throws Exception {

    List<Aranzman> aranzmani;

    if (args.isEmpty()) {
      aranzmani = agencija.dajAranzmane();
    } else {

      var uzorak = new RegexKomandeGraditelj()
          .dodajDatum("od")
          .dodajDatum("do")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "ITAK [od do]";
        throw new NeispravnaKomandaGreska(opis);
      }

      LocalDate datumOd = FormatDatuma.dajInstancu().parsirajDatum(matcher.group("od"));
      LocalDate datumDo = FormatDatuma.dajInstancu().parsirajDatum(matcher.group("do"));

      aranzmani = agencija.dajAranzmane(datumOd, datumDo);
    }

    if (aranzmani.isEmpty()) {
      System.out.println("Nema aranžmana za prikaz.");
      return;
    }

    prikaziAranzmane(aranzmani);
  }

  private void prikaziAranzmane(List<Aranzman> aranzmani) {

    var formatDatuma = FormatDatuma.dajInstancu();

    var tablicniIspis = new TablicniIspisGraditelj<Aranzman>()
        .dodajStupac("Oznaka", 6, e -> Integer.toString(e.oznaka()))
        .poravnajDesno()
        .dodajStupac("Naziv", 40, e -> e.naziv())
        .dodajStupac("Početni datum", 14, e -> formatDatuma.formatiraj(e.pocetniDatum()))
        .dodajStupac("Završni datum", 14, e -> formatDatuma.formatiraj(e.zavrsniDatum()))
        .dodajStupac("Vrijeme kretanja", 18, e -> formatDatuma.formatiraj(e.vrijemeKretanja()))
        .dodajStupac("Vrijeme povratka", 18, e -> formatDatuma.formatiraj(e.vrijemePovratka()))
        .dodajStupac("Cijena", 12, e -> String.format("%.2f", e.cijena()))
        .poravnajDesno()
        .dodajStupac("Min putnika", 12, e -> Integer.toString(e.minBrojPutnika()))
        .poravnajDesno()
        .dodajStupac("Max putnika", 12, e -> Integer.toString(e.maxBrojPutnika()))
        .poravnajDesno()
        .dodajStupac("Status", 20, e -> e.nazivStanja())
        .napravi();

    tablicniIspis.ispisiZaglavlje();
    tablicniIspis.ispisi(aranzmani);

  }

}
