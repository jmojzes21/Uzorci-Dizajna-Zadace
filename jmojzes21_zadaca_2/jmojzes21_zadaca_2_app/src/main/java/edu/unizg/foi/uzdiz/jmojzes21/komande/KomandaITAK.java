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

    var fd = FormatDatuma.dajInstancu();

    var tablicniIspis = new TablicniIspisGraditelj()
        .dodajStupac("Oznaka", 6)
        .poravnajDesno()
        .dodajStupac("Naziv", 40)
        .dodajStupac("Početni datum", 14)
        .dodajStupac("Završni datum", 14)
        .dodajStupac("Vrijeme kretanja", 18)
        .dodajStupac("Vrijeme povratka", 18)
        .dodajStupac("Cijena", 12)
        .poravnajDesno()
        .dodajStupac("Min putnika", 12)
        .poravnajDesno()
        .dodajStupac("Max putnika", 12)
        .poravnajDesno()
        .dodajStupac("Status", 20)
        .napravi();

    tablicniIspis.ispisiZaglavlje();

    for (Aranzman e : aranzmani) {
      List<String> podaci = List.of(
          Integer.toString(e.oznaka()), e.naziv(),
          fd.formatiraj(e.pocetniDatum()), fd.formatiraj(e.zavrsniDatum()),
          fd.formatiraj(e.vrijemeKretanja()), fd.formatiraj(e.vrijemePovratka()),
          String.format("%.2f", e.cijena()),
          Integer.toString(e.minBrojPutnika()), Integer.toString(e.maxBrojPutnika()),
          e.nazivStanja()
      );
      tablicniIspis.ispisi(podaci);
    }


  }

}
