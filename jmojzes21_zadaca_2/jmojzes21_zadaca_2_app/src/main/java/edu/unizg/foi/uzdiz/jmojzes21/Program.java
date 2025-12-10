package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaDRTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaIRO;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaIRTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaITAK;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaITAP;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaORTA;
import edu.unizg.foi.uzdiz.jmojzes21.lib.UcitavacPodatakaFacade;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.CitacOpcija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.FormatDatuma;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
  private TuristickaAgencija agencija;

  public void pokreni(String[] args) throws Exception {

    if (args.length == 0) {
      System.out.println("Potrebno je unijeti argumente za pokretanje!");
      System.out.println("Primjer: --ta [datoteka aranžmani] --rta [datoteka rezervacije]");
      return;
    }

    ucitajOpcije(args);

    var konfig = Konfiguracija.dajKonfiguraciju();

    List<Aranzman> aranzmani = ucitajAranzmane(Path.of(konfig.putanjaAranzmani()));
    List<Rezervacija> rezervacije = ucitajRezervacije(Path.of(konfig.putanjaRezervacije()));

    agencija = new TuristickaAgencija();
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

  // endregion

  // region Komande korisnika

  private void obradiKomandeKorisnika() {

    try (var skener = new Scanner(System.in)) {
      while (zaprimajKomandeKorisnika) {
        String linija = skener.nextLine();
        if (linija == null) {return;}

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

    if (komanda.isEmpty()) {
      return;
    }

    String naziv = dajNazivKomande(komanda).toUpperCase();

    switch (naziv) {
      case "ITAK": {
        var k = new KomandaITAK(agencija);
        k.obradiKomanduPregledAranzmana(komanda);
        break;
      }
      case "ITAP": {
        var k = new KomandaITAP(agencija);
        k.obradiKomanduDetaljiAranzmana(komanda);
        break;
      }
      case "IRTA": {
        var k = new KomandaIRTA(agencija);
        k.obradiKomanduPregledRezervacijaAranzmana(komanda);
        break;
      }
      case "IRO": {
        var k = new KomandaIRO(agencija);
        k.obradiKomanduPregledRezervacijaKorisnika(komanda);
        break;
      }
      case "DRTA": {
        var k = new KomandaDRTA(agencija);
        k.obradiKomanduDodavanjeRezervacije(komanda);
        break;
      }
      case "ORTA": {
        var k = new KomandaORTA(agencija);
        k.obradiKomanduOtkaziRezervaciju(komanda);
        break;
      }
      case "Q": {
        zaprimajKomandeKorisnika = false;
        break;
      }
      default: {
        pregledKomandi();
        break;
      }
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
    LocalDateTime vrijemePrijema = formatDatuma.parsirajDatumVrijeme(stupci.get(index));

    var korisnik = new Korisnik(ime, prezime);
    return new Rezervacija(korisnik, oznaka, vrijemePrijema);
  }

  // endregion

}
