package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.CitateljOpcija;
import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.CsvCitatelj;
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

    var citateljOpcija = new CitateljOpcija();
    citateljOpcija.ucitajOpcije(args);

    konfig.setPutanjaAranzmani(citateljOpcija.dajOpciju("ta"));
    konfig.setPutanjaRezervacije(citateljOpcija.dajOpciju("rta"));

    String rezervacijeCsv = Files.readString(Path.of(konfig.putanjaRezervacije()));

    var csvCitatelj = new CsvCitatelj();
    csvCitatelj.ucitajCsv(rezervacijeCsv);
    
  }

}
