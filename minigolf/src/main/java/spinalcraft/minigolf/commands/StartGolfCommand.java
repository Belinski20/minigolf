package spinalcraft.minigolf.commands;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.title.Title;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.golf.Course;
import spinalcraft.minigolf.player.Golfer;
import spinalcraft.minigolf.player.Party;
import spinalcraft.minigolf.utils.Messages;

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
                Golfer g = Minigolf.fUtils.loadPlayerFile(golfer, course);
                Minigolf.playerManager.addGolfer(golfer, g);
                party.initliazeScoreCard();
                startCountDown(p, party);
            }


            return true;
        }
        return true;
    }

    public void startCountDown(Player player, Party party)
    {
        Audience audience = player;
        String courseName = Minigolf.playerManager.getGolfer(player).getCourse().getName();
        new BukkitRunnable()
        {
            int seconds = 5;
            @Override
            public void run()
            {
                if(seconds <= 0)
                {
                    party.teleportPlayerToFirstHole(player);
                    audience.clearTitle();
                    this.cancel();
                }
                Title title = Title.title(Messages.makeMessage("Starting Golf : " + courseName), Messages.makeMessage("Joining in " + seconds + " seconds"));
                audience.showTitle(title);
                seconds -= 1;
            }
        }.runTaskTimer(Minigolf.m, 0,20);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1)
            return Minigolf.courseManager.getCourses();
        return null;
    }
}
