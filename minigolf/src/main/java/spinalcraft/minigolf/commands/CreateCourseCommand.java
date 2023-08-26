package spinalcraft.minigolf.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.golf.CourseCreation;

import java.util.Arrays;
import java.util.List;

public class CreateCourseCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player)sender;

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
            CourseCreation cc = new CourseCreation(args[0]);
            Minigolf.courseManager.addCourseCreation(p, cc);
            cc.openCourseInv(p);
            return true;
        }

        if(args[0].equals("save"))
        {
            if(Minigolf.courseManager.saveCourse(p))
            {
                //saved course
                return true;
            }
            //no course made for ptt
            return true;
        }

        if(args[0].equals("edit") && args.length == 2)
        {
            if(Minigolf.courseManager.editCourse(p, args[1]))
            {
                //course exists
                return true;
            }
            //course does not exist
            return true;
        }

        if(args[0].equals("quit"))
        {
            if(Minigolf.courseManager.removeCourseInCreation(p))
            {
                // course removed
                return true;
            }
            //not editing a course
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1)
            return Arrays.asList("new", "save", "edit", "quit");
        if(args.length == 2 && args[0] == "new")
            return Arrays.asList("<Course Name>");
        if(args.length == 2 && args[0] == "edit")
            return Minigolf.courseManager.getCourses();
        return null;
    }
}
