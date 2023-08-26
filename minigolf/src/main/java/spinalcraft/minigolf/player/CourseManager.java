package spinalcraft.minigolf.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.golf.Course;
import spinalcraft.minigolf.golf.CourseCreation;
import spinalcraft.minigolf.golf.Hole;

import java.util.*;

public class CourseManager {

    private ArrayList<String> courses;
    private List<String> greens;
    private Map<Player, CourseCreation> coursesInCreations;
    private Map<Player, Event> courseEvents;

    public CourseManager()
    {
        courses = new ArrayList<>();
        courseEvents = new HashMap<>();
        coursesInCreations = new HashMap<>();
        greens = new LinkedList<>();
        Minigolf.fUtils.createConfigFile();
        greens = Minigolf.fUtils.loadConfigFile();
    }

    public boolean isCreatingCourse(Player p)
    {
        return coursesInCreations.containsKey(p);
    }

    public CourseCreation getCourseCreation(Player p)
    {
        return coursesInCreations.get(p);
    }

    public void removeCourseCreation(Player p)
    {
        coursesInCreations.remove(p);
    }

    public void addCourseCreation(Player p, CourseCreation c)
    {
        coursesInCreations.put(p, c);
    }

    public void addCourseEvent(Player p, Event e)
    {
        courseEvents.put(p,e);
    }
    public void removeCourseEvent(Player p)
    {
        courseEvents.remove(p);
    }

    public boolean hasCourseEvent(Player p)
    {
        return courseEvents.containsKey(p);
    }

    public Event getCourseEvent(Player p)
    {
        return courseEvents.get(p);
    }

    public List<String> getGreens()
    {
        return greens;
    }

    public void addGreen(Material green)
    {
        greens.add(green.toString());
    }

    public void addCourse(String course)
    {
        courses.add(course);
    }

    public List<String> getCourses()
    {
        return courses;
    }

    public boolean isCourse(String course)
    {
        return courses.contains(course);
    }

    public boolean saveCourse(Player p)
    {
        if(!coursesInCreations.containsKey(p))
            return false;
        coursesInCreations.get(p).saveCourse(p);
        return true;
    }

    public boolean removeCourseInCreation(Player p)
    {
        if(!coursesInCreations.containsKey(p))
            return false;
        Course c = Minigolf.fUtils.loadCourseFile(coursesInCreations.get(p).getCourseName());
        if(c != null)
            courses.add(c.getName());
        coursesInCreations.remove(p);
        courseEvents.remove(p);
        return true;
    }

    public boolean editCourse(Player p, String name)
    {
        if(courses.contains(name))
        {
            courses.remove(name);
            coursesInCreations.put(p, new CourseCreation(Minigolf.fUtils.loadCourseFile(name)));
            return true;
        }
        return false;
    }

    public boolean setHoleTee(Course c, Location loc, int par, String name)
    {
        Hole hole = new Hole(name, par, loc);
        c.addHole(hole);
        return true;
    }

}
