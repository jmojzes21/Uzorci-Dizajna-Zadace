package edu.unizg.foi.uzdiz.jmojzes21.logika.memento;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class StanjeAranzmanaCaretaker {

  private final Map<Integer, Stack<StanjeAranzmanaMemento>> mementoSpremnik = new HashMap<>();

  public void spremiMemento(int oznaka, StanjeAranzmanaMemento memento) {

    Stack<StanjeAranzmanaMemento> stanja = mementoSpremnik.get(oznaka);

    if (stanja == null) {
      stanja = new Stack<>();
      mementoSpremnik.put(oznaka, stanja);
    }

    stanja.push(memento);
  }

  public StanjeAranzmanaMemento dajMemento(int oznaka) throws Exception {

    Stack<StanjeAranzmanaMemento> stanja = mementoSpremnik.get(oznaka);

    if (stanja == null || stanja.isEmpty()) {
      String opis = String.format("Ne postoji spremljeno stanje za aranžman %d!", oznaka);
      throw new Exception(opis);
    }

    return stanja.pop();
  }

}
