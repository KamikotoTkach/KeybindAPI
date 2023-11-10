package tkachgeek.keybindapi;

public enum ClickType {
  MOUSE_LEFT("L"),
  MOUSE_RIGHT("R"),
  SPRINT_UP("N"),
  SPRINT_DOWN("C"),
  SHIFT_UP("N"),
  SHIFT_DOWN("S"),
  GO_FORWARD("MF"),
  GO_BACKWARD("MW"),
  GO_LEFT("ML"),
  GO_RIGHT("MR"),
  JUMP("J");
  private String letter;
  private boolean enabledFirst = false;
  private boolean enabled = false;

  ClickType(String letter) {
    this.letter = letter;
  }

  public String letter() {
    return letter;
  }

  public void letter(String letter) {
    this.letter = letter;
  }

  public boolean enabledFirst() {
    return enabledFirst;
  }

  public void enabledFirst(boolean enabledFirst) {
    this.enabledFirst = enabledFirst;
  }

  public boolean enabled() {
    return enabled;
  }

  public void enabled(boolean enabled) {
    this.enabled = enabled;
  }
}
