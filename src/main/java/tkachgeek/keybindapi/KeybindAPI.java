package tkachgeek.keybindapi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.tkachutils.messages.PaperMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class KeybindAPI {
  private static int KEYBIND_LENGTH = 3;
  protected static boolean CANCEL_EVENT_WHILE_KEYBINDING = false;
  private static boolean registered = false;
  private static boolean DRAW_ON_KEYBINDING = true;
  private static boolean PLAY_SOUND_ON_KEYBINDING = true;
  private static HashMap<List<ClickType>, List<KeybindConsumer>> binds = new HashMap<>();
  private static Predicate<Player> checkPlayer = (x) -> true;
  private static HashMap<Player, List<ClickType>> clicks = new HashMap<>();
  private static HashMap<Player, Long> time = new HashMap<>();

  public static void bind(KeybindConsumer consumer, ClickType... clicks) {
    if (clicks.length != KEYBIND_LENGTH) {
      Logger.getGlobal().warning("Невозможно зарегистрировать сочетание клавиш" + Arrays.toString(clicks) + ", так как длина не соответствует необходимой");
      return;
    }
    if (clicks[0].enabledFirst()) {
      Logger.getGlobal().warning("Невозможно зарегистрировать сочетание клавиш " + Arrays.toString(clicks) + ", так как " + clicks[0] + " нельзя использовать первым");
      return;
    }
    List<ClickType> clickTypes = Arrays.asList(clicks);
    if (!binds.containsKey(clickTypes)) {
      binds.put(Arrays.asList(clicks), new ArrayList<>());
    }
    binds.get(clickTypes).add(consumer);
  }

  public static void addClick(Player player, ClickType clickType) {
    if (clickType.enabled()) return;
    if (!KeybindAPI.testPlayer(player)) return;

    if (time.containsKey(player)) {
      long timeDiff = System.currentTimeMillis() - time.get(player);
      if (timeDiff > 1000) {
        clicks.get(player).clear();
      } else if (timeDiff < 200) {
        return;
      }
    }

    if (!clicks.containsKey(player)) {
      clicks.put(player, new ArrayList<>());
    }

    if (clicks.get(player).size() == 0) {
      if (clickType.enabledFirst()) return;
    }

    time.put(player, System.currentTimeMillis());
    clicks.get(player).add(clickType);

    if (PLAY_SOUND_ON_KEYBINDING) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
    if (DRAW_ON_KEYBINDING) KeybindAPI.draw(player, clicks.get(player));

    if (clicks.get(player).size() >= KeybindAPI.KEYBIND_LENGTH) {
      KeybindAPI.tryExecute(new ArrayList<>(clicks.get(player)), player);
      clicks.get(player).clear();
    }
  }

  public static void load(JavaPlugin plugin, int keybindsLength, boolean cancelEventWhileKeybinding) {
    if (KeybindAPI.registered) return;
    KeybindAPI.registered = true;
    Bukkit.getPluginManager().registerEvents(new Event(), plugin);
    CANCEL_EVENT_WHILE_KEYBINDING = cancelEventWhileKeybinding;
    KEYBIND_LENGTH = keybindsLength;
  }

  public static void applyPredicate(Predicate<Player> predicate) {
    checkPlayer = predicate;
  }

  public static boolean testPlayer(Player player) {
    return checkPlayer.test(player);
  }

  protected static void tryExecute(List<ClickType> clicks, Player player) {
    if (binds.containsKey(clicks)) {
      binds.get(clicks).stream().filter(x -> x.canRun(player)).forEach(consumer -> consumer.run(player));
    }
  }

  protected static void draw(Player player, List<ClickType> clicks) {
    Component message = Component.empty();
    for (int i = 0; i < KEYBIND_LENGTH; i++) {
      message = message.append(Component.text("[" + (clicks.size() > i ? clicks.get(i).letter() : "_") + "]"));
    }
    message = message.color(TextColor.color(202, 202, 202));

    PaperMessage.getInstance(message).sendActionBar(player);
  }

  public static void setDrawOnKeybinding(boolean drawOnKeybinding) {
    DRAW_ON_KEYBINDING = drawOnKeybinding;
  }

  public static void setPlaySoundOnKeybinding(boolean playSoundOnKeybinding) {
    PLAY_SOUND_ON_KEYBINDING = playSoundOnKeybinding;
  }
}
