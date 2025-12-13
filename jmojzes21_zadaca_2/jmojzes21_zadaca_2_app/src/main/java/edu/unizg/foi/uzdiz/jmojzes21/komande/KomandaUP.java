package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.AranzmanRepozitorij;
import edu.unizg.foi.uzdiz.jmojzes21.RezervacijaRepozitorij;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class KomandaUP {

  private final TuristickaAgencija agencija;

  public KomandaUP(TuristickaAgencija agencija) {
    this.agencija = agencija;
  }

  public void obradiKomandu(String args) throws Exception {

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
    String nazivDatoteke = matcher.group("datoteka");
    Path putanja = Path.of(nazivDatoteke);

    switch (odabir) {
      case "A":
        ucitajAranzmane(putanja);
        break;
      case "R":
        ucitajRezervacije(putanja);
        break;
    }

  }

  private void ucitajAranzmane(Path putanja) throws IOException {
    var aranzmanRepo = new AranzmanRepozitorij();
    List<Aranzman> aranzmani = aranzmanRepo.ucitajAranzmane(putanja);
    agencija.dodajAranzmane(aranzmani);
  }

  private void ucitajRezervacije(Path putanja) throws IOException {
    var rezervacijaRepo = new RezervacijaRepozitorij();
    List<Rezervacija> rezervacije = rezervacijaRepo.ucitajRezervacije(putanja);
    agencija.zaprimiRezervacije(rezervacije);
  }

}
