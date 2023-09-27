package spinalcraft.minigolf.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.player.Golfer;
import spinalcraft.minigolf.utils.Messages;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CosmeticsCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if ((sender instanceof Player))
            return true;
        if(args.length < 3)
        {
            return false;
        }
        Golfer golfer;

        Player p = Bukkit.getPlayer(args[0]);
        if(p == null)
            return true;

        if(Minigolf.playerManager.isPlayerGolfing(p))
            golfer = Minigolf.playerManager.getGolfer(p);
        else
            golfer = Minigolf.fUtils.loadPlayerFile(p, null);


        if(args.length >= 3 && args[1].equals("ball"))
        {
            int modelID = 0;
            if(args.length == 4)
            {
                try
                {
                    modelID = Integer.parseInt(args[3]);
                }
                catch(NumberFormatException e)
                {
                    p.sendMessage(Messages.makeMessage(Messages.CosmeticInvalidModelID));
                }
            }

            String materialName = args[2];
            Material mat = Material.getMaterial(materialName.toUpperCase());
            if(mat == null)
            {
                p.sendMessage(Messages.makeMessage(Messages.CosmeticInvalidMaterial.replace("MATERIAL", materialName)));
                return true;
            }
            ItemStack ball = new ItemStack(mat, 1);
            ItemMeta bMeta = ball.getItemMeta();
            bMeta.setCustomModelData(modelID);
            ball.setItemMeta(bMeta);

            golfer.getBall().setSkin(ball);
            Minigolf.fUtils.savePlayerFile(p, golfer);

            p.sendMessage(Messages.makeMessage(Messages.CosmeticSavedMaterial.replace("ITEM","Ball").replace("MATERIAL",materialName)));
            return true;
        }

        if(args.length >= 3 && args[1].equals("club"))
        {
            int modelID = 0;
            if(args.length == 4)
            {
                try
                {
                    modelID = Integer.parseInt(args[3]);
                }
                catch(NumberFormatException e)
                {
                    p.sendMessage(Messages.makeMessage(Messages.CosmeticInvalidModelID));
                }
            }

            String materialName = args[2];
            Material mat = Material.getMaterial(materialName.toUpperCase());
            if(mat == null)
            {
                p.sendMessage(Messages.makeMessage(Messages.CosmeticInvalidMaterial.replace("MATERIAL",materialName)));
                return true;
            }
            ItemStack club = new ItemStack(mat, 1);
            ItemMeta cMeta = club.getItemMeta();
            cMeta.setCustomModelData(modelID);
            club.setItemMeta(cMeta);

            golfer.getClub().setClubSkin(club);
            Minigolf.fUtils.savePlayerFile(p, golfer);

            p.sendMessage(Messages.makeMessage(Messages.CosmeticSavedMaterial.replace("ITEM","Club").replace("MATERIAL",materialName)));

            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2)
            return Arrays.asList("ball", "club");
        if(args.length == 3)
        {
            return Stream.of(Material.values()).map(Material::name).filter(material -> material.startsWith(args[2].toUpperCase())).collect(Collectors.toList());
        }
        if(args.length == 4)
        {
            return Arrays.asList("<Custom Model ID>");
        }
        return null;
    }
}
