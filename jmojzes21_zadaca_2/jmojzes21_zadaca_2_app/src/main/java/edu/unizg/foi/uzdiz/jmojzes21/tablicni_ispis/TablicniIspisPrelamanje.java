package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TablicniIspisPrelamanje extends TablicniIspisDecorator {

  public TablicniIspisPrelamanje(ITablicniIspis tablicniIspis) {
    super(tablicniIspis);
  }

  @Override
  public void ispisi(List<String> podaci) {

    var stupci = tablicniIspis.stupci();

    List<List<String>> redci = new ArrayList<>();

    List<String> prviRedak = napraviPrazniRedak(stupci.size());
    redci.add(prviRedak);

    for (int i = 0; i < stupci.size(); i++) {
      StupacTablice stupac = stupci.get(i);
      String tekst = podaci.get(i);

      if (tekst.length() > stupac.sirina()) {
        List<String> dijelovi = prelomiTekst(tekst, stupac.sirina());
        dodajPrelomljeniTekst(redci, stupci.size(), i, dijelovi);
        continue;
      }

      prviRedak.set(i, tekst);

    }

    for (List<String> redak : redci) {
      tablicniIspis.ispisi(redak);
    }

  }

  private List<String> prelomiTekst(String tekst, int sirina) {
    List<String> redci = new ArrayList<>();

    while (tekst.length() > sirina) {
      int index = tekst.lastIndexOf(' ', sirina);
      if (index == -1) {
        redci.add(tekst);
        break;
      }

      String dio = tekst.substring(0, index);
      redci.add(dio);

      tekst = tekst.substring(index + 1).trim();
    }

    redci.add(tekst);
    return redci;
  }

  private void dodajPrelomljeniTekst(List<List<String>> redci, int brojStupaca, int stupac, List<String> dijelovi) {
    for (int i = 0; i < dijelovi.size(); i++) {
      if (redci.size() < i + 1) {
        redci.add(napraviPrazniRedak(brojStupaca));
      }
      List<String> redak = redci.get(i);
      redak.set(stupac, dijelovi.get(i));
    }
  }

  private List<String> napraviPrazniRedak(int brojStupaca) {
    return new ArrayList<>(Collections.nCopies(brojStupaca, ""));
  }

}
