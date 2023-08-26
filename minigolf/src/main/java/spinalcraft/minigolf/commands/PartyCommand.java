package spinalcraft.minigolf.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spinalcraft.minigolf.Minigolf;
import spinalcraft.minigolf.player.Party;
import spinalcraft.minigolf.utils.Messages;

import java.util.Arrays;
import java.util.List;

public class PartyCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player)sender;

        if(Minigolf.playerManager.isPlayerGolfing(p))
        {
            p.sendMessage(Messages.makeMessage(Messages.PartyDuringGolf));
            return true;
        }

        if(args.length == 0)
        {
            if(Minigolf.playerManager.getPlayersParty(p) != null)
            {
                p.sendMessage(Messages.makeMessage(Messages.PartyInviteAlreadyInParty));
                return true;
            }

            Minigolf.playerManager.createParty(p);
            p.sendMessage(Messages.makeMessage(Messages.PartyCreate));
            return true;
        }

        switch(args[0])
        {
            case "invite":
                if(args.length != 2)
                {
                    p.sendMessage(Messages.makeMessage(Messages.PartyInviteNoPlayerArg));
                    return true;
                }
                Player invitee = Bukkit.getServer().getPlayer(args[1]);
                if(invitee == null || !invitee.isOnline())
                {
                    p.sendMessage(Messages.makeMessage(Messages.PartyInviteInvalidPlayer));
                    return true;
                }
                invite(p, invitee);
                break;
            case "leave":
                leave(p);
                break;
            case "accept":
                accept(p);
                break;
            case "reject":
                reject(p);
                break;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1)
            return Arrays.asList("invite", "leave", "accept", "reject");
        return null;
    }

    private boolean invite(Player sender, Player invitee)
    {
        if(sender == invitee)
        {
            sender.sendMessage(Messages.makeMessage(Messages.PartyInviteYourself));
            return true;

        }

        if(Minigolf.playerManager.getPlayersParty(sender) != null)
        {
            sender.sendMessage(Messages.makeMessage(Messages.PartyInviteNotInParty));
            return false;
        }

        if(Minigolf.playerManager.getPlayersParty(invitee) != null)
        {
            sender.sendMessage(Messages.makeMessage(Messages.PartyInviteInviteeInParty.replace("PLAYER", invitee.getName())));
            return false;
        }

        if(Minigolf.playerManager.hasInvite(invitee))
        {
            sender.sendMessage(Messages.makeMessage(Messages.PartyInviteAlreadyPending.replace("PLAYER", invitee.getName())));
            return false;
        }

        if(!Minigolf.playerManager.getPlayersParty(sender).canJoinParty())
        {
            sender.sendMessage(Messages.makeMessage(Messages.PartyInviteFullParty));
            return false;
        }


        invitee.sendMessage(Messages.makeMessage(Messages.PartyInviteInvited.replace("PLAYER", sender.getName())));
        sender.sendMessage(Messages.makeMessage(Messages.PartyInviteSent.replace("PLAYER", invitee.getName())));

        new BukkitRunnable()
        {

            int maxTime = 30;
            int seconds = 0;
            @Override
            public void run()
            {
            if(seconds >= maxTime)
            {
                invitee.sendMessage(Messages.makeMessage(Messages.PartyInviteTimeout));
                Minigolf.playerManager.removeInvite(invitee);
                this.cancel();
            }

                seconds++;
            }
        }.runTaskTimer(Minigolf.m, 0,20);

        return true;
    }

    private void reject(Player p)
    {
        if(Minigolf.playerManager.hasInvite(p))
        {
            p.sendMessage(Messages.makeMessage(Messages.PartyReject));
            Minigolf.playerManager.removeInvite(p);
            return;
        }
        p.sendMessage(Messages.makeMessage(Messages.PartyNoInviteReject));
    }

    private void leave(Player p)
    {
        Party party = Minigolf.playerManager.getPlayersParty(p);
        if(party == null)
            return;
        p.sendMessage(Messages.makeMessage(Messages.PartyLeave));
        party.leaveParty(p);
        for(Player partyMember : party.getPlayers())
            partyMember.sendMessage(Messages.makeMessage(Messages.PartyLeaveNotice.replace("PLAYER", p.getName())));
    }

    private boolean accept(Player player)
    {
        Party p = Minigolf.playerManager.getPendingPartyInvite(player);
        if(p == null)
        {
            player.sendMessage(Messages.makeMessage(Messages.PartyInviteInvalid));
            return false;
        }

        if(p.canJoinParty())
        {
            for(Player partyMember : p.getPlayers())
                partyMember.sendMessage(Messages.makeMessage(Messages.PartyInviteAcceptNotice.replace("PLAYER", player.getName())));
            p.joinParty(player);
            player.sendMessage(Messages.makeMessage(Messages.PartyInviteAccept));
            return true;
        }
        player.sendMessage(Messages.makeMessage(Messages.PartyInviteFullParty));
        return false;
    }
}