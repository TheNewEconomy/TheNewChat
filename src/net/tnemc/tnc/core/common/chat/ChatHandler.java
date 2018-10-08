package net.tnemc.tnc.core.common.chat;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public abstract class ChatHandler {

  protected Map<String, ChatType> types = new HashMap<>();
  protected Map<String, ChatVariable> variables = new HashMap<>();

  public abstract String getName();
  public abstract String parseGeneral(final Player player, String message, String format);

  public String parseMessage(final Player player, final String type, String message, String format) {
    for(Map.Entry<String, ChatVariable> entry : variables.entrySet()) {
      message = message.replaceAll(entry.getKey(), entry.getValue().parse(player, message));
    }
    if(type.equalsIgnoreCase("general")) return parseGeneral(player, message, format);
    return types.get(type).handle(player, message, format);
  }

  public ChatType getType(String name) {
    return types.get(name);
  }

  public Map<String, ChatType> getTypes() {
    return types;
  }

  public void setTypes(Map<String, ChatType> types) {
    this.types = types;
  }
}