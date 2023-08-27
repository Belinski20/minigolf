package spinalcraft.minigolf.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spinalcraft.minigolf.Minigolf;

import java.util.List;

public class ScoreBoardCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player)sender;

        if(!Minigolf.playerManager.isPlayerGolfing(p))
            return true;
        Minigolf.playerManager.getPlayersParty(p).getScoreCard().createScoreCard(Minigolf.playerManager.getPlayersParty(p));
        Minigolf.playerManager.getPlayersParty(p).forceOpenScorecard();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
