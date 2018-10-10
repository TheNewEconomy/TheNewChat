package net.tnemc.tnc.core;

import net.tnemc.tnc.core.common.configuration.ConfigurationEntry;
import net.tnemc.tnc.core.common.configuration.CoreConfigNodes;
import net.tnemc.tnc.core.utils.FileMgmt;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
  ChatManager manager;

  public void onLoad() {
    instance = this;

    ConfigurationManager.initialize(this);
  }

  public void onEnable() {
    addConfiguration(new ConfigurationEntry(CoreConfigNodes.class, new File(getRootFolder() + FileMgmt.fileSeparator() + "config.yml")));

    this.manager = new ChatManager(CoreConfigNodes.CORE_GENERAL_CHAT_HANDLER.getString());

    Bukkit.getPluginManager().registerEvents(manager, this);
  }
}