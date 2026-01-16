package edu.unizg.foi.uzdiz.jmojzes21.modeli;

import edu.unizg.foi.uzdiz.jmojzes21.logika.visitor.PutovanjeVisitor;

public abstract class PutovanjeComponent {

  private PutovanjeComponent roditelj;

  public PutovanjeComponent dajRoditelja() {
    return roditelj;
  }

  public void postaviRoditelja(PutovanjeComposite roditelj) {
    this.roditelj = roditelj;
  }

  public abstract void prihvati(PutovanjeVisitor visitor);

}
