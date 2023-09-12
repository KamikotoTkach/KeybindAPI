package tkachgeek.keybindapi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import tkachgeek.tkachutils.scheduler.Scheduler;
import tkachgeek.tkachutils.scheduler.Tasks;

import java.util.HashMap;
import java.util.UUID;

import static tkachgeek.keybindapi.KeybindAPI.addClick;

public class MovementListener {
   static MovementListener instance;
   HashMap<UUID, Location> lastLocation = new HashMap<>();
   int taskId = -1;

   private MovementListener() {
   }

   public static MovementListener getInstance() {
      if (instance == null) instance = new MovementListener();
      return instance;
   }

   public static Component get(double value, double max, double len, TextColor on, TextColor off) {
      Component scale = Component.empty();
      double part = (double) max / len;

      for (int i = 0; i < len; i++) {
         if (part * i < value) {
            scale = scale.append(Component.text("|", on));
         } else {
            scale = scale.append(Component.text("|", off));
         }
      }
      return scale;
   }

   public void startTicking(JavaPlugin plugin) {
      if (taskId != -1) return;
      taskId = Scheduler.create(this).infinite().perform(MovementListener::tick).register(plugin, 2);
   }

   public void stopTicking() {
      if (taskId != -1) return;

      Tasks.cancelTask(taskId);
      taskId = -1;
   }

   public void tick() {
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
         if (lastLocation.containsKey(onlinePlayer.getUniqueId())) {
            handleMovement(onlinePlayer, lastLocation.get(onlinePlayer.getUniqueId()));
         }
         lastLocation.put(onlinePlayer.getUniqueId(), onlinePlayer.getLocation());
      }
   }

   private void handleMovement(Player player, Location prevLocation) {
      Location newLocation = player.getLocation();

      if (newLocation.getWorld() != prevLocation.getWorld()) return;

      float angle = prevLocation.getYaw();
      Location dif = prevLocation.clone().subtract(newLocation);

      Vector move = dif.toVector().normalize();
      newLocation.setDirection(move);
      newLocation.setYaw(newLocation.getYaw() - angle + 90);

      Vector dir = newLocation.getDirection();
      double xDif = dir.getX();
      double zDif = dir.getZ();

      if (dif.length() < 0.1) return;
      if (Math.abs(xDif) > Math.abs(zDif)) {
         if (xDif > 0) {
            addClick(player, ClickType.GO_FORWARD);
         } else {
            addClick(player, ClickType.GO_BACKWARD);
         }
      } else {
         if (zDif > 0) {
            addClick(player, ClickType.GO_RIGHT);
         } else {
            addClick(player, ClickType.GO_LEFT);
         }
      }
   }
}
