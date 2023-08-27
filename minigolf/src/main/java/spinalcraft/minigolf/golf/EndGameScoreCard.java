package spinalcraft.minigolf.golf;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.player.Golfer;
import spinalcraft.minigolf.player.Party;

import java.util.LinkedList;
import java.util.List;

public class EndGameScoreCard implements Listener {

    private Inventory scoreCard;

    public EndGameScoreCard(Inventory inv)
    {
        scoreCard = Bukkit.createInventory(null, 54, text());
        scoreCard.setContents(inv.getContents());
        Bukkit.getServer().getPluginManager().registerEvents(this, Minigolf.m);
    }

    private TextComponent text()
    {
        return Component.text().content("ScoreCard").build();
    }

    public void open(HumanEntity ent)
    {
        ent.openInventory(scoreCard);
    }


    public void unregister()
    {
        HandlerList.unregisterAll(this);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(scoreCard)) return;
        e.setCancelled(true);
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(scoreCard)) {
            e.setCancelled(true);
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().equals(scoreCard)) {
            unregister();
        }
    }
}
