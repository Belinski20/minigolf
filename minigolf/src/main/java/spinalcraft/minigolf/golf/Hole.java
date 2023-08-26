package spinalcraft.minigolf.golf;

import org.bukkit.Location;

public class Hole {

    private String name;
    private int strokes;
    private int par;
    private Location loc;
    private boolean complete;

    public Hole(String name, int par, Location loc)
    {
        this.name = name;
        this.par = par;
        this.loc = loc;
        strokes = 0;
        complete = false;
    }

    public Hole(String name)
    {
        this.name = name;
        par = 0;
        strokes = 0;
        complete = false;
    }

    public void incrementStrokes()
    {
        strokes += strokes;
    }

    public boolean hasStrokedOut()
    {
        return strokes > 14;
    }
    public void setStrokes(int strokes)
    {
        this.strokes = strokes;
    }

    public int getStrokes()
    {
        return strokes;
    }

    public String getName()
    {
        return name;
    }

    public int getPar()
    {
        return par;
    }

    public Location getLoc()
    {
        return loc;
    }

    public boolean isComplete()
    {
        return complete;
    }

    public void setComplete()
    {
        complete = true;
    }

    public void setPar(int par)
    {
        this.par = par;
    }

    public void setLoc(Location loc)
    {
        this.loc = loc;
    }

}
