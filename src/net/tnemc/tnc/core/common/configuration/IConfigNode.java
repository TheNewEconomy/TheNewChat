package net.tnemc.tnc.core.common.configuration;


import net.tnemc.tnc.core.ConfigurationManager;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public interface IConfigNode {

  String getNode();

  default String getDefaultValue() {
    return "";
  }

  default String[] getComments() {
    return new String[] { "" };
  }

  String getFile();

  default boolean getBoolean() {
    return ConfigurationManager.getBoolean(getFile(), this);
  }

  default String getString() {
    return ConfigurationManager.getString(getFile(), this);
  }

  default Double getDouble() {
    return ConfigurationManager.getDouble(getFile(), this);
  }

  default Integer getInt() {
    return ConfigurationManager.getInt(getFile(), this);
  }
}