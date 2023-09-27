package spinalcraft.minigolf.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.golf.CourseCreation;
import spinalcraft.minigolf.utils.Messages;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateCourseCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player)sender;

        // Open course is editing a course and just typing /cc
        if(Minigolf.courseManager.isCreatingCourse(p) && args.length == 0)
        {
            Minigolf.courseManager.getCourseCreation(p).openCourseInv(p);
            return true;
        }

        if(args.length == 0)
        {
            return true;
        }

        if(args.length == 2 && args[0].equals("new"))
        {
            if(Minigolf.courseManager.isCreatingCourse(p))
            {
                p.sendMessage(Messages.makeMessage(Messages.CCEditingCourseAlready));
                return true;
            }
            CourseCreation cc = new CourseCreation(args[1]);
            Minigolf.courseManager.addCourseCreation(p, cc);
            cc.openCourseInv(p);
            return true;
        }

        if(args[0].equals("save"))
        {
            if(Minigolf.courseManager.saveCourse(p))
            {

                //Minigolf.courseManager.getCourseCreation(p).unregister();
                p.sendMessage(Messages.makeMessage(Messages.CCCourseSaved));
                return true;
            }
            p.sendMessage(Messages.makeMessage(Messages.CCNoCoursePending));
            return true;
        }

        if(args[0].equals("edit") && args.length == 2)
        {
            if(Minigolf.courseManager.editCourse(p, args[1]))
            {
                Minigolf.courseManager.getCourseCreation(p).openCourseInv(p);
                return true;
            }
            p.sendMessage(Messages.makeMessage(Messages.CCCourseNotExist.replace("COURSE", args[1])));
            return true;
        }

        if(args[0].equals("quit"))
        {
            if(Minigolf.courseManager.removeCourseInCreation(p))
            {
                //Minigolf.courseManager.getCourseCreation(p).unregister();
                return true;
            }
            p.sendMessage(Messages.makeMessage(Messages.CCNoCoursePending));
            return true;
        }

        if(args[0].equals("setlobby"))
        {
            Minigolf.fUtils.saveConfigFileForLobby(p.getLocation());
            p.sendMessage(Messages.makeMessage(Messages.CCLobbySet));
            return true;
        }

        if(args[0].equals("green"))
        {
            if(args.length == 1)
            {
                for(String gr : Minigolf.courseManager.getGreens())
                    p.sendMessage(Messages.makeMessage(gr));
                return true;
            }

            if(args.length != 3)
            {
                return true;
            }

            if(args[1].equals("add"))
            {
                if(Material.getMaterial(args[2].toUpperCase()) != null)
                {
                    if(!Minigolf.courseManager.getGreens().contains(args[2].toUpperCase()))
                    {
                        Minigolf.courseManager.addGreen(args[2].toUpperCase());
                        // added new green
                        p.sendMessage(Messages.makeMessage(Messages.CCGreenAdded.replace("MATERIAL", args[2])));
                        return true;
                    }
                    // green already exists
                    p.sendMessage(Messages.makeMessage(Messages.CCGreenExists.replace("MATERIAL", args[2])));
                    return true;
                }
                // Invalid Material
                p.sendMessage(Messages.makeMessage(Messages.CosmeticInvalidMaterial.replace("MATERIAL", args[2])));
                return true;
            }
            if(args[1].equals("remove"))
            {
                if(Material.getMaterial(args[2].toUpperCase()) != null)
                {
                    if(Minigolf.courseManager.getGreens().contains(args[2].toUpperCase()))
                    {
                        Minigolf.courseManager.removeGreen(args[2].toUpperCase());
                        // removed a green
                        p.sendMessage(Messages.makeMessage(Messages.CCGreenRemove.replace("MATERIAL", args[2])));
                        return true;
                    }
                    // green does not exist
                    p.sendMessage(Messages.makeMessage(Messages.CCGreenRemoveDoesNotExist.replace("MATERIAL", args[2])));
                    return true;
                }
                // Invalid Material
                p.sendMessage(Messages.makeMessage(Messages.CosmeticInvalidMaterial.replace("MATERIAL", args[2])));
                return true;
            }
            p.sendMessage(Messages.makeMessage(Messages.CCGreenInvalidArguments));
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1)
            return Arrays.asList("new", "save", "edit", "quit", "green", "setlobby");
        if(args.length == 2 && args[0].equals("new"))
            return Arrays.asList("<Course Name>");
        if(args.length == 2 && args[0].equals("edit"))
            return Minigolf.courseManager.getCourses();
        if(args.length == 2 && args[0].equals("green"))
            return Arrays.asList("add", "remove");
        if(args.length == 3 && args[0].equals("green"))
        {
            if(args[1].equals("add"))
            {
                return Stream.of(Material.values()).map(Material::name).filter(material -> material.startsWith(args[2].toUpperCase())).collect(Collectors.toList());
            }
            if(args[1].equals("remove"))
            {
                return Minigolf.courseManager.getGreens();
            }
        }
        return null;
    }
}
