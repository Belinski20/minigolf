package spinalcraft.minigolf.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import spinalcraft.minigolf.golf.CourseCreation;
import spinalcraft.minigolf.golf.Hole;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.player.Golfer;
import spinalcraft.minigolf.player.Party;

public class MinigolfListener implements Listener {

    @EventHandler
    public void playerPrepBall(PlayerInteractEvent event) {
        if(!Minigolf.playerManager.isPlayerGolfing(event.getPlayer()))
            return;
        if(!event.hasItem())
            return;

        Golfer golfer = Minigolf.playerManager.getGolfer(event.getPlayer());

        Hole hole  = golfer.getCourse().getHoleByNumber(Minigolf.playerManager.getPlayersParty(event.getPlayer()).getCurrentCourse());

        if(hole.isComplete())
            return;

        if(event.getAction().isRightClick() && event.getItem().getType().equals(golfer.getClub().getClubSkin().getType()))
        {
            if(!golfer.getIsCharging() && golfer.canHit())
            {
                golfer.setIsCharging(true);
                Bukkit.getServer().getPluginManager().callEvent(new AimEvent(event.getPlayer()));
            }
        }

        if(event.getAction().isLeftClick() && event.getItem().getType().equals(golfer.getClub().getClubSkin().getType()))
        {
            if(golfer.getIsCharging())
            {
                golfer.setCanHit(false);
                golfer.setIsCharging(false);
                int currentCourse = Minigolf.playerManager.getScoreCardForPlayer(event.getPlayer()).getParty().getCurrentCourse();
                golfer.getCourse().getHoleByNumber(currentCourse).incrementStrokes();
                event.getPlayer().getWorld().playSound(golfer.getBall().getBall(), Sound.BLOCK_GLASS_HIT, 1f, 1);
                Bukkit.getServer().getPluginManager().callEvent(new BallMovementEvent(event.getPlayer(), ((float)(golfer.getBall().getPower() + 0.1) * 100)));
            }
        }
    }

    @EventHandler
    public void ballInHole(HoleInEvent event)
    {
        Player p = event.getPlayer();
        Golfer golfer = Minigolf.playerManager.getGolfer(p);
        event.getPlayer().getWorld().playSound(golfer.getBall().getBall(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 10f, 1);
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event)
    {
        Player p = event.getPlayer();
        if(Minigolf.playerManager.isPlayerGolfing(p))
        {
            Party party = Minigolf.playerManager.getPlayersParty(p);
            party.leaveParty(p);
            Minigolf.playerManager.removeGolfer(p);
        }
    }

    @EventHandler
    public void playerTextChatCapture(AsyncChatEvent event)
    {
        Player p = event.getPlayer();

        if(Minigolf.courseManager.hasCourseEvent(p))
        {
            Event e = Minigolf.courseManager.getCourseEvent(p);
            if(Minigolf.courseManager.getCourseEvent(p) instanceof AddHoleEvent)
            {
                AddHoleEvent addHoleEvent = (AddHoleEvent) e;
                Hole hole = new Hole(((TextComponent)event.message()).content());
                CourseCreation c = Minigolf.courseManager.getCourseCreation(p);
                c.saveHole(hole);
                new BukkitRunnable() {
                    @Override
                    public void run(){
                    c.openCourseInv(p);
                    this.cancel();
                }
            }.runTaskTimer(Minigolf.m, 0, 1);

                Minigolf.courseManager.removeCourseEvent(p);
                event.setCancelled(true);
            }
            if(Minigolf.courseManager.getCourseEvent(p) instanceof ParHoleEvent)
            {
                ParHoleEvent parHoleEvent = (ParHoleEvent) e;
                int par = 0;
                try{
                    par = Integer.valueOf(((TextComponent)event.message()).content());
                    parHoleEvent.getHole().setPar(par);
                    //successfully converted string to int
                }
                catch(NumberFormatException err)
                {
                    //failed to convert number
                }
                Minigolf.courseManager.removeCourseEvent(p);
                new BukkitRunnable() {
                    @Override
                    public void run(){
                        Minigolf.courseManager.getCourseCreation(p).openHoleInv(p, parHoleEvent.getHole());
                        this.cancel();
                    }
                }.runTaskTimer(Minigolf.m, 0, 1);

                return;
            }
        }
    }

    @EventHandler
    public void placeTee(BlockPlaceEvent event)
    {
        Player p = event.getPlayer();
        if(Minigolf.courseManager.hasCourseEvent(p))
        {
            if(!event.getBlock().getType().equals(Material.WHITE_CANDLE))
                return;

            Event e = Minigolf.courseManager.getCourseEvent(p);
            if(e instanceof TeeHoleEvent)
            {
                TeeHoleEvent teeHoleEvent = (TeeHoleEvent) e;
                teeHoleEvent.getHole().setLoc(event.getBlock().getLocation());
                event.getBlock().setType(Material.AIR);
                Minigolf.courseManager.getCourseCreation(p).openHoleInv(p, teeHoleEvent.getHole());
            }
        }
    }

    @EventHandler
    public void teeClickEvent(TeeHoleEvent event)
    {
        event.getPlayer().getInventory().addItem(new ItemStack(Material.WHITE_CANDLE, 1));
        Minigolf.courseManager.addCourseEvent(event.getPlayer(), event);
    }

    @EventHandler
    public void addHole(AddHoleEvent event)
    {
        int amount = 0;
        for(ItemStack i : event.getInventory().getContents())
        {
            if(i == null || i.getType() == Material.AIR)
                continue;
            amount++;
        }

        if(amount >= 9)
        {
            event.setCancelled(true);
            return;
        }
        Minigolf.courseManager.addCourseEvent(event.getPlayer(), event);
    }

    @EventHandler
    public void parHoleEvent(ParHoleEvent event)
    {
        Minigolf.courseManager.addCourseEvent(event.getPlayer(), event);
    }

    @EventHandler
    public void editHole(EditHoleEvent event)
    {
        CourseCreation c = Minigolf.courseManager.getCourseCreation(event.getPlayer());
        c.openHoleInv(event.getPlayer(), c.getHole(PlainTextComponentSerializer.plainText().serialize(event.getitemStack().getItemMeta().displayName())));
    }

    @EventHandler
    public void saveHole(SaveHoleEvent event)
    {
        CourseCreation c = Minigolf.courseManager.getCourseCreation(event.getPlayer());
        c.saveHole(event.getHole());
        c.openCourseInv(event.getPlayer());
    }

}
