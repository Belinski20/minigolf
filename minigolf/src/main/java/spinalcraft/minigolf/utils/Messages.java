package spinalcraft.minigolf.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class Messages {

    public static TextComponent makeMessage(String message)
    {
        return Component.text().content(message).build();
    }

    // Party Messages
    public static String PartyInviteNotInParty = "You are not in a golf party.";
    public static String PartyInviteNoPlayerArg = "You need to provide a players name to invite to your golf party.";
    public static String PartyInviteAlreadyInParty = "You are already in a golf party.";
    public static String PartyInviteInviteeInParty = "The PLAYER you are trying to invite is already in a golf party.";
    public static String PartyInviteInvited = "You have been invite to a golf party by PLAYER.";
    public static String PartyInviteSent = "Invite sent to PLAYER.";
    public static String PartyInviteTimeout = "Your golf party invite has timed out.";
    public static String PartyInviteFullParty = "The golf party is already full.";
    public static String PartyLeave = "You have left the golf party.";
    public static String PartyLeaveNotice = "PLAYER has left the golf party.";
    public static String PartyCreate = "You have created a golf party";
    public static String PartyInviteYourself = "You cannot invite yourself to a golf party.";
    public static String PartyInviteAccept = "You have joined the golf party.";
    public static String PartyInviteAcceptNotice = "PLAYER has joined the golf party.";
    public static String PartyInviteInvalidPlayer = "Player is either invalid or offline.";
    public static String PartyInviteInvalid = "The golf party you tried to join has been dissolved.";
    public static String PartyInviteAlreadyPending = "PLAYER already has a pending golf party invite.";
    public static String PartyReject = "You have rejected a golf party invite.";
    public static String PartyNoInviteReject = "You have no golf party invites to reject.";
    public static String PartyDuringGolf = "You cannot use the party command while golfing.";

    //Cosmetic Messages
    public static String CosmeticInvalidMaterial = "MATERIAL is not a valid type of Material.";
    public static String CosmeticSavedMaterial = "Your ITEM has been set to look like MATERIAL.";
    //Create Course Messages
    public static String CCNoCoursePending = "You are not working on a course.";
    public static String CCCourseNotExist = "The Course COURSE does not exist.";
    public static String CCCourseSaved = "The Course was saved.";
    public static String CCEditingCourseAlready = "You are already editing a course.";
    public static String CCLobbySet = "The main lobby for mini golf was set at your position.";
    //Green Messages
    public static String CCGreenExists = "MATERIAL is already a green.";
    public static String CCGreenAdded = "MATERIAL was added as a green.";
    public static String CCGreenRemove = "MATERIAL was removed from being a green.";
    public static String CCGreenRemoveDoesNotExist = "MATERIAL is not a green.";
    public static String CCGreenInvalidArguments = "Command : /cc green <add|remove> <Material>";
    // Lobby Message
    public static String TeleportBackToLobby = "You have been brought back to the Minigolf lobby.";


}
