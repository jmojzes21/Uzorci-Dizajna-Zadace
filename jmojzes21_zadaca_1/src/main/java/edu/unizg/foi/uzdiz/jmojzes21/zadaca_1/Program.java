package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.CitacOpcija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.csv.CsvCitac;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    String rezervacijeCsv = Files.readString(Path.of(konfig.putanjaRezervacije()));

    var csvCitac = new CsvCitac();
    csvCitac.ucitajCsv(rezervacijeCsv);

  }

}
