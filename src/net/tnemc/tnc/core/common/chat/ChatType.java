package net.tnemc.tnc.core.common.chat;

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
public abstract class ChatType {


  private String name;
  private String defaultFormat = "";

  public ChatType(String name, String defaultFormat) {
    this.name = name;
    this.defaultFormat = defaultFormat;
  }
  public abstract Collection<Player> getRecipients(Collection<Player> recipients, Player player);

  public String handle(Player player, String message, String format) {
    return format;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDefaultFormat() {
    return defaultFormat;
  }

  public void setDefaultFormat(String defaultFormat) {
    this.defaultFormat = defaultFormat;
  }
}