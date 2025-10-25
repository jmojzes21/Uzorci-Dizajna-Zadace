package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.pomocnici.CitateljOpcija;

public class Program {

  public static void main(String[] args) {
    var program = new Program();
    program.pokreni(args);
  }

  public void pokreni(String[] args) {

    var konfig = Konfiguracija.dajKonfiguraciju();

    var citateljOpcija = new CitateljOpcija();
    citateljOpcija.ucitajOpcije(args);

    konfig.setPutanjaAranzmani(citateljOpcija.dajOpciju("ta"));
    konfig.setPutanjaRezervacije(citateljOpcija.dajOpciju("rta"));

  }

}
