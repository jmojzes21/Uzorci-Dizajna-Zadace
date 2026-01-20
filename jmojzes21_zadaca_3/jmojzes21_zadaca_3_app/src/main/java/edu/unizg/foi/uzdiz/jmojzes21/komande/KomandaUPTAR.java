package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.IKomanda;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.KomandaKreator;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import java.util.List;

public class KomandaUPTAR implements IKomanda {

  private final String ime;
  private final String prezime;
  private final int oznaka;

  public KomandaUPTAR(String ime, String prezime, int oznaka) {
    this.ime = ime;
    this.prezime = prezime;
    this.oznaka = oznaka;
  }

  public KomandaUPTAR(int oznaka) {
    this.ime = null;
    this.prezime = null;
    this.oznaka = oznaka;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    Aranzman aranzman = agencija.dajAranzman(oznaka);
    if (aranzman == null) {
      throw new RuntimeException("Aranžman s oznakom " + oznaka + " ne postoji!");
    }

    if (ime != null && prezime != null) {
      var korisnik = new Korisnik(ime, prezime);
      ukiniPretplatuKorisnika(aranzman, korisnik);
    } else {
      ukiniPretplatuSvihKorisnika(aranzman);
    }

  }

  private void ukiniPretplatuKorisnika(Aranzman aranzman, Korisnik korisnik) {

    boolean uspjeh = aranzman.ukloniPromatraca(korisnik);

    if (uspjeh) {
      System.out.printf("Korisnik %s je uspješno ukinuo pretplatu za aranžman %d.\n", korisnik.punoIme(), oznaka);
    } else {
      System.out.printf("Korisnik %s nema pretplate za aranžman %d.\n", korisnik.punoIme(), oznaka);
    }
  }

  private void ukiniPretplatuSvihKorisnika(Aranzman aranzman) {

    List<Korisnik> korisnici = aranzman.dajPretplaceneKorisnike();

    if (korisnici.isEmpty()) {
      System.out.printf("Aranžman %d nema pretplate korisnika.\n", oznaka);
      return;
    }

    aranzman.ukloniPretplaceneKorisnike();
    System.out.printf("Ukinute sve pretplate za aranžman %d.\n", oznaka);

  }


  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      IKomanda komanda = parsirajZaKorisnika(args);
      if (komanda != null) {
        return komanda;
      }

      komanda = parsirajZaSveKorisnike(args);
      if (komanda != null) {
        return komanda;
      }

      String opis = "UPTAR [ime prezime oznaka] | [oznaka]";
      throw new NeispravnaKomandaGreska(opis);

    }

    private IKomanda parsirajZaKorisnika(String args) {

      var uzorak = new RegexKomandeGraditelj()
          .dodajTekst("ime")
          .dodajTekst("prezime")
          .dodajBroj("oznaka")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        return null;
      }

      String ime = matcher.group("ime");
      String prezime = matcher.group("prezime");
      int oznaka = Integer.parseInt(matcher.group("oznaka"));

      return new KomandaUPTAR(ime, prezime, oznaka);
    }

    private IKomanda parsirajZaSveKorisnike(String args) {

      var uzorak = new RegexKomandeGraditelj()
          .dodajBroj("oznaka")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        return null;
      }

      int oznaka = Integer.parseInt(matcher.group("oznaka"));
      return new KomandaUPTAR(oznaka);
    }

  }

}
