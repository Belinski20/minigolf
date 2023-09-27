package spinalcraft.minigolf.golf;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import spinalcraft.minigolf.player.Golfer;

public class Ball {

    private ItemStack skin;
    private Item ball;
    private Location lastInteractiveUsed;
    private Golfer owner;
    private float power;
    private Location origin;

    public Ball(Material ballSkin)
    {
        this.skin = new ItemStack(ballSkin);
    }

    public void setOwner(Golfer owner)
    {
        this.owner = owner;
    }

    public Location getLastInteractiveUsed()
    {
        return lastInteractiveUsed;
    }

    public void setLastInteractiveUsed(Location loc)
    {
        this.lastInteractiveUsed = loc;
    }

    public Golfer getOwner()
    {
        return owner;
    }

    public Location getLocation()
    {
        return ball.getLocation();
    }
    public Location getOriginLocation()
    {
        return origin;
    }
    public void setOrigin(Location origin)
    {
        this.origin = origin;
    }

    public Item getBall()
    {
        return ball;
    }

    public void setBall(Item ball)
    {
        this.ball = ball;
    }

    public ItemStack getSkin()
    {
        return skin;
    }
    public void setSkin(ItemStack skin)
    {
        this.skin = skin;
    }

    public void setPower(float power)
    {
        this.power = power;
    }

    public float getPower()
    {
        return power;
    }
}
