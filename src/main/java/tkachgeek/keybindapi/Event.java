package tkachgeek.keybindapi;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EquipmentSlot;
import tkachgeek.tkachutils.items.ItemType;

import static tkachgeek.keybindapi.KeybindAPI.addClick;

public class Event implements Listener {

   @EventHandler(
         priority = EventPriority.MONITOR
   )
   void click(PlayerInteractEvent event) {
      if (event.getHand() == EquipmentSlot.OFF_HAND) return;

      ClickType clickType = null;
      if (event.getAction().isRightClick()) {
         if (ItemType.isUsable(event.getItem())) return;
         Block block = event.getClickedBlock();
         if (block != null && block.getType().isInteractable()) return;
         clickType = ClickType.MOUSE_RIGHT;
      }

      if (event.getAction().isLeftClick()) {
         clickType = ClickType.MOUSE_LEFT;
      }

      if (clickType == null) return;

      if (KeybindAPI.CANCEL_EVENT_WHILE_KEYBINDING) event.setCancelled(true);
      addClick(
            event.getPlayer(), clickType
      );
   }

   @EventHandler
   void sprint(PlayerToggleSprintEvent event) {
      addClick(event.getPlayer(), event.isSprinting() ? ClickType.SPRINT_DOWN : ClickType.SPRINT_UP);
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
