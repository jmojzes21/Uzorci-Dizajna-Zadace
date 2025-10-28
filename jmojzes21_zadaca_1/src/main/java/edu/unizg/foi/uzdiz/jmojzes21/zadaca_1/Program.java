package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.CitacOpcija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.FormatDatuma;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.StupacTablice;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.TablicniIspisGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv.CsvCitac;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv.CsvFormatGreska;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv.CsvRedak;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program {

  public static void main(String[] args) throws IOException {
    Locale.setDefault(Locale.ENGLISH);
    var program = new Program();
    program.pokreni(args);
  }

  private boolean zaprimajKomandeKorisnika = true;

  public void pokreni(String[] args) throws IOException {

    var konfig = Konfiguracija.dajKonfiguraciju();

    var citacOpcija = new CitacOpcija();
    citacOpcija.ucitajOpcije(args);

    konfig.setPutanjaAranzmani(citacOpcija.dajOpciju("ta"));
    konfig.setPutanjaRezervacije(citacOpcija.dajOpciju("rta"));

    var aranzmani = ucitajAranzmane(konfig.putanjaAranzmani());
    var rezervacije = ucitajRezervacije(konfig.putanjaRezervacije());

    var agencija = TuristickaAgencija.dajInstancu();
    agencija.ucitajAranzmane(aranzmani);

    for (var r : rezervacije) {
      try {
        agencija.zaprimiRezervaciju(r);
      } catch (Exception e) {
        EvidencijaGresaka.dajInstancu().evidentiraj(e);
      }
    }

    obradiKomandeKorisnika();

  }

  private void obradiKomandeKorisnika() {

    try (var skener = new Scanner(System.in)) {
      while (zaprimajKomandeKorisnika) {
        String linija = skener.nextLine();

        try {
          obradiKomanduKorisnika(linija.trim());
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  private void obradiKomanduKorisnika(String komanda) throws Exception {

    String naziv = dajNazivKomande(komanda);

    switch (naziv) {
      case "ITAK":
        obradiKomanduPregledAranzmana(komanda);
        break;
      case "ITAP":
        obradiKomanduPregledPojedinogAranzmana(komanda);
        break;
      case "IRTA":
        obradiKomanduPregledRezervacijaAranzmana(komanda);
        break;
      case "Q":
        zaprimajKomandeKorisnika = false;
        break;
    }

  }

  private void obradiKomanduPregledAranzmana(String komanda) throws Exception {

    TuristickaAgencija agencija = TuristickaAgencija.dajInstancu();
    List<Aranzman> aranzmani;

    if (komanda.equals("ITAK")) {
      aranzmani = agencija.dajAranzmane();
    } else {

      var uzorak = new RegexKomandeGraditelj("ITAK")
          .dodajDatum("od")
          .dodajDatum("do")
          .dajUzorak();

      var matcher = uzorak.matcher(komanda);
      if (!matcher.matches()) {
        throw new NeispravnaKomandaGreska();
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

    var tablicniIspis = new TablicniIspisGraditelj()
        .dodajStupac("Oznaka", 6, StupacTablice.PORAVNANJE_DESNO)
        .dodajStupac("Naziv", 40)
        .dodajStupac("Početni datum", 14)
        .dodajStupac("Završni datum", 14)
        .dodajStupac("Vrijeme kretanja", 18)
        .dodajStupac("Vrijeme povratka", 18)
        .dodajStupac("Cijena", 12, StupacTablice.PORAVNANJE_DESNO)
        .dodajStupac("Min putnika", 12, StupacTablice.PORAVNANJE_DESNO)
        .dodajStupac("Max putnika", 12, StupacTablice.PORAVNANJE_DESNO)
        .napravi();

    tablicniIspis.ispisiZaglavlje();
    tablicniIspis.ispisi(aranzmani.stream()
        .map(e -> new String[]{
            Integer.toString(e.oznaka()), e.naziv(),
            formatDatuma.formatirajDatum(e.pocetniDatum()),
            formatDatuma.formatirajDatum(e.zavrsniDatum()),
            formatDatuma.formatirajVrijeme(e.vrijemeKretanja()),
            formatDatuma.formatirajVrijeme(e.vrijemePovratka()),
            String.format("%.2f", e.cijena()),
            Integer.toString(e.minBrojPutnika()), Integer.toString(e.maxBrojPutnika())
        })
        .toList());

  }

  private void obradiKomanduPregledPojedinogAranzmana(String komanda) throws Exception {

    TuristickaAgencija agencija = TuristickaAgencija.dajInstancu();

    var uzorak = new RegexKomandeGraditelj("ITAP")
        .dodajBroj("oznaka")
        .dajUzorak();

    var matcher = uzorak.matcher(komanda);
    if (!matcher.matches()) {
      throw new NeispravnaKomandaGreska();
    }

    int oznaka = Integer.parseInt(matcher.group("oznaka"));

    Aranzman aranzman = agencija.dajAranzman(oznaka);

    if (aranzman == null) {
      System.out.println("Aranžman ne postoji.");
      return;
    }

    prikaziDetaljeAranzmana(aranzman);
  }

  private void prikaziDetaljeAranzmana(Aranzman a) {

    var formatDatuma = FormatDatuma.dajInstancu();

    System.out.printf("Oznaka: %d\n", a.oznaka());
    System.out.printf("Naziv: %s\n", a.naziv());
    System.out.printf("Program: %s\n", a.program().replace("\\n", "\n"));
    System.out.printf("Početni datum: %s\n", formatDatuma.formatirajDatum(a.pocetniDatum()));
    System.out.printf("Završni datum: %s\n", formatDatuma.formatirajDatum(a.zavrsniDatum()));
    System.out.printf("Vrijeme kretanja: %s\n",
        formatDatuma.formatirajVrijeme(a.vrijemeKretanja()));
    System.out.printf("Vrijeme povratka: %s\n",
        formatDatuma.formatirajVrijeme(a.vrijemePovratka()));
    System.out.printf("Cijena: %.2f\n", a.cijena());
    System.out.printf("Min broj putnika: %d\n", a.minBrojPutnika());
    System.out.printf("Max broj putnika: %d\n", a.maxBrojPutnika());
    System.out.printf("Broj noćenja: %d\n", a.brojNocenja());
    System.out.printf("Doplata za jednokrevetnu sobu: %.2f\n", a.doplataZaJednokrevetnuSobu());
    System.out.printf("Prijevoz: %s\n", a.prijevoz());
    System.out.printf("Broj doručka: %d\n", a.brojDorucka());
    System.out.printf("Broj ručkova: %d\n", a.brojRuckova());
    System.out.printf("Broj večera: %d\n", a.brojVecera());

  }

  private void obradiKomanduPregledRezervacijaAranzmana(String komanda) throws Exception {

    TuristickaAgencija agencija = TuristickaAgencija.dajInstancu();

    var uzorak = new RegexKomandeGraditelj("IRTA")
        .dodajBroj("oznaka")
        .dodajTekst("filter")
        .dajUzorak();

    var matcher = uzorak.matcher(komanda);
    if (!matcher.matches()) {
      throw new NeispravnaKomandaGreska();
    }

    int oznaka = Integer.parseInt(matcher.group("oznaka"));
    String filter = matcher.group("filter");

    List<Rezervacija> rezervacije = agencija.dajRezervacijeAranzmana(oznaka, filter);
    if (rezervacije == null) {
      System.out.println("Aranžman ne postoji.");
      return;
    }

    prikaziRezervacije(rezervacije);
  }

  private void prikaziRezervacije(List<Rezervacija> rezervacije) {

    var formatDatuma = FormatDatuma.dajInstancu();

    var tablicniIspis = new TablicniIspisGraditelj()
        .dodajStupac("Ime", 18)
        .dodajStupac("Prezime", 18)
        .dodajStupac("Datum i vrijeme", 24)
        .dodajStupac("Vrsta", 18)
        .napravi();

    tablicniIspis.ispisiZaglavlje();
    tablicniIspis.ispisi(rezervacije.stream()
        .map(e -> new String[]{
            e.ime(), e.prezime(),
            formatDatuma.formatirajDatumVrijeme(e.datumVrijeme()),
            e.vrsta()
        })
        .toList());

  }

  private String dajNazivKomande(String komanda) {
    int i = komanda.indexOf(' ');
    if (i == -1) {return komanda;}
    return komanda.substring(0, i);
  }

  private List<Aranzman> ucitajAranzmane(String putanjaAranzmani) throws IOException {

    String csv = Files.readString(Path.of(putanjaAranzmani));

    var csvCitac = new CsvCitac();
    csvCitac.ucitajCsv(csv);

    List<CsvRedak> redci = csvCitac.csvRedci();
    List<Aranzman> aranzmani = new ArrayList<>();

    for (CsvRedak redak : redci) {
      try {
        var aranzman = parsirajAranzman(redak);
        aranzmani.add(aranzman);
      } catch (CsvFormatGreska e) {
        EvidencijaGresaka.dajInstancu().evidentiraj(e);
      }
    }

    return aranzmani;
  }

  private Aranzman parsirajAranzman(CsvRedak csvRedak) throws CsvFormatGreska {

    if (csvRedak.brojElemenata() != 16) {
      String opis = String.format("Csv redak za aranžmane treba imati 16 elemenata, trenutno: %d!",
          csvRedak.brojElemenata());
      throw new CsvFormatGreska(opis, csvRedak);
    }

    var graditelj = new AranzmanGraditelj();

    int indeks = 0;
    graditelj.setOznaka(csvRedak.dajInt(indeks++))
        .setNaziv(csvRedak.dajString(indeks++))
        .setProgram(csvRedak.dajString(indeks++))
        .setPocetniDatum(csvRedak.dajDatum(indeks++))
        .setZavrsniDatum(csvRedak.dajDatum(indeks++))
        .setVrijemeKretanja(csvRedak.dajVrijeme(indeks++, null))
        .setVrijemePovratka(csvRedak.dajVrijeme(indeks++, null))
        .setCijena(csvRedak.dajFloat(indeks++))
        .setMinBrojPutnika(csvRedak.dajInt(indeks++))
        .setMaxBrojPutnika(csvRedak.dajInt(indeks++))
        .setBrojNocenja(csvRedak.dajInt(indeks++))
        .setDoplataZaJednokrevetnuSobu(csvRedak.dajFloat(indeks++, 0))
        .setPrijevoz(csvRedak.dajString(indeks++, null))
        .setBrojDorucka(csvRedak.dajInt(indeks++, 0))
        .setBrojRuckova(csvRedak.dajInt(indeks++, 0))
        .setBrojVecera(csvRedak.dajInt(indeks, 0));

    return graditelj.dajAranzman();
  }

  private List<Rezervacija> ucitajRezervacije(String putanjaRezervacije) throws IOException {

    String csv = Files.readString(Path.of(putanjaRezervacije));

    var csvCitac = new CsvCitac();
    csvCitac.ucitajCsv(csv);

    List<CsvRedak> redci = csvCitac.csvRedci();
    List<Rezervacija> rezervacije = new ArrayList<>();

    for (CsvRedak redak : redci) {
      try {
        var rezervacija = parsirajRezervaciju(redak);
        rezervacije.add(rezervacija);
      } catch (CsvFormatGreska e) {
        EvidencijaGresaka.dajInstancu().evidentiraj(e);
      }
    }

    return rezervacije;
  }

  private Rezervacija parsirajRezervaciju(CsvRedak csvRedak) throws CsvFormatGreska {

    if (csvRedak.brojElemenata() != 4) {
      String opis = String.format("Csv redak za rezervacije treba imati 4 elemenata, trenutno: %d!",
          csvRedak.brojElemenata());
      throw new CsvFormatGreska(opis, csvRedak);
    }

    var graditelj = new RezervacijaGraditelj();

    int indeks = 0;
    graditelj.setIme(csvRedak.dajString(indeks++))
        .setPrezime(csvRedak.dajString(indeks++))
        .setOznakaAranzmana(csvRedak.dajInt(indeks++))
        .setDatumVrijeme(csvRedak.dajDatumVrijeme(indeks));

    return graditelj.dajRezervaciju();
  }

}
