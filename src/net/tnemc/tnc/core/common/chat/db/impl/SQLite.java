package net.tnemc.tnc.core.common.chat.db.impl;

import net.tnemc.tnc.core.common.chat.db.DataProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by creatorfromhell.
 *
 * The New VPN Blocker Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class SQLite implements DataProvider {
  @Override
  public String getDriver() {
    return "org.sqlite.JDBC";
  }

  @Override
  public Boolean dataSource() {
    return false;
  }

  @Override
  public String dataSourceURL() {
    return "";
  }

  @Override
  public String getURL(String file, String host, int port, String database) {
    return "jdbc:sqlite:" + file;
  }

  @Override
  public void preConnect(String file, String host, int port, String database) {
    if(!file.endsWith(".db")) file = file + ".db";
    File db = new File(file);
    if(!db.exists()) {
      try {
        db.createNewFile();
      } catch(IOException e) {
        e.printStackTrace();
      }
    }
  }
}