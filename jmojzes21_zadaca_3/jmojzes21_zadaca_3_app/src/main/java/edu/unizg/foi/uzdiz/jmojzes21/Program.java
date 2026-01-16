package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.komande.IKomanda;
import edu.unizg.foi.uzdiz.jmojzes21.komande.KomandaKreator;
import edu.unizg.foi.uzdiz.jmojzes21.logika.UpravljanjeRezervacijamaJDRStrategy;
import edu.unizg.foi.uzdiz.jmojzes21.logika.UpravljanjeRezervacijamaNullStrategy;
import edu.unizg.foi.uzdiz.jmojzes21.logika.UpravljanjeRezervacijamaStrategy;
import edu.unizg.foi.uzdiz.jmojzes21.logika.UpravljanjeRezervacijamaVDRStrategy;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.CitacOpcija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NepoznataKomanda;
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
    boolean jdr = opcije.containsKey("--jdr");
    boolean vdr = opcije.containsKey("--vdr");

    var upravljanjeRezervacijama = dajUpravljanjeRezervacijamaStrategy(jdr, vdr);
    agencija.setUpravljanjeRezervacijama(upravljanjeRezervacijama);

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

  private UpravljanjeRezervacijamaStrategy dajUpravljanjeRezervacijamaStrategy(boolean jdr, boolean vdr) {
    if (jdr) {return new UpravljanjeRezervacijamaJDRStrategy();}
    if (vdr) {return new UpravljanjeRezervacijamaVDRStrategy();}
    return new UpravljanjeRezervacijamaNullStrategy();
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
        } catch (NepoznataKomanda e) {
          System.out.println(e.getMessage() + " Komande:");
          pregledKomandi();
        } catch (NeispravnaKomandaGreska e) {
          System.out.println("Neispravna komanda! Ispravan format: " + e.getMessage());
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  private void obradiKomanduKorisnika(String komandaTekst) throws Exception {

    if (komandaTekst.isEmpty()) {
      return;
    }

    String[] dijelovi = razdvojiKomandu(komandaTekst);
    String naziv = dijelovi[0].toUpperCase();
    String args = dijelovi[1];

    ispisiKomandu(komandaTekst);

    if (naziv.equals("Q")) {
      zaprimajKomandeKorisnika = false;
      return;
    }

    IKomanda komanda = KomandaKreator.parsirajKomandu(naziv, args);
    komanda.izvrsi(agencija);

  }

  private void pregledKomandi() {
    System.out.println("ITAK [od do] - Pregled svih aranžmana");
    System.out.println("ITAP oznaka - Pregled pojedinog aranžmana");
    System.out.println("IRTA oznaka [PA|Č|O|OD] - Pregled rezervacija za aranžman");
    System.out.println("IRO ime prezime - Pregled rezervacija za korisnika");
    System.out.println("DRTA ime prezime oznaka datum vrijeme - Dodaj rezervaciju");
    System.out.println("ORTA ime prezime oznaka - Otkaži rezervacije");
    System.out.println("OTA oznaka - Otkaži turistički aranžman");
    System.out.println("IP [N|S] - Postavi način sortiranja podataka");
    System.out.println("BP [A|R] - Brisanje podataka");
    System.out.println("UP [A|R] nazivDatoteke - Učitavanje podataka");
    System.out.println("ITAS [od do] - Ispis statističkih podataka");
    System.out.println("POTI naziv vrijednost - Postavi postavke tabličnog ispisa");
    System.out.println("PPTAR [A|R] riječ - Pretraživanje aranžmana i rezervacija");
    System.out.println("PTAR ime prezime oznaka - Pretplata za informacije o promjenama");
    System.out.println("UPTAR [ime prezime oznaka] | [oznaka] - Ukidanje pretplata za informacije o promjenama");
    System.out.println("PSTAR oznaka - Pohranjivanje stanja aranžmana");
    System.out.println("VSTAR oznaka - Vraćanje stanja aranžmana");
    System.out.println("Q - Izlaz");
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
        case "--rta":
          rezultat.put(opcija, citacOpcija.dajVrijednost(opcija));
          break;
        case "--jdr":
        case "--vdr":
          rezultat.put(opcija, null);
          break;
        default:
          throw new Exception(String.format("Nepoznata opcija %s", opcija));
      }
    }

    if (rezultat.containsKey("--jdr") && rezultat.containsKey("--vdr")) {
      throw new Exception("Nije moguće koristiti --jdr i --vdr istovremeno!");
    }

    return rezultat;
  }

  // endregion

}
