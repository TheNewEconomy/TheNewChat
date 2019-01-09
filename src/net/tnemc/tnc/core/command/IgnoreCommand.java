package net.tnemc.tnc.core.command;

import net.tnemc.tnc.core.TheNewChat;
import net.tnemc.tnc.core.common.chat.db.IgnoredChannel;
import net.tnemc.tnc.core.common.configuration.CoreConfigNodes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
public class IgnoreCommand extends TNECommand {
  public IgnoreCommand(TheNewChat plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "ignorechannel";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "ignorec", "igc", "leave"
    };
  }

  @Override
  public String getNode() {
    return "tnc.ignore";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/ignorechannel <channel> - Ignores the specified channel."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length > 0) {
      Bukkit.getScheduler().runTaskAsynchronously(TheNewChat.instance(), ()->{
        final UUID id = getPlayer(sender).getUniqueId();
        if(plugin.getManager().getCommands().values().contains(arguments[0]) || arguments[0].equalsIgnoreCase("general")) {
          final String handler = plugin.getManager().getHandler(arguments[0]);
          boolean ignorable = (arguments[0].equalsIgnoreCase("general"))? CoreConfigNodes.CORE_GENERAL_CHAT_IGNORABLE.getBoolean() :
              plugin.getManager().getChats().get(handler).get(arguments[0]).isIgnorable();
          if(ignorable) {
            TheNewChat.saveManager().open();
            if(!IgnoredChannel.exists(id, arguments[0])) {
              IgnoredChannel.add(id, arguments[0]);
              sender.sendMessage(ChatColor.GOLD + "Now ignoring channel: " + arguments[0] + ".");
              TheNewChat.saveManager().close();
              return;
            }
            IgnoredChannel.delete(id, arguments[0]);
            sender.sendMessage(ChatColor.GOLD + "No longer ignoring channel: " + arguments[0] + ".");
            TheNewChat.saveManager().close();
            return;
          }
          sender.sendMessage(ChatColor.RED + "Cannot ignore that channel.");
          return;
        }
        sender.sendMessage(ChatColor.RED + "Invalid channel.");
        return;
      });
    }
    help(sender);
    return false;
  }
}