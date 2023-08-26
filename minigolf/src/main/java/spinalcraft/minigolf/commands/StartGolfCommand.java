package spinalcraft.minigolf.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.golf.Course;
import spinalcraft.minigolf.golf.ScoreCard;
import spinalcraft.minigolf.player.Golfer;
import spinalcraft.minigolf.player.Party;

import java.util.List;

public class StartGolfCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;

        if(args.length == 0)
            return true;

        if(!Minigolf.courseManager.isCourse(args[0]))
            return true;

        Player p = (Player)sender;

        if(!Minigolf.playerManager.isPlayerGolfing(p))
        {
            Party party = Minigolf.playerManager.getPlayersParty(p);
            if(party == null)
                party = Minigolf.playerManager.createParty(p);
            Course course = Minigolf.fUtils.loadCourseFile(args[0]);

            for(Player golfer : party.getPlayers())
            {
                Minigolf.fUtils.createPlayerFile(golfer);
                Golfer g = Minigolf.fUtils.loadPlayerFile(golfer, null);
                //g.devCreateCourses();
                Minigolf.playerManager.addGolfer(golfer, g);
                party.initliazeScoreCard();
                party.teleportToNextHole();
                //Jank Code
                g.getBall().setOwner(g);
                Item item = p.getWorld().dropItem(g.getCourse().getHoleByNumber(party.getCurrentCourse()).getLoc(), g.getBall().getBallSkin());
                item.setCanPlayerPickup(false);
                item.setUnlimitedLifetime(true);
                g.getBall().setBall(item);
            }

            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1)
            return Minigolf.courseManager.getCourses();
        return null;
    }
}
