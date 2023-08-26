package spinalcraft.minigolf.golf;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.events.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CourseCreation implements Listener {

    private int size = 9;
    private String name;
    private Map<String, Hole> holes;
    private Inventory courseInv;
    private Inventory holeInv;
    private Hole currentHole;


    public CourseCreation(String name)
    {
        this.name = name;
        holes = new HashMap<>();
        courseInv = Bukkit.createInventory(null, size, Component.text().content(name).build());
        createCourseInv();
        Bukkit.getServer().getPluginManager().registerEvents(this, Minigolf.m);
    }

    public CourseCreation(Course course)
    {
        this.name = course.getName();
        holes = new HashMap<>();
        for(Hole hole : course.getHoles())
            holes.put(hole.getName(), hole);
        courseInv = Bukkit.createInventory(null, size, Component.text().content(name).build());
        createCourseInv();
        Bukkit.getServer().getPluginManager().registerEvents(this, Minigolf.m);
    }

    public void openCourseInv(HumanEntity ent)
    {
        createCourseInv();
        ent.openInventory(courseInv);
    }

    public void openHoleInv(HumanEntity ent, Hole hole)
    {
        holeInv = Bukkit.createInventory(null, InventoryType.HOPPER, Component.text().content(hole.getName()).build());
        createHoleInv(hole);
        ent.openInventory(holeInv);
    }

    public String getCourseName()
    {
        return name;
    }

    public void saveCourse(Player p)
    {
        Course course = new Course(name);
        for(ItemStack item : courseInv.getStorageContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(item.getType().equals(Material.LIME_CONCRETE))
                continue;
            String name = PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName());
            course.addHole(holes.get(getHole(name)));
        }
        Minigolf.courseManager.addCourse(name);
        Minigolf.fUtils.createCourseFile(course);
        HandlerList.unregisterAll(this);
        courseInv.close();
        Minigolf.courseManager.removeCourseCreation(p);
    }

    public void createCourseInv()
    {
        courseInv.clear();
        courseInv.setItem(0, makeCourseAddButton());
        int index = 1;
        for(Hole h : holes.values())
        {
            courseInv.setItem(index, makeHoleItem(h));
            index++;
        }
    }

    private ItemStack makeCourseAddButton()
    {
        //Save Button
        ItemStack save = new ItemStack(Material.LIME_CONCRETE, 1);
        ItemMeta saveMeta = save.getItemMeta();
        saveMeta.displayName(Component.text().content("Add").build());
        save.setItemMeta(saveMeta);
        return save;
    }

    private ItemStack makeHoleItem(Hole h)
    {
        ItemStack holeItem = new ItemStack(Material.MOSS_BLOCK, 1);
        ItemMeta meta = holeItem.getItemMeta();
        meta.displayName(Component.text().content(h.getName()).build());
        holeItem.setItemMeta(meta);
        return holeItem;
    }

    public void saveHole(Hole hole)
    {
        holes.put(hole.getName(), hole);
    }

    private void createHoleInv(Hole hole)
    {
        //Save Button
        ItemStack save = new ItemStack(Material.LIME_CONCRETE, 1);
        ItemMeta saveMeta = save.getItemMeta();
        saveMeta.displayName(Component.text().content("Save").build());
        save.setItemMeta(saveMeta);
        //Par Snowball
        ItemStack par = new ItemStack(Material.SNOWBALL, 1);
        ItemMeta parMeta = par.getItemMeta();
        parMeta.displayName(Component.text().content("Par : " + hole.getPar()).build());
        parMeta.lore(Arrays.asList(Component.text().content("Click to set Par.").build()));
        par.setItemMeta(parMeta);
        //Tee Candle
        ItemStack tee = new ItemStack(Material.WHITE_CANDLE, 1);
        ItemMeta teeMeta = tee.getItemMeta();
        if(hole.getLoc() == null)
        {
            teeMeta.displayName(Component.text().content("Tee location not set yet.").build());
            teeMeta.lore(Arrays.asList(Component.text().content("Click to set tee.").build()));
        }
        else
        {
            teeMeta.displayName(Component.text().content("Tee : X:" + hole.getLoc().getX() + " Y: " + hole.getLoc().getY() + " Z: " + hole.getLoc().getZ()).build());
            teeMeta.lore(Arrays.asList(Component.text().content("Click to reset tee.").build()));
        }
        tee.setItemMeta(teeMeta);
        //Quit Button
        ItemStack quit = new ItemStack(Material.RED_CONCRETE, 1);
        ItemMeta quitMeta = quit.getItemMeta();
        quitMeta.displayName(Component.text().content("Quit").build());
        quitMeta.lore(Arrays.asList(Component.text().content("Will not save hole.").build()));
        quit.setItemMeta(quitMeta);
        holeInv.setItem(0, save);
        holeInv.setItem(1, par);
        holeInv.setItem(3, tee);
        holeInv.setItem(4, quit);
    }

    public void removeHole(Hole hole)
    {
        holes.remove(hole.getName());
    }

    public Hole getHole(String name)
    {
        return holes.get(name);
    }

    // Check for clicks on items in course inventory
    @EventHandler
    public void onCourseInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(courseInv)) return;
        if(e.getClickedInventory() == null) return;

        if(!e.getClickedInventory().equals(courseInv))
        {
            e.setCancelled(true);
            return;
        }

        if(e.getInventory().equals(courseInv))
        {
            ItemStack item = courseInv.getItem(e.getSlot());

            if(item == null)
                return;

            switch(item.getType())
            {
                case LIME_CONCRETE:
                    if(holes.size() >= 9)
                        return;
                    Bukkit.getServer().getPluginManager().callEvent(new AddHoleEvent(courseInv, e.getWhoClicked()));
                    break;
                case MOSS_BLOCK:
                    courseInv.close();
                    currentHole = getHole(PlainTextComponentSerializer.plainText().serialize(e.getCurrentItem().getItemMeta().displayName()));
                    Bukkit.getServer().getPluginManager().callEvent(new EditHoleEvent(e.getWhoClicked(), e.getCurrentItem()));
                    break;
            }
        }

        e.setCancelled(true);
    }

    // Check for clicks on items in Hole inventory
    @EventHandler
    public void onHoleInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(holeInv)) return;
        if(e.getClickedInventory() == null) return;

        if(!e.getClickedInventory().equals(holeInv))
        {
            e.setCancelled(true);
            return;
        }

        if(e.getInventory().equals(holeInv))
        {
            ItemStack item = holeInv.getItem(e.getSlot());

            if(item == null)
                return;

            Hole hole = currentHole;

            switch(item.getType())
            {
                // undo editing hole
                case RED_CONCRETE:
                    Bukkit.getServer().getPluginManager().callEvent(new UndoHoleEvent(hole, this));
                    holeInv.close();
                    break;
                // par
                case SNOWBALL:
                    Bukkit.getServer().getPluginManager().callEvent(new ParHoleEvent(hole, (Player)e.getWhoClicked()));
                    holeInv.close();
                    break;
                // place tee
                case WHITE_CANDLE:
                    Bukkit.getServer().getPluginManager().callEvent(new TeeHoleEvent((Player)e.getWhoClicked(), hole));
                    holeInv.close();
                    break;
                // save hole
                case LIME_CONCRETE:
                    Bukkit.getServer().getPluginManager().callEvent(new SaveHoleEvent((Player)e.getWhoClicked(), hole));
                    holeInv.close();
                    break;
            }
        }

        e.setCancelled(true);
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(courseInv) || e.getInventory().equals(holeInv)) {
            e.setCancelled(true);
        }
    }
}
