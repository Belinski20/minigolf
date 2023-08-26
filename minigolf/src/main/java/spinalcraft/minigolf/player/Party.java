package spinalcraft.minigolf.player;

import org.bukkit.entity.Player;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.golf.ScoreCard;

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
        players.remove(p);
        if(players.size() == 0)
            Minigolf.playerManager.removeParty(this);
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public int getCurrentCourse()
    {
        return currentCourse;
    }

    public void initliazeScoreCard()
    {
        scoreCard = new ScoreCard(this);
    }

    public void forceOpenScorecard()
    {
        for(Player p : players)
            scoreCard.openGUI(p);
    }

    public boolean isDoneWithHole()
    {
        boolean isDoneWithHole = true;
        for(Player p : players)
        {
            if(!Minigolf.playerManager.getGolfer(p).getCourse().getHoleByNumber(currentCourse).isComplete())
                isDoneWithHole = false;
        }
        return isDoneWithHole;
    }

    public void incrementHole()
    {
        currentCourse += 1;
    }

    public void teleportToNextHole()
    {
        for(Player p : players)
        {
            p.teleport(Minigolf.playerManager.getGolfer(p).getCourse().getHoleByNumber(currentCourse).getLoc());
        }
    }

    public ScoreCard getScoreCard()
    {
        return scoreCard;
    }

    public void setCurrentCourse(int currentCourse)
    {
        this.currentCourse = currentCourse;
    }
}
