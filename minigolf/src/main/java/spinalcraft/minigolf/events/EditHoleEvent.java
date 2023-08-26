package spinalcraft.minigolf.events;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EditHoleEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private Player player;
    private ItemStack itemStack;


    public EditHoleEvent(HumanEntity ent, ItemStack itemStack)
    {
        this.player = (Player)ent;
        this.itemStack = itemStack;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getitemStack() {
        return itemStack;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
