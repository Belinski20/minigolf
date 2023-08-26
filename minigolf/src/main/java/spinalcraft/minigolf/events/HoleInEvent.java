package spinalcraft.minigolf.events;

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

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HoleInEvent(Player p)
    {
        this.p = p;
        Golfer golfer = Minigolf.playerManager.getGolfer(p);
        Party party = Minigolf.playerManager.getPlayersParty(p);

        party.getScoreCard().updateScoreCard(party);
        golfer.getCourse().getHoleByNumber(party.getCurrentCourse()).setComplete();
        golfer.getBall().getBall().remove();
        golfer.getBall().setBall(null);

        if(party.isDoneWithHole())
        {
            party.incrementHole();
            party.teleportToNextHole();
        }
    }

    public Player getPlayer()
    {
        return p;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
