package spinalcraft.minigolf.golf.interactiveblocks;

import org.bukkit.Location;
import spinalcraft.minigolf.player.Golfer;

public interface Interactivable {

    Location[] getLocations();
    InteractiveType getType();
    void ApplyEffect(Golfer golfer);

}
