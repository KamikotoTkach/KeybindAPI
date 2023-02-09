package tkachgeek.keybindapi;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;

import static tkachgeek.keybindapi.KeybindAPI.addClick;

public class Event implements Listener {
  
  @EventHandler(
     priority = EventPriority.MONITOR
  )
  void click(PlayerInteractEvent event) {
    if (event.getHand() == EquipmentSlot.OFF_HAND) return;
    if (event.getMaterial().isInteractable()) return;
    
    if (KeybindAPI.CANCEL_EVENT_WHILE_KEYBINDING) event.setCancelled(true);
    
    addClick(
       event.getPlayer(), event.getAction() == Action.LEFT_CLICK_AIR
          || event.getAction() == Action.LEFT_CLICK_BLOCK
          ? ClickType.MOUSE_LEFT : ClickType.MOUSE_RIGHT
    );
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
