package edu.unizg.foi.uzdiz.jmojzes21.logika.memento;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Korisnik;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.Rezervacija;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.AranzmanStanje;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StanjeAranzmanaMemento {

  private final int oznaka;
  private final String naziv;
  private final String program;

  private final LocalDate pocetniDatum;
  private final LocalDate zavrsniDatum;
  private final LocalTime vrijemeKretanja;
  private final LocalTime vrijemePovratka;

  private final float cijena;
  private final int minBrojPutnika;
  private final int maxBrojPutnika;

  private final int brojNocenja;
  private final int brojDorucka;
  private final int brojRuckova;
  private final int brojVecera;
  private final float doplataJKS;
  private final List<String> prijevoz;

  private final List<Korisnik> pretplaceniKorisnici;

  private final AranzmanStanje stanje;
  private final List<Rezervacija> rezervacije;

  public StanjeAranzmanaMemento(Aranzman aranzman) {

    oznaka = aranzman.oznaka();
    naziv = aranzman.naziv();
    program = aranzman.program();

    pocetniDatum = aranzman.pocetniDatum();
    zavrsniDatum = aranzman.zavrsniDatum();
    vrijemeKretanja = aranzman.vrijemeKretanja();
    vrijemePovratka = aranzman.vrijemePovratka();

    cijena = aranzman.cijena();
    minBrojPutnika = aranzman.minBrojPutnika();
    maxBrojPutnika = aranzman.maxBrojPutnika();

    brojNocenja = aranzman.brojNocenja();
    brojDorucka = aranzman.brojDorucka();
    brojRuckova = aranzman.brojRuckova();
    brojVecera = aranzman.brojVecera();
    doplataJKS = aranzman.doplataJKS();
    prijevoz = new ArrayList<>(aranzman.prijevoz());

    pretplaceniKorisnici = aranzman.dajPretplaceneKorisnike().stream().map(e -> e.kopiraj()).toList();

    stanje = aranzman.stanje();
    rezervacije = aranzman.rezervacije().stream().map(e -> e.kopiraj()).toList();
  }

  public int oznaka() {return oznaka;}

  public String naziv() {return naziv;}

  public String program() {return program;}

  public LocalDate pocetniDatum() {return pocetniDatum;}

  public LocalDate zavrsniDatum() {return zavrsniDatum;}

  public LocalTime vrijemeKretanja() {return vrijemeKretanja;}

  public LocalTime vrijemePovratka() {return vrijemePovratka;}

  public float cijena() {return cijena;}

  public int minBrojPutnika() {return minBrojPutnika;}

  public int maxBrojPutnika() {return maxBrojPutnika;}

  public int brojNocenja() {return brojNocenja;}

  public int brojDorucka() {return brojDorucka;}

  public int brojRuckova() {return brojRuckova;}

  public int brojVecera() {return brojVecera;}

  public float doplataJKS() {return doplataJKS;}

  public List<String> prijevoz() {return prijevoz;}

  public List<Korisnik> pretplaceniKorisnici() {return pretplaceniKorisnici;}

  public AranzmanStanje stanje() {return stanje;}

  public List<Rezervacija> rezervacije() {return rezervacije;}

}
