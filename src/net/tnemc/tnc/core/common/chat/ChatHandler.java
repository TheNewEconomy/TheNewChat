package net.tnemc.tnc.core.common.chat;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
  protected Map<String, ChatCheck> checks = new HashMap<>();

  public abstract String getName();

  public String parseMessage(final Player player, final String type, String message, String format) {
    for(Map.Entry<String, ChatVariable> entry : variables.entrySet()) {
      format = format.replaceAll(Pattern.quote(entry.getKey()), entry.getValue().parse(player, message));
    }

    if(type.equalsIgnoreCase("general")) return format;
    return types.get(type).handle(player, message, format);
  }

  public void addCheck(final ChatCheck check) {
    checks.put(check.name(), check);
  }

  public boolean hasCheck(final String name) {
    return checks.containsKey(name);
  }

  public ChatCheck getCheck(final String name) {
    return checks.get(name);
  }

  public void addVariable(final ChatVariable variable) {
    variables.put(variable.name(), variable);
  }

  public void addType(final ChatType type) {
    types.put(type.getName(), type);
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