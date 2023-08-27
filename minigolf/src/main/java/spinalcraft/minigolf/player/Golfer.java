package spinalcraft.minigolf.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import spinalcraft.minigolf.golf.Ball;
import spinalcraft.minigolf.golf.Club;
import spinalcraft.minigolf.golf.Course;

public class Golfer {

    private Club club;
    private Ball ball;
    private Course course;
    private boolean canHit;
    private boolean isCharging;

    public Golfer(Material club, Material ball, Course course)
    {
        this.club = new Club(club);
        this.ball = new Ball(ball);
        this.course = course;
        isCharging = false;
        canHit = true;
    }

    public Club getClub()
    {
        return club;
    }

    public Ball getBall()
    {
        return ball;
    }

    public Course getCourse()
    {
        return course;
    }

    public Vector getDirection(Player player)
    {
        Location ballLocation = getBall().getLocation();
        double x = ballLocation.getX() - player.getX();
        double z = ballLocation.getZ() - player.getZ();
        return new Vector(x, 0, z).normalize();
    }

    public boolean getIsCharging()
    {
        return isCharging;
    }

    public void setIsCharging(boolean isCharging)
    {
        this.isCharging = isCharging;
    }

    public boolean canHit()
    {
        return canHit;
    }

    public void setCanHit(boolean canHit)
    {
        this.canHit = canHit;
    }

    public void cleanUp()
    {
        if(ball.getBall() != null)
            ball.getBall().remove();
    }

    public void giveGolferClub(Player player)
    {
        player.getInventory().addItem(getClub().getClubSkin());
    }

    public void placeBall(Party p, int currentCourse)
    {
        ball.setOwner(this);
        Item b = p.getWorld().dropItem(getCourse().getHoleByNumber(currentCourse).getLoc().add(0.5, 0.5, 0.5), ball.getBallSkin());
        b.setVelocity(new Vector(0,0,0));
        b.setCanPlayerPickup(false);
        b.setUnlimitedLifetime(true);
        ball.setBall(b);
    }
}
