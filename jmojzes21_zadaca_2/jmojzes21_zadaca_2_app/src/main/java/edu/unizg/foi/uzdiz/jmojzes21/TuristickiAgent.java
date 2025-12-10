package edu.unizg.foi.uzdiz.jmojzes21;

/**
 * Turistički agent obavlja zaprimanje i otkazivanje rezervacija korisnika.
 */
public class TuristickiAgent {

  /*

  private final Map<Integer, Aranzman> aranzmani;

  public TuristickiAgent(Map<Integer, Aranzman> aranzmani) {
    this.aranzmani = aranzmani;
  }

  // region Zaprimi rezervaciju

  /**
   * Zaprimi rezervaciju
   *
   * @param aranzman    aranžman
   * @param rezervacija rezervacija
   * @throws Exception zaprimanje nije uspjelo

  public void zaprimiRezervaciju(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    provjeriDatumPrimljeneRezervacije(aranzman, rezervacija);

    int minPutnika = aranzman.minBrojPutnika();
    int maxPutnika = aranzman.maxBrojPutnika();

    int brojPrimljenih = aranzman.brojPrimljenih();
    int brojAktivnih = aranzman.brojAktivnih();

    if (brojAktivnih >= maxPutnika) {
      dodajRezervacijuNaCekanje(aranzman, rezervacija);
      return;
    }

    if (brojAktivnih >= minPutnika) {
      dodajRezervacijuUAktvine(aranzman, rezervacija);
      return;
    }

    dodajPrimljenuRezervaciju(aranzman, rezervacija);

    var rezervacije = dajRezervacijeKorisnika(aranzman, rezervacija.korisnik());
    if (rezervacije.stream().noneMatch(e -> e.datumVrijeme().isEqual(rezervacija.datumVrijeme()))) {
      throw new Exception(
          String.format("Rezervacija korisnika %s za aranžman %d je neispravna te je obrisana.",
              rezervacija.korisnik(), aranzman.oznaka()));
    }

  }

  private void obradiPrimljeneRezervacije(Aranzman aranzman) {

    List<Rezervacija> primljeneRezervacije = aranzman.primljeneRezervacije();

    List<Rezervacija> obrisaneRezervacije = new ArrayList<>();
    List<Rezervacija> ispravneRezervacije = filtrirajDuplikateRezervacija(primljeneRezervacije,
        obrisaneRezervacije);

    for (var r : obrisaneRezervacije) {
      evidentirajBrisanjeRezervacije(r,
          "Korisnik već ima primljenu rezervaciju za navedeni aranžman.");
    }

    ispravneRezervacije = ispravneRezervacije.stream()
        .filter(e -> {
          try {
            provjeriIspravnostRezervacije(aranzman, e);
            return true;
          } catch (Exception greska) {
            evidentirajBrisanjeRezervacije(e, greska.getMessage());
            return false;
          }
        })
        .toList();

    aranzman.primljeneRezervacije().clear();
    aranzman.primljeneRezervacije().addAll(ispravneRezervacije);

    if (aranzman.brojPrimljenih() >= aranzman.minBrojPutnika()) {

      var kreatorRezervacije = new KreatorAktivneRezervacije();
      var aktivneRezervacije = aranzman.primljeneRezervacije().stream()
          .map(e -> promijeniVrstuRezervacije(e, kreatorRezervacije))
          .toList();

      aranzman.primljeneRezervacije().clear();
      aranzman.aktivneRezervacije().addAll(aktivneRezervacije);

    }

  }

  private void dodajPrimljenuRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {

    int minPutnika = aranzman.minBrojPutnika();

    var novaRezervacija = promijeniVrstuRezervacije(rezervacija, new KreatorPrimljeneRezervacije());
    aranzman.primljeneRezervacije().add(novaRezervacija);

    if (aranzman.brojPrimljenih() >= minPutnika) {
      obradiPrimljeneRezervacije(aranzman);
    }

  }

  private void dodajRezervacijuUAktvine(Aranzman aranzman, Rezervacija rezervacija)
      throws Exception {

    try {
      provjeriIspravnostRezervacije(aranzman, rezervacija);
    } catch (Exception e) {
      throw new Exception(
          String.format("Korisnik %s već ima aktivnu rezervaciju za aranžman %d.",
              rezervacija.korisnik().punoIme(), aranzman.oznaka()));
    }

    var novaRezervacija = promijeniVrstuRezervacije(rezervacija, new KreatorAktivneRezervacije());
    aranzman.aktivneRezervacije().add(novaRezervacija);
  }

  private void dodajRezervacijuNaCekanje(Aranzman aranzman, Rezervacija rezervacija) {
    var novaRezervacija = promijeniVrstuRezervacije(rezervacija, new KreatorRezervacijeNaCekanju());
    aranzman.rezervacijeNaCekanju().add(novaRezervacija);
  }

  private void evidentirajBrisanjeRezervacije(Rezervacija rezervacija, String razlog) {
    var formatDatum = FormatDatuma.dajInstancu();

    String opis = String.format(
        "Brisanje rezervacije korisnika %s, aranžman %d, vrijeme %s. Razlog: %s",
        rezervacija.korisnik().punoIme(), rezervacija.oznakaAranzmana(),
        formatDatum.formatiraj(rezervacija.datumVrijeme()), razlog);
    EvidencijaGresaka.dajInstancu().evidentiraj(opis);
  }

  private void provjeriDatumPrimljeneRezervacije(Aranzman aranzman, Rezervacija rezervacija)
      throws Exception {

    LocalDateTime vrijeme = rezervacija.datumVrijeme();

    if (vrijeme.isAfter(aranzman.pocetniDatum().atStartOfDay())) {
      String opis = "Datum rezervacije je nakon početka turističkog aranžmana.";
      throw new Exception(opis);
    }
  }

  // endregion

  // region Otkaži rezervaciju

  /**
   * Otkaži rezervaciju korisnika za aranžman.
   *
   * @param aranzman aranžman
   * @param korisnik korisnik
   * @throws Exception otkazivanje nije uspjelo

  public void otkaziRezervaciju(Aranzman aranzman, Korisnik korisnik) throws Exception {

    List<Rezervacija> rezervacije = dajRezervacijeKorisnika(aranzman, korisnik);
    Rezervacija rezervacija = !rezervacije.isEmpty() ? rezervacije.getFirst() : null;

    if (rezervacija == null) {
      String opis = String.format("Korisnik %s nema rezervaciju za aranžman %d!",
          korisnik.punoIme(), aranzman.oznaka());
      throw new Exception(opis);
    }

    switch (rezervacija) {
      case PrimljenaRezervacija r:
        otkaziPrimljenuRezervaciju(aranzman, r);
        break;
      case AktivnaRezervacija r:
        otkaziAktivnuRezervaciju(aranzman, r);
        break;
      case RezervacijaNaCekanju r:
        otkaziRezervacijuNaCekanju(aranzman, r);
        break;
      default:
        throw new Exception("Nije moguće otkazati rezervaciju!");
    }

  }

  private void otkaziPrimljenuRezervaciju(Aranzman aranzman, PrimljenaRezervacija rezervacija)
      throws Exception {

    if (!aranzman.primljeneRezervacije().remove(rezervacija)) {
      throw new Exception("Nije moguće otkazati rezervaciju!");
    }

    var otkazanaRezervacija = promijeniVrstuRezervacije(rezervacija,
        new KreatorOtkazaneRezervacije());
    aranzman.otkazaneRezervacije().add(otkazanaRezervacija);

  }

  private void otkaziAktivnuRezervaciju(Aranzman aranzman, AktivnaRezervacija rezervacija)
      throws Exception {

    if (!aranzman.aktivneRezervacije().remove(rezervacija)) {
      throw new Exception("Nije moguće otkazati rezervaciju!");
    }

    Queue<Rezervacija> rezervacijeNaCekanju = aranzman.rezervacijeNaCekanju();

    var otkazanaRezervacija = promijeniVrstuRezervacije(rezervacija,
        new KreatorOtkazaneRezervacije());
    aranzman.otkazaneRezervacije().add(otkazanaRezervacija);

    if (!rezervacijeNaCekanju.isEmpty()) {

      while (!rezervacijeNaCekanju.isEmpty()) {
        Rezervacija naCekanju = rezervacijeNaCekanju.poll();

        try {
          provjeriIspravnostRezervacije(aranzman, naCekanju);
        } catch (Exception e) {
          evidentirajBrisanjeRezervacije(naCekanju, e.getMessage());
          continue;
        }

        var novaRezervacija = promijeniVrstuRezervacije(naCekanju, new KreatorAktivneRezervacije());
        aranzman.aktivneRezervacije().add(novaRezervacija);
      }


    }

    if (aranzman.brojAktivnih() < aranzman.minBrojPutnika()) {

      var kreatorRezervacije = new KreatorPrimljeneRezervacije();

      List<Rezervacija> trenutneRezervacije = aranzman.aktivneRezervacije().stream()
          .map(e -> promijeniVrstuRezervacije(e, kreatorRezervacije))
          .toList();

      aranzman.aktivneRezervacije().clear();
      aranzman.primljeneRezervacije().addAll(trenutneRezervacije);

    }

  }

  private void otkaziRezervacijuNaCekanju(Aranzman aranzman, RezervacijaNaCekanju rezervacija)
      throws Exception {

    if (!aranzman.rezervacijeNaCekanju().remove(rezervacija)) {
      throw new Exception("Nije moguće otkazati rezervaciju!");
    }

    var otkazanaRezervacija = promijeniVrstuRezervacije(rezervacija,
        new KreatorOtkazaneRezervacije());
    aranzman.otkazaneRezervacije().add(otkazanaRezervacija);

  }

  // endregion

  // region Ostalo

  /**
   * Provjeri je li rezervacija ispravna.
   *
   * @param aranzman    aranžman
   * @param rezervacija rezervacija
   * @throws Exception rezervacija nije ispravna

  private void provjeriIspravnostRezervacije(Aranzman aranzman, Rezervacija rezervacija) throws Exception {

    var korisnik = rezervacija.korisnik();
    var sveRezervacije = dajSveRezervacijeKorisnika(korisnik);

    for (var r : sveRezervacije) {
      Aranzman drugiAranzman = aranzmani.get(r.oznakaAranzmana());
      if (drugiAranzman == null) {continue;}

      if (r instanceof AktivnaRezervacija) {
        if (drugiAranzman.oznaka() == aranzman.oznaka() ||
            aranzmaniSePreklapaju(drugiAranzman, aranzman)) {

          String opis = String.format(
              "Korisnik ima aktivnu rezervaciju za aranžman %d koji se preklapa s aranžmanom %d.",
              drugiAranzman.oznaka(), rezervacija.oznakaAranzmana());
          throw new Exception(opis);
        }
      }

    }
  }

  /**
   * Filtriraj rezervacije pri čemu korisnik može imati samo jednu rezervaciju. Ostavlja se samo najstarija rezervacija
   * korisnika, ostale se brišu.
   *
   * @param rezervacije
   * @param obrisaneRezervacije rezervacije koje su obrisane, tj. duplikati su
   * @return rezervacije

  private List<Rezervacija> filtrirajDuplikateRezervacija(List<Rezervacija> rezervacije,
      List<Rezervacija> obrisaneRezervacije) {

    List<Rezervacija> rezultat = new ArrayList<>();

    for (Rezervacija rezervacija : rezervacije) {

      Rezervacija duplikat = rezultat.stream()
          .filter(e -> e.oznakaAranzmana() == rezervacija.oznakaAranzmana() && e.korisnik()
              .equals(rezervacija.korisnik()))
          .findFirst().orElse(null);

      if (duplikat != null) {
        var korisnik = rezervacija.korisnik();
        Rezervacija obrisana = null;

        if (rezervacija.datumVrijeme().isAfter(duplikat.datumVrijeme())) {
          int index = rezultat.indexOf(duplikat);
          rezultat.set(index, rezervacija);
          obrisana = duplikat;
        } else {
          obrisana = rezervacija;
        }

        obrisaneRezervacije.add(obrisana);
        continue;
      }

      rezultat.add(rezervacija);
    }

    return rezultat;
  }

  private Rezervacija promijeniVrstuRezervacije(Rezervacija rezervacija,
      KreatorRezervacije kreatorRezervacije) {
    return kreatorRezervacije.promijeniVrstu(rezervacija);
  }

  /**
   * Provjeri preklapaju li se 2 aranćmana prema datumima početka i kraja
   *
   * @param a1 aranžman 1
   * @param a2 aranžman 2
   * @return true ako se aranžmani preklapaju

  private boolean aranzmaniSePreklapaju(Aranzman a1, Aranzman a2) {
    if (a1.pocetniDatum().compareTo(a2.pocetniDatum()) > 0) {
      Aranzman a3 = a1;
      a1 = a2;
      a2 = a3;
    }
    return a1.zavrsniDatum().compareTo(a2.pocetniDatum()) >= 0;
  }

  // endregion

  // region Dohvaćanje rezervacija korisnika

  /**
   * Daj sve rezervacije korisnika za određeni aranžman. Uključuje: primljene, aktivne, na čekanju
   *
   * @param aranzman aranžman
   * @param korisnik korisnik
   * @return rezervacije korisnika

  public List<Rezervacija> dajRezervacijeKorisnika(Aranzman aranzman, Korisnik korisnik) {
    return dajRezervacijeKorisnika(aranzman, korisnik, false);
  }

  /**
   * Daj sve rezervacije korisnika za određeni aranžman. Uključuje: primljene, aktivne, na čekanju, otkazane
   *
   * @param aranzman        aranžman
   * @param korisnik        korisnik
   * @param prikaziOtkazane prikaži otkazane rezervacije
   * @return rezervacije korisnika

  public List<Rezervacija> dajRezervacijeKorisnika(Aranzman aranzman, Korisnik korisnik,
      boolean prikaziOtkazane) {
    List<Rezervacija> rezultat = new ArrayList<>();

    rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.primljeneRezervacije(), korisnik));
    rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.aktivneRezervacije(), korisnik));
    rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.rezervacijeNaCekanju(), korisnik));

    if (prikaziOtkazane) {
      rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.otkazaneRezervacije(), korisnik));
    }

    return rezultat;
  }

  /**
   * Daj sve rezervacije korisnika za sve aranžmane. Uključuje: primljene, aktivne, na čekanju
   *
   * @param korisnik korisnik
   * @return rezervacije korisnika

  public List<Rezervacija> dajSveRezervacijeKorisnika(Korisnik korisnik) {
    return dajSveRezervacijeKorisnika(korisnik, false);
  }

  /**
   * Daj sve rezervacije korisnika za sve aranžmane. Uključuje: primljene, aktivne, na čekanju, otkazane
   *
   * @param korisnik korisnik
   * @return rezervacije korisnika

  public List<Rezervacija> dajSveRezervacijeKorisnika(Korisnik korisnik, boolean prikaziOtkazane) {
    List<Rezervacija> rezultat = new ArrayList<>();

    for (Aranzman aranzman : aranzmani.values()) {
      rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.primljeneRezervacije(), korisnik));
      rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.aktivneRezervacije(), korisnik));
      rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.rezervacijeNaCekanju(), korisnik));

      if (prikaziOtkazane) {
        rezultat.addAll(filtrirajRezervacijeKorisnika(aranzman.otkazaneRezervacije(), korisnik));
      }
    }

    return rezultat;
  }

  private List<Rezervacija> filtrirajRezervacijeKorisnika(Collection<Rezervacija> rezervacije,
      Korisnik korisnik) {
    return rezervacije.stream()
        .filter(e -> e.korisnik().equals(korisnik))
        .toList();
  }

  // endregion

  */

}
