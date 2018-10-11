package net.tnemc.tnc.core.command;

import net.tnemc.tnc.core.TheNewChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class ChatCommand extends TNECommand {
  private String[] commands;

  public ChatCommand(TheNewChat plugin, String[] commands) {
    super(plugin);
    this.commands = commands;
  }

  @Override
  public String getName() {
    return "tnc";
  }

  @Override
  public String[] getAliases() {
    return commands;
  }

  @Override
  public String getNode() {
    return "tnc.channel";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/tnc <channel> [message] - Joins the specified channel if no message provided.",
        "other wise will send the specified message in the specified channel"
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    final UUID id = getPlayer(sender).getUniqueId();
    if(command.equalsIgnoreCase("tnc")) {
      if(arguments.length >= 1) {
        if(!plugin.getManager().getHandler(arguments[0]).equalsIgnoreCase("")) {
          if(arguments.length == 1) {
            plugin.getManager().getChannels().put(id.toString(), arguments[0]);
            sender.sendMessage(ChatColor.GOLD + "Joined channel: " + arguments[0]);
            return true;
          } else {
            plugin.getManager().sendMessage(getPlayer(sender), getCollection(), arguments[0], String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length + 1)), false);
            return true;
          }
        }
        sender.sendMessage(ChatColor.RED + "Unable to locate the specified channel.");
        return false;
      }
      help(sender);
      return false;
    } else {
      String channel = plugin.getManager().getChannelByCommand(command);
      if(command.equalsIgnoreCase("gc") || command.equalsIgnoreCase("generalchat")) {
        channel = "general";
      }
      if(channel != null) {
        if(arguments.length == 0) {
          if(channel.equalsIgnoreCase("general")) {
            plugin.getManager().getChannels().remove(id.toString());
            sender.sendMessage(ChatColor.GOLD + "Joined General Chat");
            return true;
          } else {
            if(plugin.getManager().getChannels().containsKey(id.toString()) &&
                channel.equalsIgnoreCase(plugin.getManager().getChannels().get(id.toString()))) {
              plugin.getManager().getChannels().remove(id.toString());
              sender.sendMessage(ChatColor.GOLD + "Left channel: " + channel);
              return true;
            } else {
              plugin.getManager().getChannels().put(id.toString(), channel);
              sender.sendMessage(ChatColor.GOLD + "Joined channel: " + channel);
              return true;
            }
          }
        } else {
          plugin.getManager().sendMessage(getPlayer(sender), getCollection(), channel, String.join(" ", arguments), false);
          return true;
        }
      }
    }
    return true;
  }

  private Collection<Player> getCollection() {
    Collection<Player> players = new ArrayList<>();
    players.addAll(Bukkit.getOnlinePlayers());
    return players;
  }
}