package spinalcraft.minigolf.events;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class AddHoleEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private Player player;
    private Inventory courseInv;
    private boolean isCancelled = false;

    public AddHoleEvent(Inventory courseInv, HumanEntity ent)
    {
        Player p = (Player)ent;
        this.player = p;
        this.courseInv = courseInv;
        courseInv.close();
        p.sendMessage(Component.text().content("Type the name of the Hole.").build());
    }

    public Player getPlayer()
    {
        return player;
    }

    public Inventory getInventory()
    {
        return courseInv;
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = true;
    }
}
