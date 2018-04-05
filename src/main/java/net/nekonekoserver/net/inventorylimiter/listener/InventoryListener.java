package net.nekonekoserver.net.inventorylimiter.listener;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.nekonekoserver.net.inventorylimiter.InventoryLimiter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Nekoneko on 2017/11/03.
 */
@Data
@RequiredArgsConstructor
public final class InventoryListener implements Listener {

    private final InventoryLimiter plugin;
    private final ItemStack filteredItem;

    public InventoryListener(@NonNull InventoryLimiter plugin) {
        this.plugin = plugin;
        this.filteredItem = new ItemStack(Material.BARRIER);
        this.filteredItem.setAmount(1);
        ItemMeta filteredMeta = this.filteredItem.getItemMeta();
        filteredMeta.setDisplayName(ChatColor.RED + "使用不可スロット");
        this.filteredItem.setItemMeta(filteredMeta);
    }

    public void reset(Player player) {
        if (!player.hasPermission("inventorylimiter.limit")) {
            return;
        }

        Inventory inventory = player.getInventory();
        inventory.remove(filteredItem);
        for (int i : plugin.getYamlConfig().getFilteredSlots()) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                continue;
            }

            inventory.setItem(i, filteredItem);
        }
        player.updateInventory();
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        reset(event.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        inventory.remove(filteredItem);
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent event) {
        reset(event.getPlayer());
    }

    @EventHandler
    public void death(PlayerDeathEvent event) {
        event.getDrops().removeIf(itemStack -> itemStack.equals(filteredItem));
    }

    @EventHandler
    public void inventorySlot(InventoryMoveItemEvent event) {
        if (event.getItem().equals(filteredItem)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void inventorySlot(InventoryClickEvent event) {
        if (!event.getWhoClicked().hasPermission("inventorylimiter.limit")) {
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (item != null && item.equals(filteredItem)) {
            event.setCancelled(true);
            ((Player) event.getWhoClicked()).updateInventory();
        }
    }

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().equals(filteredItem)) {
            event.setCancelled(true);
        }
    }
}
