package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.statistika.StatistikaAranzmana;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.TablicniIspisGraditelj;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class KomandaITAS {

  private final TuristickaAgencija agencija;

  public KomandaITAS(TuristickaAgencija agencija) {
    this.agencija = agencija;
  }

  public void obradiKomandu(String args) throws Exception {

    var f = Formati.dajInstancu();

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
        String opis = "ITAS [od do]";
        throw new NeispravnaKomandaGreska(opis);
      }

      LocalDate datumOd = f.parsirajDatum(matcher.group("od"));
      LocalDate datumDo = f.parsirajDatum(matcher.group("do"));

      aranzmani = agencija.dajAranzmane(datumOd, datumDo);
    }

    if (aranzmani.isEmpty()) {
      System.out.println("Nema aranžmana za prikaz.");
      return;
    }

    dajStatistikuAranzmana(aranzmani);
  }

  private void dajStatistikuAranzmana(List<Aranzman> aranzmani) {
    List<StatistikaAranzmana> statistika = agencija.dajStatistikuAranzmana(aranzmani);
    prikaziStatistiku(statistika);
  }

  private void prikaziStatistiku(List<StatistikaAranzmana> statistika) {

    var postavke = PostavkeSustava.dajInstancu();
    var f = Formati.dajInstancu();

    statistika = sortirajStatistiku(statistika, postavke.sortirajUzlazno());

    var tablicniIspis = new TablicniIspisGraditelj()
        .koristiPrelamanjeTeksta(postavke.koristiPrelamanjeTeksta())
        .postaviIspisDodatnihCrta(postavke.ispisDodatnihCrta())
        .dodajStupac("Oznaka", 8)
        .poravnajDesno()
        .dodajStupac("Naziv", 30)
        .poravnajLijevo()
        .dodajStupac("Ukupno rezervacija", 20)
        .poravnajDesno()
        .dodajStupac("Broj aktivnih", 16)
        .poravnajDesno()
        .dodajStupac("Broj na čekanju", 16)
        .poravnajDesno()
        .dodajStupac("Broj odgođenih", 16)
        .poravnajDesno()
        .dodajStupac("Broj otkazanih", 16)
        .poravnajDesno()
        .dodajStupac("Ukupan prihod", 16)
        .poravnajDesno()
        .napravi();

    System.out.println("Pregled statističkih podataka za turističke aranžmane");
    tablicniIspis.ispisiZaglavlje();

    for (StatistikaAranzmana e : statistika) {
      List<String> podaci = List.of(
          Integer.toString(e.aranzman().oznaka()), e.aranzman().naziv(),
          Integer.toString(e.ukupanBrojRezervacija()),
          Integer.toString(e.brojAktivnihRezervacija()), Integer.toString(e.brojRezervacijaNaCekanju()),
          Integer.toString(e.brojOdgodjenihRezervacija()), Integer.toString(e.brojOtkazanihRezervacija()),
          f.formatiraj(e.ukupanPrihod())
      );
      tablicniIspis.ispisi(podaci);
    }
    tablicniIspis.ispisiCrtu();

  }

  private List<StatistikaAranzmana> sortirajStatistiku(List<StatistikaAranzmana> statistika, boolean uzlazno) {
    var comparator = Comparator.comparing((StatistikaAranzmana e) -> e.aranzman().pocetniDatum());
    if (!uzlazno) {comparator = comparator.reversed();}
    return statistika.stream().sorted(comparator).toList();
  }

}
