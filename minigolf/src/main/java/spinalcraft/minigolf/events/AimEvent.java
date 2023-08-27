package spinalcraft.minigolf.events;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.player.Golfer;

public class AimEvent extends Event implements Cancellable {

    private boolean isCancelled;
    private float length = 3;
    private float speed = 0.05f;
    private float particleSize = 0.5f;
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public AimEvent(Player p)
    {
        isCancelled = false;

        new BukkitRunnable()
        {
            Golfer gp = Minigolf.playerManager.getGolfer(p);
            Location currentParticleLocation = gp.getBall().getLocation();

            @Override
            public void run()
            {

                if(isCancelled)
                    this.cancel();

                if(Minigolf.playerManager.getGolfer(p) == null)
                    this.cancel();


                if(!gp.getIsCharging())
                    this.cancel();



                Vector dir = gp.getDirection(p);
                gp.getBall().setPower((float)gp.getBall().getLocation().distance(currentParticleLocation) / length);

                spawnParticleTargetLocation(gp.getBall().getLocation(), dir, p);
                spawnPreviousParticle(gp.getBall().getLocation(), currentParticleLocation, p);
                currentParticleLocation = spawnParticle(gp.getBall().getLocation(), currentParticleLocation, dir, p);


            }
        }.runTaskTimer(Minigolf.m, 1,1);

    }

    private void spawnParticleTargetLocation(Location origin, Vector direction, Player p)
    {
        Particle.DustOptions opt= new Particle.DustOptions(Color.WHITE, 1f);
        p.getWorld().spawnParticle(Particle.REDSTONE, origin.add(direction.multiply(length)), 1, opt);
    }

    private Location spawnParticle(Location origin, Location current, Vector direction, Player p)
    {
        Location loc = getNextParticleLocation(origin, current, direction, speed);
        p.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, getDustColor(origin, current));
        return loc;
    }

    private Particle.DustOptions getDustColor(Location origin, Location current)
    {
        Particle.DustOptions opt= new Particle.DustOptions(Color.WHITE, particleSize);
        if(origin.distance(current) / length > 0.66)
            opt = new Particle.DustOptions(Color.RED, particleSize);
        else
        if(origin.distance(current) / length > 0.33)
            opt = new Particle.DustOptions(Color.YELLOW, particleSize);
        else
        if(origin.distance(current) / length >= 0)
            opt = new Particle.DustOptions(Color.GREEN, particleSize);
        return opt;
    }

    private void spawnPreviousParticle(Location origin, Location current, Player p)
    {
        p.getWorld().spawnParticle(Particle.REDSTONE, current, 1, getDustColor(origin, current));
    }

    private Location getNextParticleLocation(Location origin, Location current, Vector direction, float speed)
    {
        if(origin.distance(current) > length)
            return origin;

        return current.add(direction.multiply(speed));
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }
}
