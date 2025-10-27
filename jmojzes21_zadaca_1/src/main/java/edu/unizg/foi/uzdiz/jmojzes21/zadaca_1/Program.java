package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.CitacOpcija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv.CsvCitac;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv.CsvFormatGreska;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv.CsvRedak;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Program {

  public static void main(String[] args) throws IOException {
    var program = new Program();
    program.pokreni(args);
  }

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
