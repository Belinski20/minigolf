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

public class ScoreCard implements Listener {

    private Inventory scoreCard;
    private Party party;

    public ScoreCard(Party party)
    {
        this.party = party;
        scoreCard = Bukkit.createInventory(null, 54, text());
        createScoreCard(party);
        Bukkit.getServer().getPluginManager().registerEvents(this, Minigolf.m);
    }

    private TextComponent text()
    {
        return Component.text().content("ScoreCard").build();
    }

    public void openGUI(HumanEntity ent)
    {
        ent.openInventory(scoreCard);
    }

    public Inventory getScoreCard()
    {
        return scoreCard;
    }

    public void createScoreCard(Party party)
    {
        int scorecardIndex = 1;
        int playerScoreCardStartIndex = 9;
        for(Hole c : Minigolf.playerManager.getGolfer(party.getPlayers().get(0)).getCourse().getHoles())
        {
            ItemStack course = createCourseNames(c);
            scoreCard.setItem(scorecardIndex, course);
            scorecardIndex++;
        }

        scorecardIndex = playerScoreCardStartIndex;
        for(Player p : party.getPlayers())
        {

            Golfer g = Minigolf.playerManager.getGolfer(p);
            ItemStack golferIcon = createGolferIcon(p, g);
            scoreCard.setItem(scorecardIndex, golferIcon);
            scorecardIndex++;


            for(Hole c : g.getCourse().getHoles())
            {
                ItemStack golferCourseItem = createGolferCourseItems(c);
                scoreCard.setItem(scorecardIndex, golferCourseItem);
                scorecardIndex++;
            }
            playerScoreCardStartIndex += 9;
            scorecardIndex = playerScoreCardStartIndex;
        }
    }

    private ItemStack createCourseNames(Hole course)
    {
        final ItemStack item = new ItemStack(Material.MOSS_BLOCK, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text().content(course.getName()).build());
        List<Component> lore = new LinkedList<>();
        lore.add(Component.text().content("Par: " + course.getPar()).build());
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createGolferIcon(Player p, Golfer g)
    {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.displayName(Component.text().content(p.getName()).build());
        skullMeta.setOwningPlayer(p);
        List<Component> lore = new LinkedList<>();

        lore.add(Component.text().content("Course Par : "+ g.getCourse().getCourseTotalPar()).build());
        lore.add(Component.text().content("Player Course Par : "+ g.getCourse().getGolferScore()).build());
        skullMeta.lore(lore);
        item.setItemMeta(skullMeta);
        return item;
    }

    private ItemStack createGolferCourseItems(Hole c)
    {
        final ItemStack item;
        if(c.getStrokes() < c.getPar())
            item = new ItemStack(Material.GREEN_CONCRETE, 1);
        else if (c.getStrokes() == c.getPar())
            item = new ItemStack(Material.GRAY_CONCRETE, 1);
        else
            item = new ItemStack(Material.RED_CONCRETE, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text().content(c.getName()).build());
        List<Component> lore = new LinkedList<>();
        lore.add(Component.text().content("Strokes: " + c.getStrokes()).build());
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void unregister()
    {
        HandlerList.unregisterAll(this);
    }

    public Party getParty()
    {
        return party;
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
}
