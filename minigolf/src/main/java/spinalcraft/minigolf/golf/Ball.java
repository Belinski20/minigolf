package spinalcraft.minigolf.golf;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import spinalcraft.minigolf.player.Golfer;

public class Ball {

    private ItemStack ballSkin;
    private Item ball;
    private Golfer owner;
    private float power;

    public Ball(Material ballSkin)
    {
        this.ballSkin = new ItemStack(ballSkin);
    }

    public void setOwner(Golfer owner)
    {
        this.owner = owner;
    }

    public Golfer getOwner()
    {
        return owner;
    }

    public Location getLocation()
    {
        return ball.getLocation();
    }

    public Item getBall()
    {
        return ball;
    }

    public void setBall(Item ball)
    {
        this.ball = ball;
    }

    public ItemStack getBallSkin()
    {
        return ballSkin;
    }
    public void setBallSkin(ItemStack ballSkin)
    {
        this.ballSkin = ballSkin;
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
