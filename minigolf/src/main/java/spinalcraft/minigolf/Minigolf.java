package spinalcraft.minigolf;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import spinalcraft.minigolf.commands.*;
import spinalcraft.minigolf.events.MinigolfListener;
import spinalcraft.minigolf.player.CourseManager;
import spinalcraft.minigolf.player.PlayerManager;
import spinalcraft.minigolf.utils.FileUtils;

public final class Minigolf extends JavaPlugin {
    public static PlayerManager playerManager;
    public static Plugin m;
    public static FileUtils fUtils;
    public static CourseManager courseManager;

    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners){
        for(Listener listener: listeners)
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void onEnable() {
        m = this;
        fUtils = new FileUtils();
        playerManager = new PlayerManager();
        courseManager = new CourseManager();
        registerEvents(this, new MinigolfListener());
        getCommand("tpb").setExecutor(new TpBall());
        getCommand("start").setExecutor(new StartGolfCommand());
        getCommand("party").setExecutor(new PartyCommand());
        getCommand("cc").setExecutor(new CreateCourseCommand());

    }

    @Override
    public void onDisable() {
        fUtils.saveConfigFile();
    }
}
