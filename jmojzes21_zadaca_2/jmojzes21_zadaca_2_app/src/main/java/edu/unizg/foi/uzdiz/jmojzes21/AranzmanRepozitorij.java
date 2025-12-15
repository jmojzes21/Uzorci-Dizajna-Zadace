package edu.unizg.foi.uzdiz.jmojzes21;

import edu.unizg.foi.uzdiz.jmojzes21.lib.facade.UcitavacPodatakaFacade;
import edu.unizg.foi.uzdiz.jmojzes21.podaci.Aranzman;
import edu.unizg.foi.uzdiz.jmojzes21.pomocnici.Formati;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AranzmanRepozitorij {

  public List<Aranzman> ucitajAranzmane(Path putanja) throws IOException {

    var ucitavacPodataka = new UcitavacPodatakaFacade();
    List<List<String>> csvRedci = ucitavacPodataka.ucitajAranzmane(putanja);

    List<Aranzman> aranzmani = new ArrayList<>();

    for (List<String> redci : csvRedci) {
      String csvRedak = redci.getFirst();
      List<String> stupci = redci.subList(1, redci.size());

      try {
        Aranzman aranzman = parsirajAranzman(stupci);
        aranzmani.add(aranzman);
      } catch (Exception e) {
        System.out.printf("[Greška] %s\n", e.getMessage());
        System.out.println(csvRedak);
      }
    }

    return aranzmani;
  }

  private Aranzman parsirajAranzman(List<String> stupci) throws Exception {

    if (stupci.size() != 16) {
      String opis = String.format("Broj stupaca aranžmana mora biti 16! Trenutno: %d", stupci.size());
      throw new Exception(opis);
    }

    int index = 0;
    String oznaka = stupci.get(index++);
    String naziv = stupci.get(index++);
    String program = stupci.get(index++);
    String pocetniDatum = stupci.get(index++);
    String zavrsniDatum = stupci.get(index++);
    String vrijemeKretanja = stupci.get(index++);
    String vrijemePovratka = stupci.get(index++);
    String cijena = stupci.get(index++);

    String minPutnika = stupci.get(index++);
    String maxPutnika = stupci.get(index++);
    String brojNocenja = stupci.get(index++);
    String doplataJednokrevetnaSoba = stupci.get(index++);
    String prijevoz = stupci.get(index++);
    String brojDorucka = stupci.get(index++);
    String brojRuckova = stupci.get(index++);
    String brojVecera = stupci.get(index);

    var f = Formati.dajInstancu();
    AranzmanGraditelj graditelj = new AranzmanStvarniGraditelj();

    graditelj.napraviAranzman(Integer.parseInt(oznaka), naziv)
        .setProgram(program)
        .setPocetniDatum(f.parsirajDatum(pocetniDatum))
        .setZavrsniDatum(f.parsirajDatum(zavrsniDatum))
        .setCijena(Float.parseFloat(cijena))
        .setMinBrojPutnika(Integer.parseInt(minPutnika))
        .setMaxBrojPutnika(Integer.parseInt(maxPutnika));

    if (vrijemeKretanja != null) {
      graditelj.setVrijemeKretanja(f.parsirajVrijeme(vrijemeKretanja));
    }

    if (vrijemePovratka != null) {
      graditelj.setVrijemePovratka(f.parsirajVrijeme(vrijemePovratka));
    }

    if (brojNocenja != null) {
      graditelj.setBrojNocenja(Integer.parseInt(brojNocenja));
    }

    if (doplataJednokrevetnaSoba != null) {
      graditelj.setDoplataZaJednokrevetnuSobu(Float.parseFloat(doplataJednokrevetnaSoba));
    }

    if (prijevoz != null) {
      graditelj.setPrijevoz(parsirajPrijevozAranzmana(prijevoz));
    }

    if (brojDorucka != null) {
      graditelj.setBrojDorucka(Integer.parseInt(brojDorucka));
    }

    if (brojRuckova != null) {
      graditelj.setBrojRuckova(Integer.parseInt(brojRuckova));
    }

    if (brojVecera != null) {
      graditelj.setBrojVecera(Integer.parseInt(brojVecera));
    }

    return graditelj.dajAranzman();
  }

  private List<String> parsirajPrijevozAranzmana(String prijevozTekst) {
    if (prijevozTekst == null) {return null;}
    return Arrays.stream(prijevozTekst.split(";"))
        .map(e -> e.trim())
        .filter(e -> !e.isEmpty())
        .toList();
  }

}
