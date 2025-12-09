package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.lib.UcitavacPodatakaFacade;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.OtkazanaRezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.CitacOpcija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.FormatDatuma;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.tablicni_ispis.TablicniIspisGraditelj;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    var aranzmani = ucitajAranzmane(Path.of(konfig.putanjaAranzmani()));
    var rezervacije = ucitajRezervacije(Path.of(konfig.putanjaRezervacije()));

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

    if (komanda.isEmpty()) {
      return;
    }

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
        .napravi();

    tablicniIspis.ispisiZaglavlje();
    tablicniIspis.ispisi(aranzmani);

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
    System.out.printf("Početni datum: %s\n", formatDatuma.formatiraj(a.pocetniDatum()));
    System.out.printf("Završni datum: %s\n", formatDatuma.formatiraj(a.zavrsniDatum()));
    System.out.printf("Vrijeme kretanja: %s\n", formatDatuma.formatiraj(a.vrijemeKretanja()));
    System.out.printf("Vrijeme povratka: %s\n", formatDatuma.formatiraj(a.vrijemePovratka()));
    System.out.printf("Cijena: %.2f\n", a.cijena());
    System.out.printf("Min broj putnika: %d\n", a.minBrojPutnika());
    System.out.printf("Max broj putnika: %d\n", a.maxBrojPutnika());
    System.out.printf("Broj noćenja: %d\n", a.brojNocenja());
    System.out.printf("Doplata za jednokrevetnu sobu: %.2f\n", a.doplataZaJednokrevetnuSobu());
    System.out.printf("Prijevoz: %s\n", a.prijevoz() != null ? String.join(", ", a.prijevoz()) : "nema");
    System.out.printf("Broj doručka: %d\n", a.brojDorucka());
    System.out.printf("Broj ručkova: %d\n", a.brojRuckova());
    System.out.printf("Broj večera: %d\n", a.brojVecera());

  }

  private void obradiKomanduPregledRezervacijaAranzmana(String komanda) throws Exception {

    TuristickaAgencija agencija = TuristickaAgencija.dajInstancu();

    var uzorak = new RegexKomandeGraditelj("IRTA")
        .dodajBroj("oznaka")
        .dodajTekstOpcionalno("filter")
        .dajUzorak();

    var matcher = uzorak.matcher(komanda);
    if (!matcher.matches()) {
      String opis = "IRTA oznaka [PA|Č|O]";
      throw new NeispravnaKomandaGreska(opis);
    }

    int oznaka = Integer.parseInt(matcher.group("oznaka"));
    String filter = matcher.group("filter");
    if (filter == null) {filter = "PAČO";}

    boolean prikaziPrimljeneAktivne = filter.contains("PA");
    boolean prikaziNaCekanju = filter.contains("Č");
    boolean prikaziOtkazane = filter.contains("O");

    List<Rezervacija> rezervacije = agencija.dajRezervacijeAranzmana(oznaka,
        prikaziPrimljeneAktivne, prikaziNaCekanju, prikaziOtkazane);
    if (rezervacije == null) {
      System.out.println("Aranžman ne postoji.");
      return;
    }

    if (rezervacije.isEmpty()) {
      System.out.println("Nema rezervacija za prikaz.");
      return;
    }

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

    var tablicniIspis = new TablicniIspisGraditelj<Rezervacija>()
        .dodajStupac("Ime", 18, e -> e.korisnik().ime())
        .dodajStupac("Prezime", 18, e -> e.korisnik().prezime())
        .dodajStupac("Datum i vrijeme", 24, e -> formatDatuma.formatiraj(e.datumVrijeme()))
        .dodajStupac("Vrsta", 18, e -> e.vrsta())
        .dodajStupac("Datum vrijeme otkaza", 24, e -> {
          if (e instanceof OtkazanaRezervacija r) {
            return formatDatuma.formatiraj(r.datumVrijemeOtkaza());
          }
          return "";
        })
        .prikazujStupac(prikaziOtkazane)
        .napravi();

    tablicniIspis.ispisiZaglavlje();
    tablicniIspis.ispisi(rezervacije);

  }

  private void prikaziRezervacijeKorisnika(List<Rezervacija> rezervacije) {

    var formatDatuma = FormatDatuma.dajInstancu();
    var agencija = TuristickaAgencija.dajInstancu();

    var tablicniIspis = new TablicniIspisGraditelj<Rezervacija>()
        .dodajStupac("Datum i vrijeme", 24, e -> formatDatuma.formatiraj(e.datumVrijeme()))
        .dodajStupac("Oznaka aranžmana", 16, e -> Integer.toString(e.oznakaAranzmana()))
        .dodajStupac("Naziv aranžmana", 20, e -> agencija.dajAranzman(e.oznakaAranzmana()).naziv())
        .dodajStupac("Vrsta", 18, e -> e.vrsta())
        .napravi();

    tablicniIspis.ispisiZaglavlje();
    tablicniIspis.ispisi(rezervacije);

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

    var korisnik = new Korisnik(ime, prezime);

    KreatorRezervacije kreatorRezervacije = new KreatorPrimljeneRezervacije();
    Rezervacija rezervacija = kreatorRezervacije.napraviRezervaciju(korisnik, oznaka, datumVrijeme);

    try {
      agencija.zaprimiRezervaciju(rezervacija);
      System.out.println("Rezervacija je uspješno zaprimljena.");
    } catch (Exception e) {
      System.out.println("Dodavanje rezervacije nije uspjelo. " + e.getMessage());
    }

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

    try {
      agencija.otkaziRezervaciju(ime, prezime, oznaka);
      System.out.println("Rezervacija je uspješno otkazana.");
    } catch (Exception e) {
      System.out.println("Otkazivanje rezervacije nije uspjelo. " + e.getMessage());
    }

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

  private List<Aranzman> ucitajAranzmane(Path putanja) throws IOException {

    var ucitavacPodataka = new UcitavacPodatakaFacade();
    List<List<String>> csvRedci = ucitavacPodataka.ucitajAranzmane(putanja);

    List<Aranzman> aranzmani = new ArrayList<>();

    for (List<String> stupci : csvRedci) {
      try {
        Aranzman aranzman = parsirajAranzman(stupci);
        aranzmani.add(aranzman);
      } catch (Exception e) {
        EvidencijaGresaka.dajInstancu().evidentiraj(e);
      }
    }

    return aranzmani;
  }

  private Aranzman parsirajAranzman(List<String> stupci) throws Exception {

    if (stupci.size() != 16) {
      String opis = String.format("Broj stupaca aranžmana mora biti 16! Trenutno: %d", stupci.size());
      throw new Exception(opis);
    }

    int index = 0;
    String oznaka = stupci.get(index++);
    String naziv = stupci.get(index++);
    String program = stupci.get(index++);
    String pocetniDatum = stupci.get(index++);
    String zavrsniDatum = stupci.get(index++);
    String vrijemeKretanja = stupci.get(index++);
    String vrijemePovratka = stupci.get(index++);
    String cijena = stupci.get(index++);

    String minPutnika = stupci.get(index++);
    String maxPutnika = stupci.get(index++);
    String brojNocenja = stupci.get(index++);
    String doplataJednokrevetnaSoba = stupci.get(index++);
    String prijevoz = stupci.get(index++);
    String brojDorucka = stupci.get(index++);
    String brojRuckova = stupci.get(index++);
    String brojVecera = stupci.get(index);

    var formatDatuma = FormatDatuma.dajInstancu();
    AranzmanGraditelj graditelj = new AranzmanStvarniGraditelj();

    graditelj.napraviAranzman(Integer.parseInt(oznaka), naziv)
        .setProgram(program)
        .setPocetniDatum(formatDatuma.parsirajDatum(pocetniDatum))
        .setZavrsniDatum(formatDatuma.parsirajDatum(zavrsniDatum))
        .setCijena(Float.parseFloat(cijena))
        .setMinBrojPutnika(Integer.parseInt(minPutnika))
        .setMaxBrojPutnika(Integer.parseInt(maxPutnika));

    if (vrijemeKretanja != null) {
      graditelj.setVrijemeKretanja(formatDatuma.parsirajVrijeme(vrijemeKretanja));
    }

    if (vrijemePovratka != null) {
      graditelj.setVrijemePovratka(formatDatuma.parsirajVrijeme(vrijemePovratka));
    }

    if (brojNocenja != null) {
      graditelj.setBrojNocenja(Integer.parseInt(brojNocenja));
    }

    if (doplataJednokrevetnaSoba != null) {
      graditelj.setDoplataZaJednokrevetnuSobu(Float.parseFloat(doplataJednokrevetnaSoba));
    }

    if (prijevoz != null) {
      graditelj.setPrijevoz(parsirajPrijevozAranzmana(prijevoz));
    }

    if (brojDorucka != null) {
      graditelj.setBrojDorucka(Integer.parseInt(brojDorucka));
    }

    if (brojRuckova != null) {
      graditelj.setBrojRuckova(Integer.parseInt(brojRuckova));
    }

    if (brojVecera != null) {
      graditelj.setBrojVecera(Integer.parseInt(brojVecera));
    }

    return graditelj.dajAranzman();
  }

  private List<String> parsirajPrijevozAranzmana(String prijevozTekst) {
    if (prijevozTekst == null) {return null;}
    return Arrays.stream(prijevozTekst.split(";"))
        .map(e -> e.trim())
        .filter(e -> !e.isEmpty())
        .toList();
  }

  private List<Rezervacija> ucitajRezervacije(Path putanja) throws IOException {

    var ucitavacPodataka = new UcitavacPodatakaFacade();
    List<List<String>> csvRedci = ucitavacPodataka.ucitajRezervacije(putanja);

    List<Rezervacija> rezervacije = new ArrayList<>();

    for (List<String> stupci : csvRedci) {
      try {
        Rezervacija rezervacija = parsirajRezervaciju(stupci);
        rezervacije.add(rezervacija);
      } catch (Exception e) {
        EvidencijaGresaka.dajInstancu().evidentiraj(e);
      }
    }

    return rezervacije;
  }

  private Rezervacija parsirajRezervaciju(List<String> stupci) throws Exception {

    if (stupci.size() != 4) {
      String opis = String.format("Broj stupaca rezervacije mora biti 4! Trenutno: %d", stupci.size());
      throw new Exception(opis);
    }

    var formatDatuma = FormatDatuma.dajInstancu();

    int index = 0;
    String ime = stupci.get(index++);
    String prezime = stupci.get(index++);
    int oznaka = Integer.parseInt(stupci.get(index++));
    LocalDateTime datumVrijeme = formatDatuma.parsirajDatumVrijeme(stupci.get(index));

    var korisnik = new Korisnik(ime, prezime);

    KreatorRezervacije kreatorRezervacije = new KreatorPrimljeneRezervacije();
    return kreatorRezervacije.napraviRezervaciju(korisnik, oznaka, datumVrijeme);
  }

  // endregion

}
