package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.OtkazanaRezervacija;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Program {

  // region Početak

  public static void main(String[] args) {
    Locale.setDefault(Locale.ENGLISH);
    var program = new Program();

    try {
      program.pokreni(args);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private boolean zaprimajKomandeKorisnika = true;

  public void pokreni(String[] args) throws Exception {

    if (args.length == 0) {
      System.out.println("Potrebno je unijeti argumente za pokretanje!");
      System.out.println("Primjer: --ta [datoteka aranžmani] --rta [datoteka rezervacije]");
      return;
    }

    ucitajOpcije(args);

    var konfig = Konfiguracija.dajKonfiguraciju();

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
        } catch (NeispravnaKomandaGreska e) {
          System.out.println("Neispravan format komande! Ispravan format: " + e.getMessage());
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  private void obradiKomanduKorisnika(String komanda) throws Exception {

    Map<String, String> alternativeKomandi = Map.ofEntries(
        Map.entry("a", "ITAK"),
        Map.entry("da", "ITAP"),
        Map.entry("ra", "IRTA"),
        Map.entry("rk", "IRO"),
        Map.entry("dr", "DRTA"),
        Map.entry("or", "ORTA")
    );

    String naziv = dajNazivKomande(komanda);

    for (var e : alternativeKomandi.entrySet()) {
      if (naziv.equals(e.getKey())) {
        naziv = e.getValue();
        komanda = komanda.replaceFirst(e.getKey(), e.getValue());
      }
    }

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
      case "IRO":
        obradiKomanduPregledRezervacijaKorisnika(komanda);
        break;
      case "DRTA":
        obradiKomanduDodavanjeRezervacije(komanda);
        break;
      case "ORTA":
        obradiKomanduOtkaziRezervaciju(komanda);
        break;
      case "Q":
        zaprimajKomandeKorisnika = false;
        break;
      default:
        System.out.println("Nepoznata komanda.");
        pregledKomandi();
        break;
    }

  }

  private void pregledKomandi() {
    System.out.println("Komande:");
    System.out.println("  ITAK - Pregled svih aranžmana");
    System.out.println("  ITAP - Pregled pojedinog aranžmana");
    System.out.println("  IRTA - Pregled rezervacija za aranžman");
    System.out.println("  IRO  - Pregled rezervacija za korisnika");
    System.out.println("  DRTA - Dodaj rezervaciju");
    System.out.println("  ORTA - Otkaži rezervacije");
    System.out.println("  Q    - Izlaz");
  }

  // endregion

  // region Komande korisnika

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
      String opis = "ITAP oznaka";
      throw new NeispravnaKomandaGreska(opis);
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
      String opis = "IRTA oznaka [PA|Č|O]";
      throw new NeispravnaKomandaGreska(opis);
    }

    int oznaka = Integer.parseInt(matcher.group("oznaka"));
    String filter = matcher.group("filter");

    List<Rezervacija> rezervacije = agencija.dajRezervacijeAranzmana(oznaka, filter);
    if (rezervacije == null) {
      System.out.println("Aranžman ne postoji.");
      return;
    }

    if (rezervacije.isEmpty()) {
      System.out.println("Nema rezervacija za prikaz.");
      return;
    }

    boolean prikaziOtkazane = filter.contains("O");
    prikaziRezervacije(rezervacije, prikaziOtkazane);
  }

  private void obradiKomanduPregledRezervacijaKorisnika(String komanda) throws Exception {

    TuristickaAgencija agencija = TuristickaAgencija.dajInstancu();

    var uzorak = new RegexKomandeGraditelj("IRO")
        .dodajTekst("ime")
        .dodajTekst("prezime")
        .dajUzorak();

    var matcher = uzorak.matcher(komanda);
    if (!matcher.matches()) {
      String opis = "IRO ime prezime";
      throw new NeispravnaKomandaGreska(opis);
    }

    String ime = matcher.group("ime");
    String prezime = matcher.group("prezime");

    List<Rezervacija> rezervacije = agencija.dajRezervacijeKorisnika(ime, prezime);
    if (rezervacije.isEmpty()) {
      System.out.println("Korisnik nema rezervacija.");
      return;
    }

    prikaziRezervacijeKorisnika(rezervacije);
  }

  private void prikaziRezervacije(List<Rezervacija> rezervacije, boolean prikaziOtkazane) {

    var formatDatuma = FormatDatuma.dajInstancu();

    var tablicniIspis = new TablicniIspisGraditelj()
        .dodajStupac("Ime", 18)
        .dodajStupac("Prezime", 18)
        .dodajStupac("Datum i vrijeme", 24)
        .dodajStupac("Vrsta", 18)
        .dodajStupac("Datum vrijeme otkaza", 24)
        .prikazujStupac(prikaziOtkazane)
        .napravi();

    tablicniIspis.ispisiZaglavlje();
    tablicniIspis.ispisi(rezervacije.stream()
        .map(e -> {
          String datumVrijmeOtkaza = "";
          if (prikaziOtkazane && e instanceof OtkazanaRezervacija) {
            datumVrijmeOtkaza = formatDatuma.formatirajDatumVrijeme(
                ((OtkazanaRezervacija) e).datumVrijemeOtkaza());
          }
          return new String[]{
              e.ime(), e.prezime(),
              formatDatuma.formatirajDatumVrijeme(e.datumVrijeme()),
              e.vrsta(), datumVrijmeOtkaza
          };
        })
        .toList());

  }

  private void prikaziRezervacijeKorisnika(List<Rezervacija> rezervacije) {

    var formatDatuma = FormatDatuma.dajInstancu();
    var agencija = TuristickaAgencija.dajInstancu();

    var tablicniIspis = new TablicniIspisGraditelj()
        .dodajStupac("Datum i vrijeme", 24)
        .dodajStupac("Oznaka aranžmana", 16)
        .dodajStupac("Naziv aranžmana", 20)
        .dodajStupac("Vrsta", 18)
        .napravi();

    tablicniIspis.ispisiZaglavlje();
    tablicniIspis.ispisi(rezervacije.stream()
        .map(e -> {
          String nazivAranzmana = agencija.dajAranzman(e.oznakaAranzmana()).naziv();
          return new String[]{
              formatDatuma.formatirajDatumVrijeme(e.datumVrijeme()),
              Integer.toString(e.oznakaAranzmana()), nazivAranzmana, e.vrsta()
          };
        })
        .toList());

  }

  private void obradiKomanduDodavanjeRezervacije(String komanda) throws Exception {

    TuristickaAgencija agencija = TuristickaAgencija.dajInstancu();
    FormatDatuma formatDatuma = FormatDatuma.dajInstancu();

    var uzorak = new RegexKomandeGraditelj("DRTA")
        .dodajTekst("ime")
        .dodajTekst("prezime")
        .dodajBroj("oznaka")
        .dodajDatum("datum")
        .dodajVrijeme("vrijeme")
        .dajUzorak();

    var matcher = uzorak.matcher(komanda);
    if (!matcher.matches()) {
      String opis = "DRTA ime prezime oznaka datum vrijeme";
      throw new NeispravnaKomandaGreska(opis);
    }

    String ime = matcher.group("ime");
    String prezime = matcher.group("prezime");
    int oznaka = Integer.parseInt(matcher.group("oznaka"));
    String datum = matcher.group("datum");
    String vrijeme = matcher.group("vrijeme");
    LocalDateTime datumVrijeme = formatDatuma.parsirajDatumVrijeme(datum, vrijeme);

    KreatorRezervacije kreatorRezervacije = new KreatorPrimljeneRezervacije();
    Rezervacija rezervacija = kreatorRezervacije.napraviRezervaciju(ime, prezime, oznaka, datumVrijeme);

    agencija.zaprimiRezervaciju(rezervacija);
    System.out.println("Rezervacija je uspješno zaprimljena.");

  }

  private void obradiKomanduOtkaziRezervaciju(String komanda) throws Exception {

    TuristickaAgencija agencija = TuristickaAgencija.dajInstancu();
    FormatDatuma formatDatuma = FormatDatuma.dajInstancu();

    var uzorak = new RegexKomandeGraditelj("ORTA")
        .dodajTekst("ime")
        .dodajTekst("prezime")
        .dodajBroj("oznaka")
        .dajUzorak();

    var matcher = uzorak.matcher(komanda);
    if (!matcher.matches()) {
      String opis = "ORTA ime prezime oznaka";
      throw new NeispravnaKomandaGreska(opis);
    }

    String ime = matcher.group("ime");
    String prezime = matcher.group("prezime");
    int oznaka = Integer.parseInt(matcher.group("oznaka"));

    agencija.otkaziRezervaciju(ime, prezime, oznaka);
    System.out.println("Rezervacija je uspješno otkazana.");

  }

  // endregion

  // region Pomoćne metode

  private String dajNazivKomande(String komanda) {
    int i = komanda.indexOf(' ');
    if (i == -1) {return komanda;}
    return komanda.substring(0, i);
  }

  private void ucitajOpcije(String[] args) throws Exception {

    var konfig = Konfiguracija.dajKonfiguraciju();

    var citacOpcija = new CitacOpcija();
    citacOpcija.ucitajOpcije(args);

    var opcije = citacOpcija.opcije();

    for (var opcija : opcije.keySet()) {
      switch (opcija) {
        case "--ta":
          konfig.setPutanjaAranzmani(citacOpcija.dajVrijednost(opcija));
          break;
        case "--rta":
          konfig.setPutanjaRezervacije(citacOpcija.dajVrijednost(opcija));
          break;
        default:
          throw new Exception(String.format("Nepoznata opcija %s!", opcija));
      }
    }

  }

  // endregion

  // region Učitavanje podataka

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
    int oznaka = csvRedak.dajInt(indeks++);
    String naziv = csvRedak.dajString(indeks++);

    graditelj.napraviAranzman(oznaka, naziv)
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

    int indeks = 0;
    String ime = csvRedak.dajString(indeks++);
    String prezime = csvRedak.dajString(indeks++);
    int oznaka = csvRedak.dajInt(indeks++);
    LocalDateTime datumVrijeme = csvRedak.dajDatumVrijeme(indeks);

    KreatorRezervacije kreatorRezervacije = new KreatorPrimljeneRezervacije();

    return kreatorRezervacije.napraviRezervaciju(ime, prezime, oznaka, datumVrijeme);
  }

  // endregion

}
