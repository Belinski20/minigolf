package spinalcraft.minigolf.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import spinalcraft.minigolf.golf.CourseCreation;
import spinalcraft.minigolf.golf.Hole;

public class UndoHoleEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public UndoHoleEvent(Hole hole, CourseCreation creation)
    {
        creation.removeHole(hole);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
