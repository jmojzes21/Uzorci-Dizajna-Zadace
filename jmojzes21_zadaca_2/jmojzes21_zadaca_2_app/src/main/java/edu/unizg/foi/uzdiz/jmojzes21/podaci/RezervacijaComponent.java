package edu.unizg.foi.uzdiz.jmojzes21.podaci;

public abstract class RezervacijaComponent {

  private RezervacijaComponent roditelj;

  public RezervacijaComponent dajRoditelja() {
    return roditelj;
  }

  public void postaviRoditelja(RezervacijaComposite roditelj) {
    this.roditelj = roditelj;
  }

}
