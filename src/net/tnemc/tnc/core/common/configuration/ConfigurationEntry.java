package net.tnemc.tnc.core.common.configuration;

import java.io.File;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class ConfigurationEntry {

  private IConfigNode[] nodes;
  private String file;
  private boolean module;
  private String owner = "TheNewKings";
  private CommentedConfiguration newConfig;
  private CommentedConfiguration oldConfig;

  public <T extends Enum<T> & IConfigNode> ConfigurationEntry(Class<T> nodes, File file) {
    this(nodes, file, false, "TheNewKings");
  }

  public <T extends Enum<T> & IConfigNode> ConfigurationEntry(Class<T> nodes, File file, boolean module, String owner) {
    this(nodes.getEnumConstants(), file, module, owner);
  }

  public <T extends Enum<T> & IConfigNode> ConfigurationEntry(IConfigNode[] nodes, File file, boolean module, String owner) {
    this.nodes = nodes;
    this.file = file.getName();
    this.module = module;
    this.owner = owner;
    this.newConfig = new CommentedConfiguration(file);
    this.oldConfig = new CommentedConfiguration(file);
  }

  public IConfigNode[] getNodes() {
    int length = nodes.length + CommonNodes.values().length;
    IConfigNode[] nodeArray = new IConfigNode[length];
    int index = 0;
    for(IConfigNode node : CommonNodes.values()) {
      nodeArray[index] = node;
      index++;
    }

    for(IConfigNode node : nodes) {
      nodeArray[index] = node;
      index++;
    }

    return nodeArray;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public boolean isModule() {
    return module;
  }

  public void setModule(boolean module) {
    this.module = module;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public CommentedConfiguration getNewConfig() {
    return newConfig;
  }

  public void setNewConfig(CommentedConfiguration newConfig) {
    this.newConfig = newConfig;
  }

  public CommentedConfiguration getOldConfig() {
    return oldConfig;
  }

  public void setOldConfig(CommentedConfiguration oldConfig) {
    this.oldConfig = oldConfig;
  }
}