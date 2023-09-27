package spinalcraft.minigolf.utils;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import spinalcraft.minigolf.golf.Course;
import spinalcraft.minigolf.golf.Hole;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.player.Golfer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {

    private static Plugin plugin = Minigolf.m;
    private String playerFileLocation = plugin.getDataFolder() + File.separator + "users";
    private String courseFileLocation = plugin.getDataFolder() + File.separator + "courses";

    public FileUtils()
    {
        createDirectory();
    }

    private void createDirectory()
    {
        plugin.getDataFolder().mkdir();

        String names[] = {"users", "courses"};
        for(String name : names)
        {
            File file = new File(plugin.getDataFolder(), name);
            if(!file.exists())
                if(file.mkdirs())
                    plugin.getServer().sendMessage(Messages.makeMessage(name + " directory created"));
                else
                    plugin.getServer().sendMessage(Messages.makeMessage("Failed to create the " + name + " directory"));

        }
    }

    public void createPlayerFile(Player p){
        FileConfiguration config;
        File file = new File(playerFileLocation, p.getUniqueId() + ".yml");
        try {
            if(file.createNewFile())
            {
                config = YamlConfiguration.loadConfiguration(file);
                config.set("BallSkin", Material.SNOWBALL.toString());
                config.set("ClubSkin", Material.WOODEN_HOE.toString());
                config.save(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createConfigFile(){
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            if(file.createNewFile())
            {
                config = YamlConfiguration.loadConfiguration(file);
                config.set("Green", Arrays.asList(Material.GREEN_CONCRETE_POWDER.toString(), Material.GREEN_CONCRETE.toString()));
                config.save(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfigFileForGreens(){
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "config.yml");
        if(file.exists())
        {
            config = YamlConfiguration.loadConfiguration(file);
            config.set("Green", Minigolf.courseManager.getGreens());
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveConfigFileForLobby(Location loc){
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "config.yml");
        if(file.exists())
        {
            config = YamlConfiguration.loadConfiguration(file);
            config.set("Lobby", loc);
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public List<String> loadConfigFileForGreens(){
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(file);
        if(file.exists())
        {
            return config.getStringList("Green");
        }
        return Arrays.asList(Material.GREEN_CONCRETE_POWDER.toString(), Material.GREEN_CONCRETE.toString());
    }

    public Location loadConfigFileForLobby(){
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(file);
        if(file.exists())
        {
            return config.getLocation("Lobby");
        }
        return null;
    }

    public void savePlayerFile(Player p, Golfer g){
        FileConfiguration config;
        File file = new File(playerFileLocation, p.getUniqueId() + ".yml");
        if(file.exists())
        {
            config = YamlConfiguration.loadConfiguration(file);
            config.set("BallSkin", g.getBall().getSkin().getType().toString());
            config.set("BallModelID", g.getBall().getSkin().getItemMeta().getCustomModelData());
            config.set("ClubSkin", g.getClub().getClubSkin().getType().toString());
            config.set("ClubModelID", g.getClub().getClubSkin().getItemMeta().getCustomModelData());
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Golfer loadPlayerFile(Player p, Course c){
        FileConfiguration config;
        File file = new File(playerFileLocation, p.getUniqueId() + ".yml");
        Golfer g = new Golfer(Material.SNOWBALL, Material.WOODEN_HOE, c);
        if(file.exists())
        {
            config = YamlConfiguration.loadConfiguration(file);
            Material bsMat = Material.valueOf(config.getString("BallSkin"));
            int bModel = config.getInt("BallModelID");
            Material csMat = Material.valueOf(config.getString("ClubSkin"));
            int cModel = config.getInt("ClubModelID");
            g = new Golfer(csMat, bsMat, c);
            g.setCustomModels(bModel, cModel);
        }
        else
            createPlayerFile(p);
        return g;
    }

    public List<String> getCourseNames()
    {
        List<String> names = new LinkedList<>();
        File file = new File(courseFileLocation);
        File[] files = file.listFiles();
        for(File f : files)
            names.add(f.getName().replace(".yml", ""));

        return names;
    }

    public void createCourseFile(Course c){
        FileConfiguration config;
        File file = new File(courseFileLocation, c.getName() + ".yml");
        try {
            if(file.exists())
                file.delete();

            if(file.createNewFile())
            {
                config = YamlConfiguration.loadConfiguration(file);
                config.set("CourseName", c.getName());

                for(Hole hole : c.getHoles())
                {
                    Location loc = hole.getLoc();
                    config.set("holes." + hole.getName() + ".w", loc.getWorld().getName());
                    config.set("holes." + hole.getName() + ".x", loc.getX());
                    config.set("holes." + hole.getName() + ".y", loc.getY());
                    config.set("holes." + hole.getName() + ".z", loc.getZ());
                    config.set("holes." + hole.getName() + ".par", hole.getPar());
                }
                config.save(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Course loadCourseFile(String name){
        FileConfiguration config;
        File file = new File(courseFileLocation, name + ".yml");
        Course c = null;
        if(file.exists())
        {
            config = YamlConfiguration.loadConfiguration(file);
            c = new Course(config.getString("CourseName"));

            for(String s : config.getConfigurationSection("holes").getKeys(false))
            {
                World w = Bukkit.getWorld(config.getString("holes." + s + ".w"));
                double x = config.getDouble("holes." + s + ".x");
                double y = config.getDouble("holes." + s + ".y");
                double z = config.getDouble("holes." + s + ".z");
                int par = config.getInt("holes." + s + ".par");
                Location loc = new Location(w, x, y, z);
                Hole hole = new Hole(s, par, loc);
                c.addHole(hole);
            }
        }
        return c;
    }


}
