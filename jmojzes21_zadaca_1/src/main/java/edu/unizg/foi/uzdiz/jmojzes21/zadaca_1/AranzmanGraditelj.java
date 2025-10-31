package edu.unizg.foi.uzdiz.jmojzes21.zadaca_1;

import edu.unizg.foi.uzdiz.jmojzes21.zadaca_1.podaci.Aranzman;
import java.time.LocalDate;
import java.time.LocalTime;

public class AranzmanGraditelj {

  private Aranzman aranzman;

  public AranzmanGraditelj() {}

  public AranzmanGraditelj napraviAranzman(int oznaka, String naziv) {
    aranzman = new Aranzman(oznaka, naziv);
    return this;
  }
 
  public AranzmanGraditelj setProgram(String program) {
    aranzman.setProgram(program);
    return this;
  }

  public AranzmanGraditelj setPocetniDatum(LocalDate pocetniDatum) {
    aranzman.setPocetniDatum(pocetniDatum);
    return this;
  }

  public AranzmanGraditelj setZavrsniDatum(LocalDate zavrsniDatum) {
    aranzman.setZavrsniDatum(zavrsniDatum);
    return this;
  }

  public AranzmanGraditelj setVrijemeKretanja(LocalTime vrijemeKretanja) {
    aranzman.setVrijemeKretanja(vrijemeKretanja);
    return this;
  }

  public AranzmanGraditelj setVrijemePovratka(LocalTime vrijemePovratka) {
    aranzman.setVrijemePovratka(vrijemePovratka);
    return this;
  }

  public AranzmanGraditelj setCijena(float cijena) {
    aranzman.setCijena(cijena);
    return this;
  }

  public AranzmanGraditelj setMinBrojPutnika(int minBrojPutnika) {
    aranzman.setMinBrojPutnika(minBrojPutnika);
    return this;
  }

  public AranzmanGraditelj setMaxBrojPutnika(int maxBrojPutnika) {
    aranzman.setMaxBrojPutnika(maxBrojPutnika);
    return this;
  }

  public AranzmanGraditelj setBrojNocenja(int brojNocenja) {
    aranzman.setBrojNocenja(brojNocenja);
    return this;
  }

  public AranzmanGraditelj setBrojDorucka(int brojDorucka) {
    aranzman.setBrojDorucka(brojDorucka);
    return this;
  }

  public AranzmanGraditelj setBrojRuckova(int brojRuckova) {
    aranzman.setBrojRuckova(brojRuckova);
    return this;
  }

  public AranzmanGraditelj setBrojVecera(int brojVecera) {
    aranzman.setBrojVecera(brojVecera);
    return this;
  }

  public AranzmanGraditelj setDoplataZaJednokrevetnuSobu(float doplataZaJednokrevetnuSobu) {
    aranzman.setDoplataZaJednokrevetnuSobu(doplataZaJednokrevetnuSobu);
    return this;
  }

  public AranzmanGraditelj setPrijevoz(String prijevoz) {
    aranzman.setPrijevoz(prijevoz);
    return this;
  }

  public Aranzman dajAranzman() {
    return aranzman;
  }

}
