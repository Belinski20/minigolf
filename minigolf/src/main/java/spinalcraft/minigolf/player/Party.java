package spinalcraft.minigolf.player;

import org.bukkit.World;
import org.bukkit.entity.Player;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.golf.Course;
import spinalcraft.minigolf.golf.Hole;
import spinalcraft.minigolf.golf.ScoreCard;
import spinalcraft.minigolf.utils.Messages;

import java.util.ArrayList;

public class Party {
    private int partySize = 5;
    private int currentCourse = 1;
    private ArrayList<Player> players;
    private ScoreCard scoreCard;

    public Party()
    {
        players = new ArrayList<>();
    }

    public boolean canJoinParty()
    {
        return players.size() < partySize;
    }

    public void joinParty(Player p)
    {
        players.add(p);
    }

    public void leaveParty(Player p)
    {
        Golfer g = Minigolf.playerManager.getGolfer(p);
        if( g != null)
        {
            g.cleanUp();
            p.getInventory().clear();
            Minigolf.playerManager.removeGolfer(p);
            Minigolf.playerManager.teleportToLobby(p);
        }
        players.remove(p);
        if(players.size() == 0)
        {
            if(scoreCard != null)
                scoreCard.unregister();
            Minigolf.playerManager.removeParty(this);
        }
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public int getCurrentCourse()
    {
        return currentCourse - 1;
    }

    public void initliazeScoreCard()
    {
        scoreCard = new ScoreCard(this);
    }

    public void forceOpenScorecard(Player p)
    {
        scoreCard.openGUI(p);
    }

    public boolean isDoneWithHole()
    {
        for(Player p : players)
        {
            if(!Minigolf.playerManager.getGolfer(p).getCourse().getHoleByNumber(getCurrentCourse()).isComplete())
                return false;
        }
        return true;
    }

    public void incrementHole()
    {
        currentCourse += 1;
    }

    public void teleportToNextHole()
    {
        for(Player p : players)
        {
            Hole hole = Minigolf.playerManager.getGolfer(p).getCourse().getHoleByNumber(getCurrentCourse());
            p.teleport(hole.getLoc());
            p.sendActionBar(Messages.makeMessage(Messages.NextHoleInfo.replace("HOLE", hole.getName()).replace("AMOUNT", Integer.toString(hole.getPar()))));
            Minigolf.playerManager.getGolfer(p).placeBall(this, getCurrentCourse());
        }
    }

    public void teleportPlayerToFirstHole(Player p)
    {
        p.teleport(Minigolf.playerManager.getGolfer(p).getCourse().getHoleByNumber(getCurrentCourse()).getLoc());
        Minigolf.playerManager.getGolfer(p).placeBall(this, getCurrentCourse());
        Minigolf.playerManager.getGolfer(p).giveGolferClub(p);
    }

    public boolean isCourseDone(Golfer golfer)
    {
        return currentCourse >= golfer.getCourse().getHoles().size();
    }

    public World getWorld()
    {
        return players.get(0).getWorld();
    }

    public ScoreCard getScoreCard()
    {
        return scoreCard;
    }
}
