package spinalcraft.minigolf.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.player.Golfer;
import spinalcraft.minigolf.player.Party;

public class HoleInEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Player p;
    private Golfer golfer;
    private Location location;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HoleInEvent(Player p)
    {
        this.p = p;
        Golfer golfer = Minigolf.playerManager.getGolfer(p);
        this.golfer = golfer;
        Party party = Minigolf.playerManager.getPlayersParty(p);

        party.getScoreCard().createScoreCard(party);
        location = golfer.getBall().getLocation().clone();
        golfer.getBall().getBall().remove();
        golfer.getBall().setBall(null);
    }

    public Location getLocation()
    {
        return location;
    }

    public Player getPlayer()
    {
        return p;
    }
    public Golfer getGolfer()
    {
        return golfer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
