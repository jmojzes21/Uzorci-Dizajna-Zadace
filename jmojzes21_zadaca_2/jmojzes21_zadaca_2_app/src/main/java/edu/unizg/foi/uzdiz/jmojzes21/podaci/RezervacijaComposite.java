package edu.unizg.foi.uzdiz.jmojzes21.podaci;

import java.util.ArrayList;
import java.util.List;

public abstract class RezervacijaComposite extends RezervacijaComponent {

  protected List<RezervacijaComponent> djeca = new ArrayList<>();

  protected abstract void dodaj(RezervacijaComponent r);

  protected abstract void obrisi(RezervacijaComponent r);

  protected abstract void obrisiSve();

}
