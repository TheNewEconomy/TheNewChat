package net.tnemc.tnc.core.common.configuration;


import net.tnemc.tnc.core.ConfigurationManager;

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