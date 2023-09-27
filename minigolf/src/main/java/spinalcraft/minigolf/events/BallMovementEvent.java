package spinalcraft.minigolf.events;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import spinalcraft.minigolf.golf.Ball;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.golf.interactiveblocks.SpeedUpInteractive;

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
        ball.setOrigin(originPosition);

        MovementEvent(ball, p, dir, originPosition, power);
    }

    public BallMovementEvent(Player p, float power, Block b)
    {
        Ball ball = Minigolf.playerManager.getGolfer(p).getBall();
        Vector dir = getDirection(b);
        Location originPosition = ball.getOriginLocation();
        if(originPosition == null)
            ball.getLocation();

        MovementEvent(ball, p, dir, originPosition, power);
    }

    private Vector getDirection(Block b)
    {
        if((b.getBlockData() instanceof Directional))
        {
            Directional dir = (Directional) b.getBlockData();
            return dir.getFacing().getOppositeFace().getDirection();
        }
        return new Vector(0,0,0);
    }

    public void MovementEvent(Ball b, Player p, Vector dir, Location originPosition, float power)
    {
        new BukkitRunnable()
        {
            float range = power;
            boolean onSlope = false;
            boolean rollingDown = false;
            Vector d = dir.clone();
            Item pball = b.getBall();

            @Override
            public void run()
            {
                Vector downwardForce = new Vector(0,0,0);

                if(Minigolf.playerManager.getGolfer(p) == null)
                {
                    this.cancel();
                    return;
                }

                if(range <= 0)
                {
                    Minigolf.playerManager.getGolfer(p).setCanHit(true);
                    this.cancel();
                }

                if(isInCauldron(pball.getLocation(), p))
                {
                    Minigolf.playerManager.getGolfer(p).setCanHit(true);
                    Bukkit.getServer().getPluginManager().callEvent(new HoleInEvent(p));
                    this.cancel();
                }

                if(!Minigolf.courseManager.getGreens().contains(pball.getLocation().subtract(0,1,0).getBlock().getType().toString()))
                {
                    pball.setVelocity(new Vector(0,0,0));
                    pball.teleport(originPosition);
                    Minigolf.playerManager.getGolfer(p).setCanHit(true);
                    this.cancel();
                    return;
                }

                //Speedup if the block under it a directional block such as magenta glazed terracotta, not including rails
                if(pball.getLocation().subtract(0,1,0).getBlock().getBlockData() instanceof Directional)
                {
                    if(!pball.getLocation().subtract(0,1,0).getBlock().getType().equals(Material.RAIL))
                    {
                        new SpeedUpInteractive(25).ApplyEffect(Minigolf.playerManager.getGolfer(p));
                        this.cancel();
                    }
                }

                // If Air is a green then allow ball to fall if in air
                if(pball.getLocation().subtract(0,1,0).getBlock().getType() == Material.AIR)
                    downwardForce = new Vector(0,-.6,0);

                float speed = getBallSpeed(power, range);

                Block targetBlock = pball.getLocation().add(d.clone().multiply(0.5f)).getBlock();

                //Slope logic
                Block block = isGoingToBeOnRail(pball, d.clone());

                if(block != null)
                {
                    Location loc = getTeleportLocation(pball, block);
                    if(loc != null)
                    {
                        if(range <= 3 && onSlope && !rollingDown)
                        {
                            d = new Vector(-d.getX(), -d.getY(), -d.getZ());
                            rollingDown = true;
                        }
                        if(rollingDown && range <= 3)
                            range += 1;
                        if(!onSlope)
                        {
                            onSlope = true;
                            range = range / 2;
                        }
                        //pball.setGravity(false);
                        pball.teleport(loc);
                    }
                }
                else
                {
                    onSlope = false;
                    //pball.setGravity(true);
                }

                //End Slope Logic

                // Collision with course barrier
                if(!targetBlock.getType().equals(Material.AIR) && !onSlope)
                {
                    rollingDown = false;
                    Block b = pball.getLocation().add(d.clone()).getBlock();
                    BlockFace bFace = b.getFace(pball.getLocation().getBlock());
                    Vector v = getNormal(bFace);
                    if(b.getLocation().add(v).getBlock().getType() != Material.AIR)
                    {
                        b = b.getLocation().add(v).getBlock();
                        bFace = b.getFace(pball.getLocation().getBlock());
                        v = getNormal(bFace);
                    }
                    d = DirectionChange(d, v);
                }
                Vector applyDirection = d.clone();
                pball.setVelocity(applyDirection.multiply(speed).add(downwardForce));
                range -= 1;
            }
        }.runTaskTimer(Minigolf.m, 1,1);
    }

    private boolean isInCauldron(Location loc, Player p)
    {
        Block b = p.getWorld().getBlockAt(loc.add(0,.25f,0));

        return b.getType().equals(Material.CAULDRON) || b.getType().equals(Material.COMPOSTER);
    }

    public Block isGoingToBeOnRail(Item ball, Vector direction)
    {
        direction.multiply(0.5f);
        Block ballBlock = ball.getLocation().getBlock();
        Location ballLocation = ball.getLocation();
        Block ballCurrentPosition = ballLocation.clone().getBlock();
        Block ballNextMovementPosition = ballLocation.clone().add(direction).getBlock();
        Block ballNextMovementUnderPosition = ballBlock.getLocation().add(0,-1,0).getBlock();
        Block ballNextMovementAbovePosition = ballBlock.getLocation().add(0,1,0).getBlock();
        if(ballNextMovementAbovePosition.getType().equals(Material.RAIL))
        {
            return ballNextMovementAbovePosition;
        }
        if(ballNextMovementUnderPosition.getType().equals(Material.RAIL))
        {
            return ballNextMovementUnderPosition;
        }
        if(ballCurrentPosition.getType().equals(Material.RAIL))
        {
            return ballCurrentPosition;
        }
        if(ballNextMovementPosition.getType().equals(Material.RAIL))
        {
            return ballNextMovementPosition;
        }

        return null;
    }

    public Location getTeleportLocation(Item ball, Block block)
    {
        BlockData data = block.getBlockData();

        if(!(data instanceof Rail))
            return null;

        BlockFace bFace = block.getFace(ball.getLocation().getBlock());

        if(bFace == null)
            bFace = block.getFace(ball.getLocation().add(0, block.getY() - ball.getLocation().getY(), 0).getBlock());

        if(bFace == null)
            return null;

        Rail r = (Rail)data;

        switch(bFace)
        {
            case WEST:
                if(r.getShape().equals(Rail.Shape.ASCENDING_EAST))
                    return getYTeleportPosition(block, ball, bFace.getOppositeFace());
                break;
            case EAST:
                if(r.getShape().equals(Rail.Shape.ASCENDING_WEST))
                    return getYTeleportPosition(block, ball, bFace.getOppositeFace());
                break;
            case SOUTH:
                if(r.getShape().equals(Rail.Shape.ASCENDING_NORTH))
                    return getYTeleportPosition(block, ball, bFace.getOppositeFace());
                break;
            case NORTH:
                if(r.getShape().equals(Rail.Shape.ASCENDING_SOUTH))
                    return getYTeleportPosition(block, ball, bFace.getOppositeFace());
                break;
            case UP:
                if(r.getShape().equals(Rail.Shape.ASCENDING_EAST))
                    return getYTeleportPosition(block, ball, BlockFace.EAST);
                if(r.getShape().equals(Rail.Shape.ASCENDING_WEST))
                    return getYTeleportPosition(block, ball, BlockFace.WEST);
                if(r.getShape().equals(Rail.Shape.ASCENDING_NORTH))
                    return getYTeleportPosition(block, ball, BlockFace.SOUTH);
                if(r.getShape().equals(Rail.Shape.ASCENDING_SOUTH))
                    return getYTeleportPosition(block, ball, BlockFace.NORTH);
                break;
            case DOWN:
                if(r.getShape().equals(Rail.Shape.ASCENDING_EAST))
                    return getYTeleportPosition(block, ball, BlockFace.WEST);
                if(r.getShape().equals(Rail.Shape.ASCENDING_WEST))
                    return getYTeleportPosition(block, ball, BlockFace.EAST);
                if(r.getShape().equals(Rail.Shape.ASCENDING_NORTH))
                    return getYTeleportPosition(block, ball, BlockFace.NORTH);
                if(r.getShape().equals(Rail.Shape.ASCENDING_SOUTH))
                    return getYTeleportPosition(block, ball, BlockFace.SOUTH);
                break;
            case SELF:
            {
                return getYTeleportPosition(block, ball, bFace);
            }

        }
        return null;
    }

    private float getBallSpeed(float power, float remainingTravelPower)
    {
        float maxSpeed = 0.5f;
        return Math.min(((remainingTravelPower / power) * maxSpeed), maxSpeed);
    }

    //Ascending West 205 -> 204
    //Ascending East 204 -> 205
    //Ascending South 294 -> 295
    //Ascending north 295 -> 294
    private Location getYTeleportPosition(Block b, Item ball, BlockFace face)
    {
        double addedHeight = 0;
        double blockD = 0;
        double ballD = 0;
        double offset = .25;

        switch(face)
        {
            case NORTH:
                blockD = b.getLocation().getZ();
                ballD = ball.getLocation().getZ();
                addedHeight = ballD - blockD;
                addedHeight = 1 - addedHeight;
                break;
            case SOUTH:
                blockD = b.getLocation().getZ();
                ballD = ball.getLocation().getZ();
                addedHeight = ballD - blockD;
                break;
            case WEST:
                blockD = b.getLocation().getX();
                ballD = ball.getLocation().getX();
                addedHeight = ballD - blockD;
                addedHeight = 1 - addedHeight;
                break;
            case EAST:
                blockD = b.getLocation().getX();
                ballD = ball.getLocation().getX();
                addedHeight = ballD - blockD;
                break;
            case SELF:
                switch(((Rail)b.getBlockData()).getShape())
                {
                    case ASCENDING_SOUTH:
                        blockD = b.getLocation().getZ();
                        ballD = ball.getLocation().getZ();
                        addedHeight = ballD - blockD;
                        break;
                    case ASCENDING_NORTH:
                        blockD = b.getLocation().getZ();
                        ballD = ball.getLocation().getZ();
                        addedHeight = ballD - blockD;
                        addedHeight = 1 - addedHeight;
                        break;
                    case ASCENDING_WEST:
                        blockD = b.getLocation().getX();
                        ballD = ball.getLocation().getX();
                        addedHeight = ballD - blockD;
                        addedHeight = 1 - addedHeight;
                        break;
                    case ASCENDING_EAST:
                        blockD = b.getLocation().getX();
                        ballD = ball.getLocation().getX();
                        addedHeight = ballD - blockD;
                        break;
                    case NORTH_SOUTH:
                    case EAST_WEST:
                    case SOUTH_EAST:
                    case SOUTH_WEST:
                    case NORTH_WEST:
                    case NORTH_EAST:
                        addedHeight = 0;
                        break;
                }
                break;
        }
        if(addedHeight < 0)
            addedHeight = 0;
        if (addedHeight > 1)
            addedHeight = 1;

        return new Location(b.getWorld(), ball.getX(), b.getY() + addedHeight + offset, ball.getZ());
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
