package edu.unizg.foi.uzdiz.jmojzes21.tablicni_ispis;

import java.util.List;

public abstract class TablicniIspisDecorator implements ITablicniIspis {

  protected ITablicniIspis tablicniIspis;

  public TablicniIspisDecorator(ITablicniIspis tablicniIspis) {
    this.tablicniIspis = tablicniIspis;
  }

  @Override
  public void ispisiZaglavlje() {
    tablicniIspis.ispisiZaglavlje();
  }

  @Override
  public void ispisi(List<String> podaci) {
    tablicniIspis.ispisi(podaci);
  }

  @Override
  public void ispisiCrtu() {
    tablicniIspis.ispisiCrtu();
  }

  @Override
  public List<StupacTablice> stupci() {
    return tablicniIspis.stupci();
  }
  
}
