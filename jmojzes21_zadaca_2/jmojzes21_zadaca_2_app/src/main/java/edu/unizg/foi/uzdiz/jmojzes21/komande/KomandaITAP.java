package edu.unizg.foi.uzdiz.jmojzes21.komande;

import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.TuristickaAgencija;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.NeispravnaKomandaGreska;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.RegexKomandeGraditelj;

public class KomandaITAP {

  private final TuristickaAgencija agencija;

  public KomandaITAP(TuristickaAgencija agencija) {
    this.agencija = agencija;
  }

  public void obradiKomanduDetaljiAranzmana(String args) throws Exception {

    var uzorak = new RegexKomandeGraditelj()
        .dodajBroj("oznaka")
        .dajUzorak();

    var matcher = uzorak.matcher(args);
    if (!matcher.matches()) {
      String opis = "ITAP oznaka";
      throw new NeispravnaKomandaGreska(opis);
    }

    int oznaka = Integer.parseInt(matcher.group("oznaka"));

    Aranzman aranzman = agencija.dajAranzman(oznaka);

    if (aranzman == null) {
      System.out.println("Aranžman ne postoji.");
      return;
    }

    prikaziDetaljeAranzmana(aranzman);
  }

  private void prikaziDetaljeAranzmana(Aranzman a) {

    var f = Formati.dajInstancu();

    System.out.printf("Oznaka: %d\n", a.oznaka());
    System.out.printf("Naziv: %s\n", a.naziv());
    System.out.printf("Program: %s\n", a.program().replace("\\n", "\n"));
    System.out.printf("Početni datum: %s\n", f.formatiraj(a.pocetniDatum()));
    System.out.printf("Završni datum: %s\n", f.formatiraj(a.zavrsniDatum()));
    System.out.printf("Vrijeme kretanja: %s\n", f.formatiraj(a.vrijemeKretanja()));
    System.out.printf("Vrijeme povratka: %s\n", f.formatiraj(a.vrijemePovratka()));
    System.out.printf("Cijena: %s\n", f.formatiraj(a.cijena()));
    System.out.printf("Min broj putnika: %d\n", a.minBrojPutnika());
    System.out.printf("Max broj putnika: %d\n", a.maxBrojPutnika());
    System.out.printf("Broj noćenja: %d\n", a.brojNocenja());
    System.out.printf("Doplata za jednokrevetnu sobu: %s\n", f.formatiraj(a.doplataZaJednokrevetnuSobu()));
    System.out.printf("Prijevoz: %s\n", a.prijevoz() != null ? String.join(", ", a.prijevoz()) : "nema");
    System.out.printf("Broj doručka: %d\n", a.brojDorucka());
    System.out.printf("Broj ručkova: %d\n", a.brojRuckova());
    System.out.printf("Broj večera: %d\n", a.brojVecera());
    System.out.printf("Status: %s\n", a.nazivStanja());

  }

}
