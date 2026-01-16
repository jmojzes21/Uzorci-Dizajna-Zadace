package edu.unizg.foi.uzdiz.jmojzes21.modeli;

import edu.unizg.foi.uzdiz.jmojzes21.logika.visitor.PutovanjeVisitor;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.RezervacijaNova;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.RezervacijaStanje;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class Rezervacija extends PutovanjeComponent implements RezervacijaObserver {

  public enum StanjeId {
    nova, primljena, aktivna, naCekanju, odgodjena, otkazana
  }

  private Korisnik korisnik;

  private int oznakaAranzmana;
  private LocalDateTime vrijemePrijema;
  private LocalDateTime vrijemeOtkaza;

  private RezervacijaStanje stanje;

  public Rezervacija(Korisnik korisnik, int oznakaAranzmana, LocalDateTime vrijemePrijema) {
    this.korisnik = korisnik;
    this.oznakaAranzmana = oznakaAranzmana;
    this.vrijemePrijema = vrijemePrijema;
    vrijemeOtkaza = null;

    stanje = new RezervacijaNova();
  }

  public void zaprimi() {
    stanje.zaprimi(this);
  }

  public void aktiviraj() {
    stanje.aktiviraj(this);
  }

  public void staviNaCekanje() {
    stanje.staviNaCekanje(this);
  }

  public void odgodi() {
    stanje.odgodi(this);
  }

  public void otkazi() {
    stanje.otkazi(this);
  }

  @Override
  public void kadaRezervacijaPostalaAktivna(Rezervacija aktivirana) {
    if (oznakaAranzmana == aktivirana.oznakaAranzmana) {return;}
    stanje.kadaRezervacijaPostalaAktivna(this, aktivirana);
  }

  @Override
  public void kadaRezervacijaNijeViseAktivna(Rezervacija rezervacija) {
    if (oznakaAranzmana == rezervacija.oznakaAranzmana) {return;}
    stanje.kadaRezervacijaNijeViseAktivna(this, rezervacija);
  }

  @Override
  public void prihvati(PutovanjeVisitor visitor) {
    visitor.posjeti(this);
  }

  public RezervacijaStanje stanje() {
    return stanje;
  }

  public void postaviStanje(RezervacijaStanje stanje) {
    this.stanje = stanje;
    dajAranzman().obavijestiPromjenuStanjaRezervacije(this);
  }

  public StanjeId idStanja() {
    return stanje.dajId();
  }

  public String nazivStanja() {
    return stanje.dajNaziv();
  }

  public boolean jePrimljena() {
    return idStanja() == StanjeId.primljena;
  }

  public boolean jeAktivna() {
    return idStanja() == StanjeId.aktivna;
  }

  public boolean jeNaCekanju() {
    return idStanja() == StanjeId.naCekanju;
  }

  public boolean jeOtkazana() {
    return idStanja() == StanjeId.otkazana;
  }

  public boolean jeOdgodjena() {
    return idStanja() == StanjeId.odgodjena;
  }

  public Aranzman dajAranzman() {
    return (Aranzman) dajRoditelja();
  }

  /**
   * Sortira rezervacije prema vremenu prijema.
   *
   * @param rezervacije rezervacije
   * @param uzlazno     ako je true sortira uzlazno, inače sortira silazno
   * @return sortirane rezervacije
   */
  public static List<Rezervacija> sortiraj(List<Rezervacija> rezervacije, boolean uzlazno) {
    var comparator = Comparator.comparing(Rezervacija::vrijemePrijema);
    if (!uzlazno) {comparator = comparator.reversed();}
    return rezervacije.stream().sorted(comparator).toList();
  }

  // region Metode za dohvaćanje i postavljanje atributa

  public Korisnik korisnik() {return korisnik;}

  public int oznakaAranzmana() {return oznakaAranzmana;}

  public LocalDateTime vrijemePrijema() {return vrijemePrijema;}

  public LocalDateTime vrijemeOtkaza() {return vrijemeOtkaza;}

  public void setKorisnik(Korisnik korisnik) {this.korisnik = korisnik;}

  public void setOznakaAranzmana(int oznakaAranzmana) {this.oznakaAranzmana = oznakaAranzmana;}

  public void setVrijemePrijema(LocalDateTime vrijemePrijema) {this.vrijemePrijema = vrijemePrijema;}

  public void setVrijemeOtkaza(LocalDateTime vrijemeOtkaza) {this.vrijemeOtkaza = vrijemeOtkaza;}

  // endregion

}
