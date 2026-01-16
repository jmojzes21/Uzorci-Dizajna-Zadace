package edu.unizg.foi.uzdiz.jmojzes21.modeli.graditelji;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Konkretni graditelj za turistički aranžman.
 */
public class AranzmanStvarniGraditelj implements AranzmanGraditelj {

  private Aranzman aranzman;

  public AranzmanStvarniGraditelj() {}

  public AranzmanStvarniGraditelj napraviAranzman(int oznaka, String naziv) {
    aranzman = new Aranzman(oznaka, naziv);
    return this;
  }

  public AranzmanStvarniGraditelj setProgram(String program) {
    aranzman.setProgram(program);
    return this;
  }

  public AranzmanStvarniGraditelj setPocetniDatum(LocalDate pocetniDatum) {
    aranzman.setPocetniDatum(pocetniDatum);
    return this;
  }

  public AranzmanStvarniGraditelj setZavrsniDatum(LocalDate zavrsniDatum) {
    aranzman.setZavrsniDatum(zavrsniDatum);
    return this;
  }

  public AranzmanStvarniGraditelj setVrijemeKretanja(LocalTime vrijemeKretanja) {
    aranzman.setVrijemeKretanja(vrijemeKretanja);
    return this;
  }

  public AranzmanStvarniGraditelj setVrijemePovratka(LocalTime vrijemePovratka) {
    aranzman.setVrijemePovratka(vrijemePovratka);
    return this;
  }

  public AranzmanStvarniGraditelj setCijena(float cijena) {
    aranzman.setCijena(cijena);
    return this;
  }

  public AranzmanStvarniGraditelj setMinBrojPutnika(int minBrojPutnika) {
    aranzman.setMinBrojPutnika(minBrojPutnika);
    return this;
  }

  public AranzmanStvarniGraditelj setMaxBrojPutnika(int maxBrojPutnika) {
    aranzman.setMaxBrojPutnika(maxBrojPutnika);
    return this;
  }

  public AranzmanStvarniGraditelj setBrojNocenja(int brojNocenja) {
    aranzman.setBrojNocenja(brojNocenja);
    return this;
  }

  public AranzmanStvarniGraditelj setBrojDorucka(int brojDorucka) {
    aranzman.setBrojDorucka(brojDorucka);
    return this;
  }

  public AranzmanStvarniGraditelj setBrojRuckova(int brojRuckova) {
    aranzman.setBrojRuckova(brojRuckova);
    return this;
  }

  public AranzmanStvarniGraditelj setBrojVecera(int brojVecera) {
    aranzman.setBrojVecera(brojVecera);
    return this;
  }

  public AranzmanStvarniGraditelj setDoplataJKS(float doplataJKS) {
    aranzman.setDoplataJKS(doplataJKS);
    return this;
  }

  public AranzmanStvarniGraditelj setPrijevoz(List<String> prijevoz) {
    aranzman.setPrijevoz(prijevoz);
    return this;
  }

  public Aranzman dajAranzman() throws Exception {
    provjeriAranzman(aranzman);
    return aranzman;
  }

  private void provjeriAranzman(Aranzman aranzman) throws Exception {

    if (aranzman.naziv().isEmpty()) {
      throw new Exception("Naziv aranžmana ne može biti prazan!");
    }

    if (aranzman.pocetniDatum() == null) {
      throw new Exception("Aranžman mora imati početni datum!");
    }

    if (aranzman.zavrsniDatum() == null) {
      throw new Exception("Aranžman mora imati završni datum!");
    }

    if (aranzman.pocetniDatum().isAfter(aranzman.zavrsniDatum())) {
      throw new Exception("Početni datum aranžmana mora biti prije završnog datuma!");
    }

    if (aranzman.cijena() < 0) {
      throw new Exception("Cijena aranžmana ne može biti manja od 0!");
    }

    if (aranzman.minBrojPutnika() < 1) {
      throw new Exception("Minimalni broj putnika mora biti barem 1!");
    }

    if (aranzman.maxBrojPutnika() < aranzman.minBrojPutnika()) {
      throw new Exception("Maksimalni broj putnika mora biti veći ili jednak minimalnom broju putnika!");
    }

    if (aranzman.brojNocenja() < 0) {
      throw new Exception("Broj noćenja ne može biti negativan!");
    }

    if (aranzman.brojDorucka() < 0) {
      throw new Exception("Broj doručka ne može biti negativan!");
    }

    if (aranzman.brojRuckova() < 0) {
      throw new Exception("Broj ručkova ne može biti negativan!");
    }

    if (aranzman.brojVecera() < 0) {
      throw new Exception("Broj večera ne može biti negativan!");
    }

  }

}
