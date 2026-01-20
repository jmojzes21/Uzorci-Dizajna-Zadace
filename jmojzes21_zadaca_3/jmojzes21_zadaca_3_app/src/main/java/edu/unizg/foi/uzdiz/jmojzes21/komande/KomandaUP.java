package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.AranzmanRepozitorij;
import edu.unizg.foi.uzdiz.jmojzes21.RezervacijaRepozitorij;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.IKomanda;
import edu.unizg.foi.uzdiz.jmojzes21.komande.glavno.KomandaKreator;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class KomandaUP implements IKomanda {

  private final String odabir;
  private final String putanja;

  public KomandaUP(String odabir, String putanja) {
    this.odabir = odabir;
    this.putanja = putanja;
  }

  @Override
  public void izvrsi(TuristickaAgencija agencija) {

    Path putanja = Path.of(this.putanja);

    try {
      switch (odabir) {
        case "A":
          ucitajAranzmane(agencija, putanja);
          break;
        case "R":
          ucitajRezervacije(agencija, putanja);
          break;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  private void ucitajAranzmane(TuristickaAgencija agencija, Path putanja) throws IOException {
    var aranzmanRepo = new AranzmanRepozitorij();
    List<Aranzman> aranzmani = aranzmanRepo.ucitajAranzmane(putanja);
    agencija.dodajAranzmane(aranzmani);
  }

  private void ucitajRezervacije(TuristickaAgencija agencija, Path putanja) throws IOException {
    var rezervacijaRepo = new RezervacijaRepozitorij();
    List<Rezervacija> rezervacije = rezervacijaRepo.ucitajRezervacije(putanja);
    agencija.zaprimiRezervacije(rezervacije);
  }

  public static class Kreator extends KomandaKreator {

    @Override
    public IKomanda parsiraj(String args) throws Exception {

      var uzorak = new RegexKomandeGraditelj()
          .dodajIzraz("odabir", "(A)|(R)")
          .dodajTekst("datoteka")
          .dajUzorak();

      var matcher = uzorak.matcher(args);
      if (!matcher.matches()) {
        String opis = "UP [A|R] nazivDatoteke";
        throw new NeispravnaKomandaGreska(opis);
      }

      String odabir = matcher.group("odabir");
      String datoteka = matcher.group("datoteka");

      return new KomandaUP(odabir, datoteka);
    }
  }

}
