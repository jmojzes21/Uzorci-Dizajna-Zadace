package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

import java.util.List;

public interface ITablicniIspis {

  void ispisiZaglavlje();

  void ispisi(List<String> podaci);

  void ispisiCrtu();

  List<StupacTablice> stupci();

}
