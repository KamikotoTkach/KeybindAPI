package tkachgeek.keybindapi;

public enum ClickType {
  MOUSE_LEFT("L", true),
  MOUSE_RIGHT("R"),
  SHIFT_UP("N", true, true),
  SHIFT_DOWN("S", true),
  GO_FORWARD("MF", false),
  GO_BACKWARD("MW", false),
  GO_LEFT("ML", false),
  GO_RIGHT("MR", false),
  JUMP("J", true);
  public String letter;
  public boolean disableFirst = false;
  boolean disabled = false;
  
  ClickType(String letter) {
    this.letter = letter;
  }
  
  ClickType(String letter, boolean blockStartWith) {
    this(letter);
    this.disableFirst = blockStartWith;
  }
  
  ClickType(String letter, boolean disableFirst, boolean disabled) {
    this(letter, disableFirst);
    this.disabled = disabled;
  }
  
  public void disable() {
    disabled = true;
  }
  
  public void enable() {
    disabled = false;
  }
  
  public void setLetter(String letter) {
    this.letter = letter;
  }
}
