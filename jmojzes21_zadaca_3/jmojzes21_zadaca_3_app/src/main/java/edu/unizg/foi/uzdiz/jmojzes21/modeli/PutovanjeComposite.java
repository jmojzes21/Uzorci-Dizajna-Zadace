package edu.unizg.foi.uzdiz.jmojzes21.modeli;

import java.util.ArrayList;
import java.util.List;

public abstract class PutovanjeComposite extends PutovanjeComponent {

  protected List<PutovanjeComponent> djeca = new ArrayList<>();

  protected abstract void dodaj(PutovanjeComponent r);

  protected abstract void obrisi(PutovanjeComponent r);

  protected abstract void obrisiSve();

}
