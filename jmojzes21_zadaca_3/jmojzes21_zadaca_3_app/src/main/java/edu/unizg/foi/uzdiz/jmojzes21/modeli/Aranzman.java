package edu.unizg.foi.uzdiz.jmojzes21.modeli;

import edu.unizg.foi.uzdiz.jmojzes21.logika.memento.StanjeAranzmanaMemento;
import edu.unizg.foi.uzdiz.jmojzes21.logika.visitor.PutovanjeVisitor;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.AranzmanOtkazan;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.AranzmanStanje;
import edu.unizg.foi.uzdiz.jmojzes21.modeli.stanja.AranzmanUPripremi;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Turistički aranžman.
 */
public class Aranzman extends PutovanjeComposite implements RezervacijaSubject, RezervacijaObserver {

  public enum StanjeId {
    uPripremi, aktivan, popunjen, otkazan
  }

  private int oznaka;
  private String naziv;

  private String program;
  private LocalDate pocetniDatum;
  private LocalDate zavrsniDatum;

  private LocalTime vrijemeKretanja;
  private LocalTime vrijemePovratka;

  private float cijena;

  private int minBrojPutnika;
  private int maxBrojPutnika;

  private int brojNocenja;
  private int brojDorucka;
  private int brojRuckova;
  private int brojVecera;

  /**
   * Doplata za jedno krevetnu sobu
   */
  private float doplataJKS;
  private List<String> prijevoz;

  private AranzmanStanje stanje;

  private final List<RezervacijaObserver> promatraci = new ArrayList<>();

  public Aranzman(int oznaka, String naziv) {
    super();
    this.oznaka = oznaka;
    this.naziv = naziv;
    stanje = new AranzmanUPripremi();
  }

  @Override
  public void dodaj(PutovanjeComponent r) {
    if (!(r instanceof Rezervacija)) {
      throw new RuntimeException("Nije moguće dodati " + r.getClass().getName() + " u turistički aranžman!");
    }
    r.postaviRoditelja(this);
    djeca.add(r);
  }

  @Override
  public void obrisi(PutovanjeComponent r) {
    djeca.remove(r);
  }

  @Override
  public void obrisiSve() {
    djeca.clear();
    postaviStanje(new AranzmanUPripremi());
  }

  public void zaprimiRezervaciju(Rezervacija rezervacija) {
    stanje.zaprimiRezervaciju(this, rezervacija);
  }

  public void otkaziRezervaciju(Korisnik korisnik) {
    stanje.otkaziRezervaciju(this, korisnik);
  }

  public void aktivirajRezervaciju(Rezervacija rezervacija) {
    if (rezervacija.oznakaAranzmana() != oznaka) {
      throw new RuntimeException("Nije moguće aktivirati rezervaciju drugog aranžmana!");
    }
    stanje.aktivirajRezervaciju(this, rezervacija);
  }

  public void aktiviraj() {
    stanje.aktiviraj(this);
  }

  public void otkazi() {

    if (stanje instanceof AranzmanOtkazan) {
      String opis = String.format("Nije moguće otkazati aranžman %d jer je on već otkazan.", oznaka);
      throw new RuntimeException(opis);
    }

    List<Rezervacija> rezervacije = rezervacije();
    for (var r : rezervacije) {
      r.otkazi();
      obavijestiRezervacijaNijeViseAktivna(r);
    }

    postaviStanje(new AranzmanOtkazan());

  }

  public void provjeriStanje() {
    stanje.provjeriStanje(this);
  }

  @Override
  public void kadaRezervacijaPostalaAktivna(Rezervacija aktivirana) {
    List<Rezervacija> rezervacije = aktivneRezervacije();
    for (Rezervacija r : rezervacije) {
      r.kadaRezervacijaPostalaAktivna(aktivirana);
    }
  }

  @Override
  public void kadaRezervacijaNijeViseAktivna(Rezervacija rezervacija) {
    List<Rezervacija> rezervacije = odgodjeneRezervacije();
    for (Rezervacija r : rezervacije) {
      r.kadaRezervacijaNijeViseAktivna(rezervacija);
    }
  }

  @Override
  public boolean dodajPromatraca(RezervacijaObserver promatrac) {
    if (promatraci.contains(promatrac)) {return false;}
    promatraci.add(promatrac);
    return true;
  }

  @Override
  public boolean ukloniPromatraca(RezervacijaObserver promatrac) {
    return promatraci.remove(promatrac);
  }

  public List<Korisnik> dajPretplaceneKorisnike() {
    return promatraci.stream()
        .filter(e -> e instanceof Korisnik)
        .map(e -> (Korisnik) e)
        .toList();
  }

  public void ukloniPretplaceneKorisnike() {
    var korisnici = dajPretplaceneKorisnike();
    for (var korisnik : korisnici) {
      ukloniPromatraca(korisnik);
    }
  }

  @Override
  public void obavijestiRezervacijaPostalaAktivna(Rezervacija aktivirana) {
    for (var promatrac : promatraci) {
      promatrac.kadaRezervacijaPostalaAktivna(aktivirana);
    }
  }

  @Override
  public void obavijestiRezervacijaNijeViseAktivna(Rezervacija rezervacija) {
    for (var promatrac : promatraci) {
      promatrac.kadaRezervacijaNijeViseAktivna(rezervacija);
    }
  }

  @Override
  public void obavijestiPromjenuStanjaAranzmana(Aranzman aranzman) {
    for (var promatrac : promatraci) {
      promatrac.kadaPromjenaStanjaAranzmana(aranzman);
    }
  }

  @Override
  public void obavijestiPromjenuStanjaRezervacije(Rezervacija rezervacija) {
    for (var promatrac : promatraci) {
      promatrac.kadaPromjenaStanjaRezervacije(rezervacija);
    }
  }

  @Override
  public void prihvati(PutovanjeVisitor visitor) {
    visitor.posjeti(this);
  }

  public AranzmanStanje stanje() {
    return stanje;
  }

  public void postaviStanje(AranzmanStanje stanje) {
    this.stanje = stanje;
    obavijestiPromjenuStanjaAranzmana(this);
  }

  public StanjeId idStanja() {
    return stanje.dajId();
  }

  public String nazivStanja() {
    return stanje.dajNaziv();
  }

  public List<Rezervacija> rezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .toList();
  }

  public List<Rezervacija> primljeneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jePrimljena())
        .toList();
  }

  public List<Rezervacija> aktivneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeAktivna())
        .toList();
  }

  public List<Rezervacija> primljeneAktivneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jePrimljena() || e.jeAktivna())
        .toList();
  }

  public List<Rezervacija> rezervacijeNaCekanju() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeNaCekanju())
        .toList();
  }

  public List<Rezervacija> otkazaneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeOtkazana())
        .toList();
  }

  public List<Rezervacija> odgodjeneRezervacije() {
    return djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeOdgodjena())
        .toList();
  }

  public int brojPrimljenih() {
    return Math.toIntExact(djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jePrimljena())
        .count());
  }

  public int brojAktivnih() {
    return Math.toIntExact(djeca.stream()
        .map(e -> (Rezervacija) e)
        .filter(e -> e.jeAktivna())
        .count());
  }

  public Rezervacija dajRezervacijuKorisnika(Korisnik korisnik, List<Rezervacija.StanjeId> prioritet) {
    var rezervacije = rezervacije().stream()
        .filter(e -> e.korisnik().equals(korisnik))
        .toList();

    for (Rezervacija.StanjeId stanjeId : prioritet) {
      Rezervacija r = rezervacije.stream()
          .filter(e -> e.idStanja() == stanjeId)
          .findFirst().orElse(null);

      if (r != null) {
        return r;
      }
    }

    return null;
  }

  public TuristickaAgencija dajAgenciju() {
    return (TuristickaAgencija) dajRoditelja();
  }

  /**
   * Provjeri preklapa li se ovaj aranžman s drugim aranžmanom.
   *
   * @param drugi
   * @return true ako se aranžmani preklapaju
   */
  public boolean preklapaSe(Aranzman drugi) {
    if (pocetniDatum().isBefore(drugi.pocetniDatum())) {
      return zavrsniDatum().compareTo(drugi.pocetniDatum()) >= 0;
    } else {
      return drugi.zavrsniDatum().compareTo(pocetniDatum()) >= 0;
    }
  }

  // region Memento

  public StanjeAranzmanaMemento spremiStanje() {
    return new StanjeAranzmanaMemento(this);
  }

  public void obnoviStanje(StanjeAranzmanaMemento memento) {

    ukloniPretplaceneKorisnike();
    obnoviOsnovneInformacije(memento);
    obnoviRezervacije(memento);
    obnoviPretplaceneKorisnike(memento);

    List<Rezervacija> odgodjene = odgodjeneRezervacije();
    for (Rezervacija rezervacija : odgodjene) {
      aktivirajRezervaciju(rezervacija);
    }

  }

  private void obnoviOsnovneInformacije(StanjeAranzmanaMemento memento) {

    naziv = memento.naziv();
    program = memento.program();

    pocetniDatum = memento.pocetniDatum();
    zavrsniDatum = memento.zavrsniDatum();
    vrijemeKretanja = memento.vrijemeKretanja();
    vrijemePovratka = memento.vrijemePovratka();

    cijena = memento.cijena();
    minBrojPutnika = memento.minBrojPutnika();
    maxBrojPutnika = memento.maxBrojPutnika();

    brojNocenja = memento.brojNocenja();
    brojDorucka = memento.brojDorucka();
    brojRuckova = memento.brojRuckova();
    brojVecera = memento.brojVecera();
    doplataJKS = memento.doplataJKS();
    prijevoz = memento.prijevoz();

  }

  private void obnoviRezervacije(StanjeAranzmanaMemento memento) {

    List<Rezervacija> aktivne = aktivneRezervacije();
    for (var rezervacija : aktivne) {
      rezervacija.otkazi();
      obavijestiRezervacijaNijeViseAktivna(rezervacija);
    }

    djeca.clear();
    for (var rezervacija : memento.rezervacije()) {
      dodaj(rezervacija);
    }

    aktivne = aktivneRezervacije();
    for (var rezervacija : aktivne) {
      obavijestiRezervacijaPostalaAktivna(rezervacija);
    }

    stanje = memento.stanje();

  }

  private void obnoviPretplaceneKorisnike(StanjeAranzmanaMemento memento) {
    var korisnici = memento.pretplaceniKorisnici();
    for (var korisnik : korisnici) {
      dodajPromatraca(korisnik);
    }
  }

  // endregion

  /**
   * Sortira turističke aranžmane prema datumu početka.
   *
   * @param aranzmani turistički aranžmani
   * @param uzlazno   ako je true sortira uzlazno, inače sortira silazno
   * @return sortirani turistički aranžmani
   */
  public static List<Aranzman> sortiraj(List<Aranzman> aranzmani, boolean uzlazno) {
    var comparator = Comparator.comparing(Aranzman::pocetniDatum)
        .thenComparing(Aranzman::vrijemeKretanja, Comparator.nullsLast(LocalTime::compareTo));
    if (!uzlazno) {comparator = comparator.reversed();}
    return aranzmani.stream().sorted(comparator).toList();
  }

  // region Metode za dohvaćanje i postavljanje atributa

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

  public void setOznaka(int oznaka) {this.oznaka = oznaka;}

  public void setNaziv(String naziv) {this.naziv = naziv;}

  public void setProgram(String program) {this.program = program;}

  public void setPocetniDatum(LocalDate pocetniDatum) {this.pocetniDatum = pocetniDatum;}

  public void setZavrsniDatum(LocalDate zavrsniDatum) {this.zavrsniDatum = zavrsniDatum;}

  public void setVrijemeKretanja(
      LocalTime vrijemeKretanja) {this.vrijemeKretanja = vrijemeKretanja;}

  public void setVrijemePovratka(
      LocalTime vrijemePovratka) {this.vrijemePovratka = vrijemePovratka;}

  public void setCijena(float cijena) {this.cijena = cijena;}

  public void setMinBrojPutnika(int minBrojPutnika) {this.minBrojPutnika = minBrojPutnika;}

  public void setMaxBrojPutnika(int maxBrojPutnika) {this.maxBrojPutnika = maxBrojPutnika;}

  public void setBrojNocenja(int brojNocenja) {this.brojNocenja = brojNocenja;}

  public void setBrojDorucka(int brojDorucka) {this.brojDorucka = brojDorucka;}

  public void setBrojRuckova(int brojRuckova) {this.brojRuckova = brojRuckova;}

  public void setBrojVecera(int brojVecera) {this.brojVecera = brojVecera;}

  public void setDoplataJKS(float doplataJKS) {this.doplataJKS = doplataJKS;}

  public void setPrijevoz(List<String> prijevoz) {this.prijevoz = prijevoz;}

  // endregion

}