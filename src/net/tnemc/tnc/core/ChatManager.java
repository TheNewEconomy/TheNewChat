package net.tnemc.tnc.core;

import net.tnemc.tnc.core.common.chat.ChatEntry;
import net.tnemc.tnc.core.common.chat.ChatHandler;
import net.tnemc.tnc.core.common.chat.ChatVariable;
import net.tnemc.tnc.core.common.configuration.CoreConfigNodes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class ChatManager implements Listener {

  private LinkedHashMap<String, ChatHandler> handlers = new LinkedHashMap<>();

  /**
   * Used for mapping chat types to their handler's name.
   */
  private LinkedHashMap<Set<String>, String> handlersMap = new LinkedHashMap<>();

  private Map<String, Map<String, ChatEntry>> chats = new HashMap<>();

  /**
   * The core variables used
   */
  private Map<String, ChatVariable> coreVariables = new HashMap<>();

  private String generalHandler = "Core";


  public ChatManager(String generalHandler) {
    //TODO: Load Chat Handlers, and types.

    loadChats();
  }

  public void loadChats() {
    final String baseNode = "Core.Chats";
    Set<String> chatConfigs = ConfigurationManager.getConfigurationSection("config.yml", baseNode).getKeys(false);
    for(String entry : chatConfigs) {
      if(!ConfigurationManager.hasConfiguration("config.yml", baseNode + "." + entry + ".Handler") ||
          !ConfigurationManager.hasConfiguration("config.yml", baseNode + "." + entry + ".Type")) {
        continue;
      }
      final String handler = ConfigurationManager.getString("config.yml", baseNode + "." + entry + ".Handler");
      final String type = ConfigurationManager.getString("config.yml", baseNode + "." + entry + ".Type");

      ChatEntry chatConfig = new ChatEntry(handler, type);

      List<String> commands = ConfigurationManager.getStrList("config.yml", baseNode + "." + entry + ".Commands");
      chatConfig.setCommands(commands.toArray(new String[commands.size()]));
      chatConfig.setRadial(ConfigurationManager.getBoolean("config.yml", baseNode + "." + entry + ".Radial", false));
      chatConfig.setRadius(ConfigurationManager.getInt("config.yml", baseNode + "." + entry + ".Radius", 20));
      chatConfig.setPermission(ConfigurationManager.getString("config.yml", baseNode + "." + entry + ".Permission", ""));
      chatConfig.setFormat(ConfigurationManager.getString("config.yml", baseNode + "." + entry + ".Permission", handlers.get(handler).getType(type).getDefaultFormat()));

      addChatEntry(chatConfig);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChat(AsyncPlayerChatEvent event) {
    String channel = "General";

    if(event.getPlayer().hasMetadata("tnc-channel")) {
      channel = event.getPlayer().getMetadata("tnc-channel").get(0).asString();
    }

    final String handler = getHandler(channel);

    if(handler.equalsIgnoreCase("") || channel.equalsIgnoreCase("general")) {
      String format = CoreConfigNodes.CORE_GENERAL_CHAT_FORMAT.getString();
      if(generalHandler.equalsIgnoreCase("core") ||
           !handlers.containsKey(generalHandler)) {
        format = parseCoreVariables(event.getPlayer(), event.getMessage(), format);
      } else {
        format = handlers.get(handler).parseGeneral(event.getPlayer(), event.getMessage(), format);
      }
      final Set<Player> recipients = getRecipientsRadial(event.getRecipients(), event.getPlayer(),
                                                   CoreConfigNodes.CORE_GENERAL_CHAT_RADIAL.getBoolean(),
                                                   CoreConfigNodes.CORE_GENERAL_CHAT_RADIUS.getInt());

      event.getRecipients().clear();
      event.getRecipients().addAll(recipients);
      event.setFormat(format);
    } else {
      ChatEntry entry = chats.get(handler).get(channel);
      String format = entry.getFormat();
      final Set<Player> recipients = getRecipientsRadial(event.getRecipients(), event.getPlayer(),
                                                         entry.isRadial(),
                                                         entry.getRadius());

      event.getRecipients().clear();
      event.getRecipients().addAll(recipients);
      event.setFormat(handlers.get(handler).parseMessage(event.getPlayer(), channel, event.getMessage(), format));
    }
  }

  private String parseCoreVariables(final Player player, String message, String format) {
    for(ChatVariable variable : coreVariables.values()) {
      format = format.replaceAll(variable.name(), variable.parse(player, message));
    }
    return format;
  }

  private Set<Player> getRecipientsRadial(final Set<Player> recipients, final Player player, final boolean radial, final int radius) {
    Set<Player> newRecipients = new HashSet<>();
    if(radial) {
      for(Player p : recipients) {
        if(p.getLocation().distance(player.getLocation()) <= radius) {
          newRecipients.add(p);
        }
      }
    } else {
      newRecipients = recipients;
    }
    return newRecipients;
  }

  public String getHandler(String type) {
    for(Map.Entry<Set<String>, String> entry : handlersMap.entrySet()) {
      if(entry.getKey().contains(type)) return entry.getValue();
    }
    return "";
  }

  public void addChatEntry(ChatEntry entry) {
    Map<String, ChatEntry> chat = new HashMap<>();
    chat.put(entry.getType(), entry);
    if(chats.containsKey(entry.getHandler())) {
      chats.get(entry.getHandler()).putAll(chat);
    } else {
      chats.put(entry.getHandler(), chat);
    }
  }

  public LinkedHashMap<Set<String>, String> getHandlersMap() {
    return handlersMap;
  }

  public void setHandlersMap(LinkedHashMap<Set<String>, String> handlersMap) {
    this.handlersMap = handlersMap;
  }

  public Map<String, Map<String, ChatEntry>> getChats() {
    return chats;
  }

  public void setChats(Map<String, Map<String, ChatEntry>> chats) {
    this.chats = chats;
  }

  public Map<String, ChatVariable> getCoreVariables() {
    return coreVariables;
  }

  public void setCoreVariables(Map<String, ChatVariable> coreVariables) {
    this.coreVariables = coreVariables;
  }

  public String getGeneralHandler() {
    return generalHandler;
  }

  public void setGeneralHandler(String generalHandler) {
    this.generalHandler = generalHandler;
  }

  public void addHandler(ChatHandler handler) {
    handlers.put(handler.getName(), handler);
  }

  public LinkedHashMap<String, ChatHandler> getHandlers() {
    return handlers;
  }

  public void setHandlers(LinkedHashMap<String, ChatHandler> handlers) {
    this.handlers = handlers;
  }
}