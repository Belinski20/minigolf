package spinalcraft.minigolf.golf;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Club {

    private ItemStack clubSkin;

    public Club(Material clubSkin)
    {
        this.clubSkin = new ItemStack(clubSkin);
    }

    public ItemStack getClubSkin()
    {
        return clubSkin;
    }

}
