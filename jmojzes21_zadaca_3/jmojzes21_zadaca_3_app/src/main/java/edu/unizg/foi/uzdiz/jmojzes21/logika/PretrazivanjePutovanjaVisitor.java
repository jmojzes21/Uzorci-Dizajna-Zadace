package edu.unizg.foi.uzdiz.jmojzes21.logika;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import java.util.ArrayList;
import java.util.List;

public class PretrazivanjePutovanjaVisitor implements PutovanjeVisitor {

  private final List<Aranzman> aranzmani = new ArrayList<>();
  private final List<Rezervacija> rezervacije = new ArrayList<>();

  private final String rijec;

  public PretrazivanjePutovanjaVisitor(String rijec) {
    this.rijec = rijec;
  }

  @Override
  public void posjeti(Aranzman aranzman) {
    String naziv = aranzman.naziv();
    String opis = aranzman.program();
    if (opis == null) {opis = "";}

    if (naziv.contains(rijec) || opis.contains(rijec)) {
      aranzmani.add(aranzman);
    }
  }

  @Override
  public void posjeti(Rezervacija rezervacija) {
    String ime = rezervacija.korisnik().ime();
    String prezime = rezervacija.korisnik().prezime();

    if (ime.contains(rijec) || prezime.contains(rijec)) {
      rezervacije.add(rezervacija);
    }
  }

  public List<Aranzman> aranzmani() {return aranzmani;}

  public List<Rezervacija> rezervacije() {return rezervacije;}

}
