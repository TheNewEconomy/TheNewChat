package net.tnemc.tnc.core.command;

import net.tnemc.tnc.core.TheNewChat;
import org.bukkit.command.CommandSender;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class ReloadCommand extends TNECommand {
  public ReloadCommand(TheNewChat plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "tncreload";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "chatreload"
    };
  }

  @Override
  public String getNode() {
    return "tnc.reload";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/tnereload - Reloads chats from configurations."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TheNewChat.instance().getManager().reload();
    return true;
  }
}