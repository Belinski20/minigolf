package spinalcraft.minigolf.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import spinalcraft.minigolf.golf.Ball;
import spinalcraft.minigolf.Minigolf;

public class BallMovementEvent extends Event{
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    public BallMovementEvent(Player p, float power)
    {
        Ball ball = Minigolf.playerManager.getGolfer(p).getBall();
        Vector dir = ball.getOwner().getDirection(p);
        Location originPosition = ball.getBall().getLocation();

        new BukkitRunnable()
        {
            float range = power;
            Vector d = dir.clone();
            Item pball = ball.getBall();

            @Override
            public void run()
            {

                if(range <= 0)
                {
                    Minigolf.playerManager.getGolfer(p).setCanHit(true);
                    this.cancel();
                }

                if(isInCauldron(ball.getLocation(), p))
                {
                    Minigolf.playerManager.getGolfer(p).setCanHit(true);
                    Bukkit.getServer().getPluginManager().callEvent(new HoleInEvent(p));
                    this.cancel();
                }


                if(!Minigolf.courseManager.getGreens().contains(pball.getWorld().getBlockAt(pball.getLocation().subtract(0,1,0)).getType().toString()))
                {
                    pball.setVelocity(new Vector(0,0,0));
                    pball.teleport(originPosition);
                    Minigolf.playerManager.getGolfer(p).setCanHit(true);
                    this.cancel();
                    return;
                }

                float speed = getBallSpeed(power, range);

                Block targetBlock = pball.getWorld().getBlockAt(pball.getLocation().add(d.clone().multiply(0.5f)));

                if(!targetBlock.getType().equals(Material.AIR))
                {
                    Block b = pball.getLocation().add(d.clone()).getBlock();
                    BlockFace bFace = b.getFace(pball.getWorld().getBlockAt(pball.getLocation()));
                    Vector v = getNormal(bFace);
                    if(b.getLocation().add(v).getBlock().getType() != Material.AIR)
                    {
                        b = b.getLocation().add(v).getBlock();
                        bFace = b.getFace(pball.getWorld().getBlockAt(pball.getLocation()));
                        v = getNormal(bFace);
                    }
                    d = DirectionChange(d, v);
                }
                Vector applyDirection = d.clone();
                pball.setVelocity(applyDirection.multiply(speed));
                range -= 1;
            }
        }.runTaskTimer(Minigolf.m, 1,1);
    }

    private boolean isInCauldron(Location loc, Player p)
    {
        Block b = p.getWorld().getBlockAt(loc.add(0,.25f,0));

        return b.getType().equals(Material.CAULDRON) || b.getType().equals(Material.COMPOSTER);
    }

    private float getBallSpeed(float power, float remainingTravelPower)
    {
        float maxSpeed = 0.5f;
        return Math.min(((remainingTravelPower / power) * maxSpeed), maxSpeed);
    }

    /**
     * Rather than just using getModX on the block face for example, other than the 4 cardinal directions they cause issues with reflection.
     * @param face
     * @return
     */
    private Vector getNormal(BlockFace face)
    {
        Vector normal;

        switch(face)
        {

            case NORTH:
            case NORTH_EAST:
            case NORTH_WEST:
            case NORTH_NORTH_WEST:
            case NORTH_NORTH_EAST:
                normal = new Vector(0,0,-1);
                break;
            case EAST:
            case EAST_NORTH_EAST:
            case EAST_SOUTH_EAST:
                normal = new Vector(1,0,0);
                break;
            case SOUTH:
            case SOUTH_EAST:
            case SOUTH_WEST:
            case SOUTH_SOUTH_EAST:
            case SOUTH_SOUTH_WEST:
                normal = new Vector(0,0,1);
                break;
            case WEST:
            case WEST_NORTH_WEST:
            case WEST_SOUTH_WEST:
                normal = new Vector(-1,0,0);
                break;
            default:
                normal = new Vector(0,0,0);
                break;

        }
        return normal;
    }

    private Vector DirectionChange(Vector dir, Vector wall)
    {
        Vector direction = dir.clone();
        Vector wallBounce = wall.clone();
        Double dot = direction.dot(wallBounce);
        return direction.subtract(wallBounce.multiply(dot * 2));
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
