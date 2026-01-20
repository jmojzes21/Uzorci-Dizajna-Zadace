package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.PostavkeSustava;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.IKomanda;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.KomandaKreator;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis.TablicniIspisGraditelj;
import java.time.LocalDate;
import java.util.List;

public class KomandaITAK implements IKomanda {

  private final LocalDate datumOd;
  private final LocalDate datumDo;

  public KomandaITAK(LocalDate datumOd, LocalDate datumDo) {
    this.datumOd = datumOd;
    this.datumDo = datumDo;
  }

  public KomandaITAK() {
    this.datumOd = null;
    this.datumDo = null;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    List<Aranzman> aranzmani;

    if (datumOd == null || datumDo == null) {
      aranzmani = agencija.dajAranzmane();
    } else {
      aranzmani = agencija.dajAranzmane(datumOd, datumDo);
    }

    if (aranzmani.isEmpty()) {
      System.out.println("Nema aranžmana za prikaz.");
      return;
    }

    prikaziAranzmane(aranzmani);
  }

  private void prikaziAranzmane(List<Aranzman> aranzmani) {

    var postavke = PostavkeSustava.dajInstancu();
    var f = Formati.dajInstancu();

    aranzmani = Aranzman.sortiraj(aranzmani, postavke.sortirajUzlazno());

    var tablicniIspis = new TablicniIspisGraditelj()
        .koristiPrelamanjeTeksta(postavke.koristiPrelamanjeTeksta())
        .postaviIspisDodatnihCrta(postavke.ispisDodatnihCrta())
        .dodajStupac("Oznaka", 6)
        .poravnajDesno()
        .dodajStupac("Naziv", 30)
        .dodajStupac("Početni datum", 15)
        .dodajStupac("Završni datum", 15)
        .dodajStupac("Vrijeme kretanja", 16)
        .dodajStupac("Vrijeme povratka", 16)
        .dodajStupac("Cijena", 10)
        .poravnajDesno()
        .dodajStupac("Min putnika", 12)
        .poravnajDesno()
        .dodajStupac("Max putnika", 12)
        .poravnajDesno()
        .dodajStupac("Status", 12)
        .napravi();

    System.out.println("Pregled turističkih aranžmana");
    tablicniIspis.ispisiZaglavlje();

    for (Aranzman e : aranzmani) {
      List<String> podaci = List.of(
          Integer.toString(e.oznaka()), e.naziv(),
          f.formatiraj(e.pocetniDatum()), f.formatiraj(e.zavrsniDatum()),
          f.formatiraj(e.vrijemeKretanja()), f.formatiraj(e.vrijemePovratka()),
          f.formatiraj(e.cijena()),
          Integer.toString(e.minBrojPutnika()), Integer.toString(e.maxBrojPutnika()),
          e.nazivStanja()
      );
      tablicniIspis.ispisi(podaci);
    }
    tablicniIspis.ispisiCrtu();


  }


  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      if (args.isEmpty()) {
        return new KomandaITAK();
      }

      var f = Formati.dajInstancu();

      var uzorak = new RegexKomandeGraditelj()
          .dodajDatum("od")
          .dodajDatum("do")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "ITAK [od do]";
        throw new NeispravnaKomandaGreska(opis);
      }

      LocalDate datumOd = f.parsirajDatum(matcher.group("od"));
      LocalDate datumDo = f.parsirajDatum(matcher.group("do"));

      return new KomandaITAK(datumOd, datumDo);
    }
  }

}
