package spinalcraft.minigolf.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.player.Golfer;
import spinalcraft.minigolf.utils.Messages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CosmeticsCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player)sender;

        if(args.length == 0)
        {
            return false;
        }
        Golfer golfer;

        if(Minigolf.playerManager.isPlayerGolfing(p))
            golfer = Minigolf.playerManager.getGolfer(p);
        else
            golfer = Minigolf.fUtils.loadPlayerFile(p, null);


        if(args.length == 2 && args[0].equals("ball"))
        {
            String materialName = args[1];
            Material mat = Material.getMaterial(materialName);
            if(mat == null)
            {
                p.sendMessage(Messages.makeMessage(Messages.CosmeticInvalidMaterial.replace("MATERIAL", materialName)));
                return true;
            }
            golfer.getBall().setBallSkin(new ItemStack(mat, 1));
            Minigolf.fUtils.savePlayerFile(p, golfer);

            p.sendMessage(Messages.makeMessage(Messages.CosmeticSavedMaterial.replace("ITEM","Ball").replace("MATERIAL",materialName)));
            return true;
        }

        if(args.length == 2 && args[0].equals("club"))
        {
            String materialName = args[1];
            Material mat = Material.getMaterial(materialName);
            if(mat == null)
            {
                p.sendMessage(Messages.makeMessage(Messages.CosmeticInvalidMaterial.replace("MATERIAL",materialName)));
                return true;
            }
            golfer.getClub().setClubSkin(new ItemStack(mat, 1));
            Minigolf.fUtils.savePlayerFile(p, golfer);

            p.sendMessage(Messages.makeMessage(Messages.CosmeticSavedMaterial.replace("ITEM","Club").replace("MATERIAL",materialName)));

            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1)
            return Arrays.asList("ball", "club");
        if(args.length == 2)
        {
            List<String> mats = new LinkedList<>();
            for(Material mat : Material.values())
            {
                mats.add(mat.name());
            }
            return mats;
        }
        return null;
    }
}
