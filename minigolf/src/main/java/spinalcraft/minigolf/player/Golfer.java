package spinalcraft.minigolf.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import spinalcraft.minigolf.golf.Ball;
import spinalcraft.minigolf.golf.Club;
import spinalcraft.minigolf.golf.Course;
import spinalcraft.minigolf.golf.Hole;

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


    public void devCreateCourses()
    {
        course = new Course("Test");
        Hole hole1 = new Hole("Hole 1", 5, null);
        Hole hole2 = new Hole("Hole 2", 5, null);
        Hole hole3 = new Hole("Hole 3", 5, null);
        Hole hole4 = new Hole("Hole 4", 5, null);
        Hole hole5 = new Hole("Hole 5", 5, null);
        Hole hole6 = new Hole("Hole 6", 5, null);
        Hole hole7 = new Hole("Hole 7", 5, null);
        Hole hole8 = new Hole("Hole 8", 5, null);
        course.addHole(hole1);
        course.addHole(hole2);
        course.addHole(hole3);
        course.addHole(hole4);
        course.addHole(hole5);
        course.addHole(hole6);
        course.addHole(hole7);
        course.addHole(hole8);

    }


}
