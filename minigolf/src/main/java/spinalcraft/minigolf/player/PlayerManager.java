package spinalcraft.minigolf.player;

import org.bukkit.entity.Player;
import spinalcraft.minigolf.golf.ScoreCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private Map<Player, Golfer> players;
    private ArrayList<Party> parties;
    private Map<Player, Party> pendingPartyInvites;

    public PlayerManager()
    {
        players = new HashMap<>();
        pendingPartyInvites = new HashMap<>();
        parties = new ArrayList<>();

    }

    public void addPendingPartyInvite(Player player, Party party)
    {
        pendingPartyInvites.put(player, party);
    }

    public Party getPendingPartyInvite(Player p)
    {
        if(parties.contains(pendingPartyInvites.get(p)))
            return pendingPartyInvites.get(p);
        return null;
    }

    public void removeInvite(Player p)
    {
        pendingPartyInvites.remove(p);
    }

    public boolean hasInvite(Player p)
    {
        return pendingPartyInvites.containsKey(p);
    }

    public Map<Player, Golfer> getGolfers()
    {
        return players;
    }

    public Golfer getGolfer(Player p)
    {
        return players.get(p);
    }

    public boolean isPlayerGolfing(Player p)
    {
        return players.containsKey(p);
    }

    public void removeGolfer(Player p)
    {
        getGolfer(p).cleanUp();
        players.remove(p);
    }

    public void addGolfer(Player player, Golfer gPlayer)
    {
        this.players.put(player, gPlayer);
    }

    public Party createParty(Player player){
        if(getPlayersParty(player) != null)
            return null;
        Party p = new Party();
        p.joinParty(player);
        parties.add(p);
        return p;
    }

    public Party getPlayersParty(Player p)
    {
        for(Party party : parties)
            if(party.getPlayers().contains(p))
                return party;
        return null;
    }

    public void removeParty(Party p)
    {
        parties.remove(p);
    }

    public void addPlayerToParty(Player p, Party party)
    {
        party.joinParty(p);
    }

    public ScoreCard getScoreCardForPlayer(Player p)
    {
        return getPlayersParty(p).getScoreCard();
    }
}
