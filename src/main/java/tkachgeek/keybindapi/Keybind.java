package tkachgeek.keybindapi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.tkachutils.messages.Message;
import tkachgeek.tkachutils.server.ServerUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Keybind {
   static JavaPlugin plugin;
   static int KEYBIND_LENGTH = 3;
   static boolean CANCEL_EVENT_WHILE_KEYBINDING = false;
   static HashMap<List<ClickType>, List<KeybindConsumer>> binds = new HashMap<>();
   static Predicate<Player> checkPlayer = (x) -> true;

   public Keybind(KeybindConsumer consumer, ClickType... clicks) {
      if (clicks.length != KEYBIND_LENGTH) {
         Logger.getGlobal().warning("Невозможно зарегистрировать сочетание клавиш, так как длина не соответствует необходимой");
         return;
      }
      if (clicks[0].disableFirst) {
         Logger.getGlobal().warning("Невозможно зарегистрировать сочетание клавиш, так как " + clicks[0] + " нельзя использовать первым");
         return;
      }
      List<ClickType> clickTypes = Arrays.asList(clicks);
      if (!binds.containsKey(clickTypes)) {
         binds.put(Arrays.asList(clicks), new ArrayList<>());
      }
      binds.get(clickTypes).add(consumer);
   }

   static public void initialize(JavaPlugin plugin, int keybindsLength, boolean cancelEventWhileKeybinding) {
      Bukkit.getPluginManager().registerEvents(new Event(), plugin);
      Keybind.plugin = plugin;
      CANCEL_EVENT_WHILE_KEYBINDING = cancelEventWhileKeybinding;
      KEYBIND_LENGTH = keybindsLength;
   }

   static public void applyPredicate(Predicate<Player> predicate) {
      checkPlayer = predicate;
   }

   static public boolean testPlayer(Player player) {
      return checkPlayer.test(player);
   }

   static protected void tryExecute(List<ClickType> clicks, Player player) {
      if (binds.containsKey(clicks)) {
         binds.get(clicks).stream().filter(x -> x.canRun(player)).forEach(consumer -> consumer.run(player));
      }
   }

   protected static void draw(Player player, List<ClickType> clicks) {
      Component message = Component.empty();
      for (int i = 0; i < KEYBIND_LENGTH; i++) {
         message = message.append(Component.text("[" + (clicks.size() > i ? clicks.get(i).letter : "_") + "]"));
      }
      message = message.color(TextColor.color(202, 202, 202));
      if (ServerUtils.isVersionBefore1_16_5()) {
         player.sendActionBar(Message.getInstance(message).toString());
      } else {
         player.sendActionBar(message);
      }
   }
}
