package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.IKomanda;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.KomandaKreator;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaPTAR implements IKomanda {

  private final String ime;
  private final String prezime;
  private final int oznaka;

  public KomandaPTAR(String ime, String prezime, int oznaka) {
    this.ime = ime;
    this.prezime = prezime;
    this.oznaka = oznaka;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    Aranzman aranzman = agencija.dajAranzman(oznaka);
    if (aranzman == null) {
      throw new RuntimeException("Aranžman s oznakom " + oznaka + " ne postoji!");
    }

    var korisnik = new Korisnik(ime, prezime);
    boolean uspjeh = aranzman.dodajPromatraca(korisnik);

    if (uspjeh) {
      System.out.printf("Korisnik %s se uspješno pretplatio za aranžman %d.\n", korisnik.punoIme(), oznaka);
    } else {
      System.out.printf("Korisnik %s je već pretplaćen za aranžman %d.\n", korisnik.punoIme(), oznaka);
    }

  }

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var uzorak = new RegexKomandeGraditelj()
          .dodajTekst("ime")
          .dodajTekst("prezime")
          .dodajBroj("oznaka")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "PTAR ime prezime oznaka";
        throw new NeispravnaKomandaGreska(opis);
      }

      String ime = matcher.group("ime");
      String prezime = matcher.group("prezime");
      int oznaka = Integer.parseInt(matcher.group("oznaka"));

      return new KomandaPTAR(ime, prezime, oznaka);
    }

  }

}
