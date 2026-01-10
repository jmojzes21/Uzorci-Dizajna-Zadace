package edu.unizg.foi.uzdiz.jmojzes21.modeli;

public abstract class PutovanjeComponent {

  private PutovanjeComponent roditelj;

  public PutovanjeComponent dajRoditelja() {
    return roditelj;
  }

  public void postaviRoditelja(PutovanjeComposite roditelj) {
    this.roditelj = roditelj;
  }

}
