package edu.unizg.foi.uzdiz.jmojzes21.podaci;

public abstract class PutovanjeComponent {

  private PutovanjeComponent roditelj;

  public PutovanjeComponent dajRoditelja() {
    return roditelj;
  }

  public void postaviRoditelja(PutovanjeComposite roditelj) {
    this.roditelj = roditelj;
  }

}
