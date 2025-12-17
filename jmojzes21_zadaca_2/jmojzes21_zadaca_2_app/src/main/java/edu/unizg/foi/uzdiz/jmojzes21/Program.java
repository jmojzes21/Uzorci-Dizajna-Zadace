package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaBP;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaDRTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaIP;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaIRO;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaIRTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaITAK;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaITAP;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaITAS;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaORTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaOTA;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaPOTI;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaUP;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.CitacOpcija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Program {

  // region Početak

  public static void main(String[] args) {

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

    agencija = new TuristickaAgencija();

    Map<String, String> opcije = ucitajOpcije(args);

    String putanjaAranzmani = opcije.get("--ta");
    String putanjaRezervacije = opcije.get("--rta");

    if (putanjaAranzmani != null) {
      try {
        var aranzmanRepo = new AranzmanRepozitorij();
        List<Aranzman> aranzmani = aranzmanRepo.ucitajAranzmane(Path.of(putanjaAranzmani));
        agencija.dodajAranzmane(aranzmani);
      } catch (Exception e) {
        System.out.println("Učitavanje aranžmana nije uspjelo!");
        System.out.println(e.getMessage());
      }
    }

    if (putanjaRezervacije != null) {
      try {
        var rezervacijaRepo = new RezervacijaRepozitorij();
        List<Rezervacija> rezervacije = rezervacijaRepo.ucitajRezervacije(Path.of(putanjaRezervacije));
        agencija.zaprimiRezervacije(rezervacije);
      } catch (Exception e) {
        System.out.println("Učitavanje rezervacija nije uspjelo!");
        System.out.println(e.getMessage());
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

    String[] dijelovi = razdvojiKomandu(komanda);
    String naziv = dijelovi[0].toUpperCase();
    String args = dijelovi[1];

    ispisiKomandu(komanda);

    switch (naziv) {
      case "ITAK": {
        var k = new KomandaITAK(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "ITAP": {
        var k = new KomandaITAP(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "IRTA": {
        var k = new KomandaIRTA(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "IRO": {
        var k = new KomandaIRO(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "DRTA": {
        var k = new KomandaDRTA(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "ORTA": {
        var k = new KomandaORTA(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "OTA": {
        var k = new KomandaOTA(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "IP": {
        var k = new KomandaIP();
        k.obradiKomandu(args);
        break;
      }
      case "BP": {
        var k = new KomandaBP(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "UP": {
        var k = new KomandaUP(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "ITAS": {
        var k = new KomandaITAS(agencija);
        k.obradiKomandu(args);
        break;
      }
      case "POTI": {
        var k = new KomandaPOTI();
        k.obradiKomandu(args);
        break;
      }
      case "Q": {
        zaprimajKomandeKorisnika = false;
        break;
      }
      default: {
        System.out.println("Nepoznata komanda.");
        pregledKomandi();
        break;
      }
    }

  }

  private void pregledKomandi() {
    System.out.println("Komande:");
    System.out.println("ITAK - Pregled svih aranžmana, ITAK [od do]");
    System.out.println("ITAP - Pregled pojedinog aranžmana, ITAP oznaka");
    System.out.println("IRTA - Pregled rezervacija za aranžman, IRTA oznaka [PA|Č|O|OD]");
    System.out.println("IRO  - Pregled rezervacija za korisnika, IRO ime prezime");
    System.out.println("DRTA - Dodaj rezervaciju, DRTA ime prezime oznaka datum vrijeme");
    System.out.println("ORTA - Otkaži rezervacije, ORTA ime prezime oznaka");
    System.out.println("OTA  - Otkaži turistički aranžman, OTA oznaka");
    System.out.println("IP   - Postavi način sortiranja podataka, IP [N|S]");
    System.out.println("BP   - Brisanje podataka, BP [A|R]");
    System.out.println("UP   - Učitavanje podataka, UP [A|R] nazivDatoteke");
    System.out.println("ITAS - Ispis statističkih podataka, ITAS [od do]");
    System.out.println("POTI - Postavi postavke tabličnog ispisa, POTI naziv vrijednost");
    System.out.println("Q    - Izlaz");
  }

  private void ispisiKomandu(String komanda) {
    System.out.println("Izvrši komandu: " + komanda);
  }

  // endregion

  // region Pomoćne metode

  private String[] razdvojiKomandu(String komanda) {
    int i = komanda.indexOf(' ');
    if (i == -1) {
      return new String[]{komanda, ""};
    }

    String naziv = komanda.substring(0, i);
    String args = komanda.substring(i + 1).trim();
    return new String[]{naziv, args};
  }

  private Map<String, String> ucitajOpcije(String[] args) throws Exception {

    var citacOpcija = new CitacOpcija();
    citacOpcija.ucitajOpcije(args);

    var opcije = citacOpcija.opcije();
    Map<String, String> rezultat = new HashMap<>();

    for (var opcija : opcije.keySet()) {
      switch (opcija) {
        case "--ta":
          rezultat.put("--ta", citacOpcija.dajVrijednost(opcija));
          break;
        case "--rta":
          rezultat.put("--rta", citacOpcija.dajVrijednost(opcija));
          break;
        default:
          throw new Exception(String.format("Nepoznata opcija %s", opcija));
      }
    }

    return rezultat;
  }

  // endregion

}
