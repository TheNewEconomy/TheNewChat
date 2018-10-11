package net.tnemc.tnc.core;

import me.clip.placeholderapi.PlaceholderAPI;
import net.tnemc.tnc.core.common.chat.ChatEntry;
import net.tnemc.tnc.core.common.chat.ChatHandler;
import net.tnemc.tnc.core.common.chat.ChatVariable;
import net.tnemc.tnc.core.common.chat.handlers.CoreHandler;
import net.tnemc.tnc.core.common.chat.handlers.TNKHandler;
import net.tnemc.tnc.core.common.chat.handlers.TownyHandler;
import net.tnemc.tnc.core.common.chat.variables.core.DisplayVariable;
import net.tnemc.tnc.core.common.chat.variables.core.LevelVariable;
import net.tnemc.tnc.core.common.chat.variables.core.MessageVariable;
import net.tnemc.tnc.core.common.chat.variables.core.UsernameVariable;
import net.tnemc.tnc.core.common.chat.variables.core.WorldVariable;
import net.tnemc.tnc.core.common.chat.variables.core.XPVariable;
import net.tnemc.tnc.core.common.configuration.CoreConfigNodes;
import net.tnemc.tnc.core.utils.Message;
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

    loadCoreVariables();
    loadHandlers();
    loadChats();
  }

  public void loadHandlers() {
    addHandler(new CoreHandler());

    if(Bukkit.getPluginManager().isPluginEnabled("TheNewKings")) {
      addHandler(new TNKHandler());
    }

    if(Bukkit.getPluginManager().isPluginEnabled("Towny")) {
      addHandler(new TownyHandler());
    }
  }

  public void loadCoreVariables() {
    coreVariables.put("$display", new DisplayVariable());
    coreVariables.put("$level", new LevelVariable());
    coreVariables.put("$message", new MessageVariable());
    coreVariables.put("$username", new UsernameVariable());
    coreVariables.put("$world", new WorldVariable());
    coreVariables.put("$xp", new XPVariable());
  }

  public void loadChats() {
    final String baseNode = "Chats";
    if(TheNewChat.instance().getChatsConfiguration().contains(baseNode)) {
      Set<String> chatConfigs = TheNewChat.instance().getChatsConfiguration().getConfigurationSection(baseNode).getKeys(false);
      for(String entry : chatConfigs) {
        if(!TheNewChat.instance().getChatsConfiguration().contains(baseNode + "." + entry + ".Handler") ||
            !TheNewChat.instance().getChatsConfiguration().contains(baseNode + "." + entry + ".Type")) {
          continue;
        }
        final String handler = TheNewChat.instance().getChatsConfiguration().getString(baseNode + "." + entry + ".Handler");
        final String type = TheNewChat.instance().getChatsConfiguration().getString(baseNode + "." + entry + ".Type");

        ChatEntry chatConfig = new ChatEntry(handler, type);

        List<String> commands = TheNewChat.instance().getChatsConfiguration().getStringList(baseNode + "." + entry + ".Commands");
        chatConfig.setCommands(commands.toArray(new String[commands.size()]));
        chatConfig.setWorld(TheNewChat.instance().getChatsConfiguration().getBoolean(baseNode + "." + entry + ".WorldBased", false));
        chatConfig.setRadial(TheNewChat.instance().getChatsConfiguration().getBoolean(baseNode + "." + entry + ".Radial", false));
        chatConfig.setRadius(TheNewChat.instance().getChatsConfiguration().getInt(baseNode + "." + entry + ".Radius", 20));
        chatConfig.setPermission(TheNewChat.instance().getChatsConfiguration().getString(baseNode + "." + entry + ".Permission", ""));
        chatConfig.setFormat(TheNewChat.instance().getChatsConfiguration().getString(baseNode + "." + entry + ".Format", handlers.get(handler).getType(type).getDefaultFormat()));

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
    System.out.println("Recipients" + recipients.toString());
    System.out.println("onChat called");
    System.out.println("format: " + event.getFormat());
    event.setFormat(formatMessage(event.getPlayer(), recipients, channel, event.getMessage()));
    System.out.println(" new recipients" + recipients.toString());
    System.out.println("new format: " + event.getFormat());
    event.setCancelled(true);

    for(Player p : event.getRecipients()) {
      if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
        p.sendMessage(PlaceholderAPI.setPlaceholders(p, event.getFormat()));
      } else {
        p.sendMessage(event.getFormat());
      }
    }
  }

  public String formatMessage(final Player player, Collection<Player> recipients, final String channel, final String message) {

    final String handler = getHandler(channel);

    if(handler.equalsIgnoreCase("") || channel.equalsIgnoreCase("general")) {
      String format = parseCoreVariables(player, message, CoreConfigNodes.CORE_GENERAL_CHAT_FORMAT.getString());
      if(!generalHandler.equalsIgnoreCase("core") &&
          handlers.containsKey(generalHandler)) {
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
      format = parseCoreVariables(player, message, format);
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
    String format = formatMessage(player, recipients, channel, message);
    for(Player p : recipients) {
      if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
        p.sendMessage(PlaceholderAPI.setPlaceholders(p, format));
      } else {
        p.sendMessage(format);
      }
    }

    if(sendPlayer) player.sendMessage(format);
  }

  private String parseCoreVariables(final Player player, String message, String format) {
    for(ChatVariable variable : coreVariables.values()) {
      format = format.replaceAll(variable.name(), variable.parse(player, message));
    }
    return Message.replaceColours(format, false);
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
        System.out.println("world-based recipients");
        for(Player p : recipients) {
          if(p.getWorld().getUID().equals(player.getWorld().getUID())) {
            newRecipients.add(p);
          }
        }
      } else {
        System.out.println("boring recipients");
        newRecipients.addAll(recipients);
      }
    }
    return newRecipients;
  }

  public String getHandler(String type) {
    for(Map.Entry<Set<String>, String> entry : handlersMap.entrySet()) {
      if(entry.getKey().contains(type)) return entry.getValue().toLowerCase();
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