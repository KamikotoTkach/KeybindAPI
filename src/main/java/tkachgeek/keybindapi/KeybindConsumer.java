package tkachgeek.keybindapi;

import org.bukkit.entity.Player;

public interface KeybindConsumer {
  void run(Player player);
  boolean canRun(Player player);
}
