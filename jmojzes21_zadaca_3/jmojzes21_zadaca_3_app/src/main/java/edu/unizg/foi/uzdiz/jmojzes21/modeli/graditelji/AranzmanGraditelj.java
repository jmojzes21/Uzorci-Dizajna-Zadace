package edu.unizg.foi.uzdiz.jmojzes21.modeli.graditelji;

import edu.unizg.foi.uzdiz.jmojzes21.modeli.Aranzman;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Graditelj za turistički aranžman.
 */
public interface AranzmanGraditelj {

  AranzmanGraditelj napraviAranzman(int oznaka, String naziv);

  AranzmanGraditelj setProgram(String program);

  AranzmanGraditelj setPocetniDatum(LocalDate pocetniDatum);

  AranzmanGraditelj setZavrsniDatum(LocalDate zavrsniDatum);

  AranzmanGraditelj setVrijemeKretanja(LocalTime vrijemeKretanja);

  AranzmanGraditelj setVrijemePovratka(LocalTime vrijemePovratka);

  AranzmanGraditelj setCijena(float cijena);

  AranzmanGraditelj setMinBrojPutnika(int minBrojPutnika);

  AranzmanGraditelj setMaxBrojPutnika(int maxBrojPutnika);

  AranzmanGraditelj setBrojNocenja(int brojNocenja);

  AranzmanGraditelj setBrojDorucka(int brojDorucka);

  AranzmanGraditelj setBrojRuckova(int brojRuckova);

  AranzmanGraditelj setBrojVecera(int brojVecera);

  AranzmanGraditelj setDoplataZaJednokrevetnuSobu(float doplataZaJednokrevetnuSobu);

  AranzmanGraditelj setPrijevoz(List<String> prijevoz);

  Aranzman dajAranzman() throws Exception;

}
