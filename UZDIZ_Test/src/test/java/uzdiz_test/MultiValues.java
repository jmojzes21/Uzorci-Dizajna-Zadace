package uzdiz_test;

import java.util.List;

public class MultiValues {

  private List<String> values;

  public MultiValues(List<String> values) {
    this.values = values;
  }

  public boolean contains(String value) {
    return values.stream().anyMatch(e -> value.contains(e));
  }

}
