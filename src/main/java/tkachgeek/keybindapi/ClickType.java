package tkachgeek.keybindapi;

public enum ClickType {
  MOUSE_LEFT("L", true),
  MOUSE_RIGHT("R"),
  SHIFT_UP("N", true, true),
  SHIFT_DOWN("S", true),
  JUMP("J", true);
  public String letter;
  public boolean disableFirst = false;
  boolean disabled = false;
  
  ClickType(String letter) {
    this.letter = letter;
  }
  
  ClickType(String letter, boolean disableFirst) {
    this(letter);
    this.disableFirst = disableFirst;
  }
  
  ClickType(String letter, boolean disableFirst, boolean disabled) {
    this(letter, disableFirst);
    this.disabled = disabled;
  }
  
  void disable() {
    disabled = true;
  }
  
  void setLetter(String letter) {
    this.letter = letter;
  }
}
