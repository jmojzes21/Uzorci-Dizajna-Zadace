package uzdiz_test;

import java.util.List;

public class MultiValues {

  private final List<String> values;

  public MultiValues(List<String> values) {
    this.values = values;
  }

  public boolean contains(String value) {
    return values.stream().anyMatch(e -> value.contains(e));
  }

  @Override
  public String toString() {
    return values.getFirst();
  }
}
