package net.tnemc.tnc.core;

import net.tnemc.tnc.core.common.chat.ChatEntry;
import net.tnemc.tnc.core.common.chat.ChatHandler;
import net.tnemc.tnc.core.common.chat.ChatVariable;
import net.tnemc.tnc.core.common.chat.handlers.TNKHandler;
import net.tnemc.tnc.core.common.chat.handlers.TownyHandler;
import net.tnemc.tnc.core.common.configuration.CoreConfigNodes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;
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

  private Map<String, String> commands = new HashMap<>();

  private Map<String, String> channels = new HashMap<>();

  /**
   * The core variables used
   */
  private Map<String, ChatVariable> coreVariables = new HashMap<>();

  private String generalHandler = "Core";


  public ChatManager(String generalHandler) {
    this.generalHandler = generalHandler;

    loadHandlers();
    loadChats();
  }

  public void loadHandlers() {
    if(Bukkit.getPluginManager().isPluginEnabled("TheNewKings")) {
      addHandler(new TNKHandler());
    }

    if(Bukkit.getPluginManager().isPluginEnabled("Towny")) {
      addHandler(new TownyHandler());
    }
  }

  public void loadChats() {
    final String baseNode = "Core.Chats";
    if(ConfigurationManager.hasConfiguration("config.yml", baseNode)) {
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
        chatConfig.setWorld(ConfigurationManager.getBoolean("config.yml", baseNode + "." + entry + ".WorldBased", false));
        chatConfig.setRadial(ConfigurationManager.getBoolean("config.yml", baseNode + "." + entry + ".Radial", false));
        chatConfig.setRadius(ConfigurationManager.getInt("config.yml", baseNode + "." + entry + ".Radius", 20));
        chatConfig.setPermission(ConfigurationManager.getString("config.yml", baseNode + "." + entry + ".Permission", ""));
        chatConfig.setFormat(ConfigurationManager.getString("config.yml", baseNode + "." + entry + ".Permission", handlers.get(handler).getType(type).getDefaultFormat()));

        addChatEntry(chatConfig);
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChat(AsyncPlayerChatEvent event) {
    if(event.getMessage().startsWith("/")) return;
    String channel = "General";

    if(channels.containsKey(event.getPlayer().getUniqueId().toString())) {
      channel = channels.get(event.getPlayer().getUniqueId().toString());
    }

    Set<Player> recipients = event.getRecipients();
    event.setFormat(formatMessage(event.getPlayer(), recipients, channel, event.getMessage()));
  }

  public String formatMessage(final Player player, Collection<Player> recipients, final String channel, final String message) {

    final String handler = getHandler(channel);

    if(handler.equalsIgnoreCase("") || channel.equalsIgnoreCase("general")) {
      String format = CoreConfigNodes.CORE_GENERAL_CHAT_FORMAT.getString();
      if(generalHandler.equalsIgnoreCase("core") ||
          !handlers.containsKey(generalHandler)) {
        format = parseCoreVariables(player, message, format);
      } else {
        format = handlers.get(handler).parseMessage(player, "general", message, format);
      }
      final Collection<Player> recip = getRecipientsRadial(recipients, player,
                                                         CoreConfigNodes.CORE_GENERAL_CHAT_WORLD_BASED.getBoolean(),
                                                         CoreConfigNodes.CORE_GENERAL_CHAT_RADIAL.getBoolean(),
                                                         CoreConfigNodes.CORE_GENERAL_CHAT_RADIUS.getInt());

      recipients.clear();
      recipients.addAll(recip);
      return format;
    } else {
      ChatEntry entry = chats.get(handler).get(channel);
      String format = entry.getFormat();
      final Collection<Player> recip = getRecipientsRadial(handlers.get(handler).getType(channel).getRecipients(recipients, player), player,
                                                         entry.isWorld(),
                                                         entry.isRadial(),
                                                         entry.getRadius());

      recipients.clear();
      recipients.addAll(recip);
      return handlers.get(handler).parseMessage(player, channel, message, format);
    }
  }

  public void sendMessage(final Player player, Collection<Player> recipients, final String channel, final String message, boolean sendPlayer) {
    Collection<Player> chatRecipients = recipients;
    String format = formatMessage(player, recipients, channel, message);
    for(Player p : chatRecipients) {
      p.sendMessage(format);
    }

    if(sendPlayer) player.sendMessage(format);
  }

  private String parseCoreVariables(final Player player, String message, String format) {
    for(ChatVariable variable : coreVariables.values()) {
      format = format.replaceAll(variable.name(), variable.parse(player, message));
    }
    return format;
  }

  private Collection<Player> getRecipientsRadial(final Collection<Player> recipients, final Player player, final boolean world, final boolean radial, final int radius) {
    Collection<Player> newRecipients = new HashSet<>();
    if(radial) {
      for(Player p : recipients) {
        if(p.getLocation().distance(player.getLocation()) <= radius) {
          newRecipients.add(p);
        }
      }
    } else {
      if(world) {
        for(Player p : recipients) {
          if(p.getWorld().getUID().equals(player.getWorld().getUID())) {
            newRecipients.add(p);
          }
        }
      } else {
        newRecipients = recipients;
      }
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

    for(String command : entry.getCommands()) {
      commands.put(command, entry.getType());
    }
  }

  public String getChannelByCommand(String command) {
    return commands.get(command);
  }

  public Map<String, String> getCommands() {
    return commands;
  }

  public Map<String, String> getChannels() {
    return channels;
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
    handlersMap.put(handler.getTypes().keySet(), handler.getName());
  }

  public LinkedHashMap<String, ChatHandler> getHandlers() {
    return handlers;
  }

  public void setHandlers(LinkedHashMap<String, ChatHandler> handlers) {
    this.handlers = handlers;
  }
}