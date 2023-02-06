package tkachgeek.keybindapi;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Event implements Listener {
  HashMap<Player, List<ClickType>> clicks = new HashMap<>();
  HashMap<Player, Long> time = new HashMap<>();
  
  void addClick(Player player, ClickType clickType) {
    if (clickType.disabled) return;
    if (!KeybindAPI.testPlayer(player)) return;
    if (time.containsKey(player)) {
      if (System.currentTimeMillis() - time.get(player) > 1000) {
        clicks.get(player).clear();
      }
    }
    if (!clicks.containsKey(player)) {
      clicks.put(player, new ArrayList<>());
    }
    if (clicks.get(player).size() == 0) {
      if (clickType.disableFirst) return;
    }
    time.put(player, System.currentTimeMillis());
    clicks.get(player).add(clickType);
    player.playSound(player.getLocation(), "ui.button.click", 0.5f, 1);
    KeybindAPI.draw(player, clicks.get(player));
    if (clicks.get(player).size() >= KeybindAPI.KEYBIND_LENGTH) {
      KeybindAPI.tryExecute(new ArrayList<>(clicks.get(player)), player);
      clicks.get(player).clear();
    }
  }
  
  @EventHandler(
     ignoreCancelled = true,
     priority = EventPriority.MONITOR
  )
  void click(PlayerInteractEvent event) {
    if (event.getHand() == EquipmentSlot.OFF_HAND) return;
    if (event.getMaterial().isInteractable()) return;
    if (KeybindAPI.CANCEL_EVENT_WHILE_KEYBINDING) event.setCancelled(true);
    addClick(event.getPlayer(), event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK ? ClickType.MOUSE_LEFT : ClickType.MOUSE_RIGHT);
  }
  
  @EventHandler
  void jump(PlayerJumpEvent event) {
    addClick(event.getPlayer(), ClickType.JUMP);
  }
  
  @EventHandler
  void sneak(PlayerToggleSneakEvent event) {
    addClick(event.getPlayer(), event.isSneaking() ? ClickType.SHIFT_DOWN : ClickType.SHIFT_UP);
  }
}
