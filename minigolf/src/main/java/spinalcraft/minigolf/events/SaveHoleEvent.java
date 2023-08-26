package spinalcraft.minigolf.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import spinalcraft.minigolf.golf.Hole;

public class SaveHoleEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    private Hole hole;
    private Player player;

    public SaveHoleEvent(Player player, Hole hole)
    {
        this.hole = hole;
        this.player = player;
    }

    public Hole getHole() {
        return hole;
    }

    public Player getPlayer()
    {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
