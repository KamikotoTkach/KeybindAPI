package tkachgeek.keybindapi;

public enum ClickType {
   MOUSE_LEFT("L", true, true),
   MOUSE_RIGHT("R", true, true),
   SPRINT_UP("N", true, true),
   SPRINT_DOWN("C", true, true),
   SHIFT_UP("N", true, true),
   SHIFT_DOWN("S", true, true),
   GO_FORWARD("MF", true, true),
   GO_BACKWARD("MW", true, true),
   GO_LEFT("ML", true, true),
   GO_RIGHT("MR", true, true),
   JUMP("J", true, true);
   public String letter;
   public boolean disabledFirst = false;
   boolean disabled = false;

   ClickType(String letter) {
      this.letter = letter;
   }

   ClickType(String letter, boolean blockStartWith) {
      this(letter);
      this.disabledFirst = blockStartWith;
   }

   ClickType(String letter, boolean disabledFirst, boolean disabled) {
      this(letter, disabledFirst);
      this.disabled = disabled;
   }

   public void disable() {
      disabled = true;
   }

   public void enable() {
      disabled = false;
   }

   public void disableFirst() {
      disabledFirst = true;
   }

   public void enableFirst() {
      disabledFirst = false;
   }

   public void setLetter(String letter) {
      this.letter = letter;
   }
}
