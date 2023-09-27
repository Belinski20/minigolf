package spinalcraft.minigolf.golf.interactiveblocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.events.BallMovementEvent;
import spinalcraft.minigolf.golf.Ball;
import spinalcraft.minigolf.player.Golfer;

public class SpeedUpInteractive implements Interactivable{
    private float extendRange;

    public SpeedUpInteractive(float extendRange)
    {
        this.extendRange = extendRange;
    }

    @Override
    public Location[] getLocations() {
        return null;
    }

    @Override
    public InteractiveType getType() {
        return InteractiveType.SPEEDUP;
    }

    @Override
    public void ApplyEffect(Golfer golfer) {
        if(golfer == null)
            return;
        if(golfer.getBall() == null)
            return;
        Ball b = golfer.getBall();
        Block block = b.getLocation().subtract(0,1,0).getBlock();
        Player p = Minigolf.playerManager.getPlayerFromGolfer(golfer);
        if(p == null)
            return;
        b.getLocation().getWorld().playSound(b.getLocation(), Sound.ENTITY_GUARDIAN_AMBIENT_LAND, 10f, 1);
        Bukkit.getServer().getPluginManager().callEvent(new BallMovementEvent(p, extendRange, block));
    }
}
