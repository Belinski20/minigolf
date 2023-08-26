package spinalcraft.minigolf.events;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import spinalcraft.minigolf.golf.Hole;

public class TeeHoleEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private Player player;
    private Hole hole;

    public TeeHoleEvent(Player player, Hole hole)
    {
        this.player = player;
        this.hole = hole;
        player.sendMessage(Component.text().content("Place the white candle where you want the tee to be.").build());
    }

    public Player getPlayer()
    {
        return player;
    }

    public Hole getHole()
    {
        return hole;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
