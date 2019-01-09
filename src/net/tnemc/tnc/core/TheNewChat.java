package net.tnemc.tnc.core;

import net.tnemc.tnc.core.command.ChatCommand;
import net.tnemc.tnc.core.command.CommandManager;
import net.tnemc.tnc.core.command.IgnoreCommand;
import net.tnemc.tnc.core.command.ReloadCommand;
import net.tnemc.tnc.core.command.TNECommand;
import net.tnemc.tnc.core.common.chat.db.SaveManager;
import net.tnemc.tnc.core.common.chat.db.Version;
import net.tnemc.tnc.core.common.configuration.ConfigurationEntry;
import net.tnemc.tnc.core.common.configuration.CoreConfigNodes;
import net.tnemc.tnc.core.utils.FileMgmt;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.tnemc.tnc.core.ConfigurationManager.addConfiguration;
import static net.tnemc.tnc.core.ConfigurationManager.getRootFolder;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class TheNewChat extends JavaPlugin {

  private static TheNewChat instance;
  private ChatManager manager;
  private CommandManager commandManager;

  private File chatsFile;
  private FileConfiguration chatsConfiguration;
  private String version = "0.0.1.0";

  private SaveManager saveManager;

  public void onLoad() {
    instance = this;

    ConfigurationManager.initialize(this);
  }

  public void onEnable() {
    addConfiguration(new ConfigurationEntry(CoreConfigNodes.class, new File(getRootFolder() + FileMgmt.fileSeparator() + "config.yml")));

    if (!ConfigurationManager.loadSettings()){
      getLogger().info("Unable to load configuration!");
    }

    initializeConfigurations();
    loadConfigurations();

    try {
      new File(getDataFolder(), "TheNewChat.db").createNewFile();
    } catch(IOException ignore) {
    }

    saveManager = new SaveManager();

    saveManager.createTables();
    saveManager.open();
    if(Version.informationExists()) {
      if(Version.outdated()) {
        saveManager.updateTables(version);
        Version.add(version);
      }
    }
    saveManager.close();

    this.manager = new ChatManager(CoreConfigNodes.CORE_GENERAL_CHAT_HANDLER.getString());
    commandManager = new CommandManager();

    registerChatCommand();
    registerCommand(new String[] { "chatreload", "tncreload" }, new ReloadCommand(this));
    registerCommand(new String[] { "ignorec", "igc", "ignorechannel" }, new IgnoreCommand(this));
    registerListener(manager);
  }

  public ChatManager getManager() {
    return manager;
  }

  private CommandManager getCommandManager() {
    return commandManager;
  }

  private void registerCommand(String[] accessors, TNECommand command) {
    commandManager.commands.put(accessors, command);
    commandManager.registerCommands();
  }

  private void registerCommands(Map<String[], TNECommand> commands) {
    commandManager.commands = commands;
    commandManager.registerCommands();
  }

  private void registerListener(Listener listener) {
    getServer().getPluginManager().registerEvents(listener, this);
  }

  private void unregisterCommand(String[] accessors) {
    commandManager.unregister(accessors);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
    TNECommand ecoCommand = commandManager.Find(label);
    if(ecoCommand != null) {
      if(!ecoCommand.canExecute(sender)) {
        sender.sendMessage(ChatColor.RED + "I'm sorry, but you're not allowed to use that command.");
        return false;
      }
      return ecoCommand.execute(sender, label, arguments);
    }
    return false;
  }

  public void registerChatCommand() {
    List<String> commands = new ArrayList<>();
    commands.addAll(manager.getCommands().keySet());
    commands.add("gc");
    commands.add("generalchat");
    commands.add("generalchat");
    commands.add("tnc");
    final String[] commandsArray = commands.toArray(new String[commands.size()]);

    registerCommand(commandsArray, new ChatCommand(this, commandsArray));
  }

  private void initializeConfigurations() {
    chatsFile = new File(getDataFolder(), "chats.yml");
    chatsConfiguration = YamlConfiguration.loadConfiguration(chatsFile);
    try {
      setConfigurationDefaults();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  void loadConfigurations() {
    chatsConfiguration.options().copyDefaults(true);
    saveConfigurations();
  }

  private void saveConfigurations() {
    try {
      if(!chatsFile.exists()) {
        chatsConfiguration.save(chatsFile);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setConfigurationDefaults() throws UnsupportedEncodingException {
    Reader chatsStream = new InputStreamReader(this.getResource("chats.yml"), "UTF8");
    if (chatsStream != null && !chatsFile.exists()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(chatsStream);
      chatsConfiguration.setDefaults(config);
    }
  }

  public static TheNewChat instance() {
    return instance;
  }

  FileConfiguration getChatsConfiguration() {
    return chatsConfiguration;
  }

  public String getVersion() {
    return version;
  }

  public static SaveManager saveManager() {
    return instance().saveManager;
  }
}