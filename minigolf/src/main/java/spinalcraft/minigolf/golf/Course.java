package spinalcraft.minigolf.golf;

import java.util.ArrayList;
import java.util.LinkedList;

public class Course {

    private String name;
    private ArrayList<Hole> holes;

    public Course(String name)
    {
        this.name = name;
        holes = new ArrayList<>();
    }

    public Hole getHoleByNumber(int i)
    {
        if(i > holes.size())
            return null;
        return holes.get(i);
    }

    public int getGolferScore()
    {
        int score = 0;
        for(Hole h : holes)
            score += h.getStrokes();
        return score;
    }

    public int getCourseTotalPar()
    {
        int score = 0;
        for(Hole h : holes)
            score += h.getPar();
        return score;
    }

    public String getName()
    {
        return name;
    }

    public void addHole(Hole hole)
    {
        holes.add(hole);
    }

    public ArrayList<Hole> getHoles()
    {
        return holes;
    }
}
