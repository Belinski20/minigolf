package spinalcraft.minigolf.events;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import spinalcraft.minigolf.golf.Hole;

public class ParHoleEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private Hole hole;
    private Player player;

    public ParHoleEvent(Hole hole, Player p)
    {
        this.hole = hole;
        this.player = p;
        p.sendMessage(Component.text().content("Type the number you want par to be.").build());
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
