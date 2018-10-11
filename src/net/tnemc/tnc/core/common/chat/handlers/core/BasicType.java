package net.tnemc.tnc.core.common.chat.handlers.core;

import net.tnemc.tnc.core.common.chat.ChatType;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class BasicType extends ChatType {
  public BasicType() {
    super("basic", "<white>[<blue>$display<white>]: <yellow>$message");
  }

  @Override
  public Collection<Player> getRecipients(Collection<Player> recipients, Player player) {
    return recipients;
  }
}