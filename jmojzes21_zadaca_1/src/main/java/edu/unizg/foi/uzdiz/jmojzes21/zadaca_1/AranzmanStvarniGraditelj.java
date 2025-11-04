package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
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

  public AranzmanStvarniGraditelj setDoplataZaJednokrevetnuSobu(float doplataZaJednokrevetnuSobu) {
    aranzman.setDoplataZaJednokrevetnuSobu(doplataZaJednokrevetnuSobu);
    return this;
  }

  public AranzmanStvarniGraditelj setPrijevoz(List<String> prijevoz) {
    aranzman.setPrijevoz(prijevoz);
    return this;
  }

  public Aranzman dajAranzman() {
    return aranzman;
  }

}
