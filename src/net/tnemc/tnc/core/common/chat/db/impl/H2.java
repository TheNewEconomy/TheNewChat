package net.tnemc.tnc.core.common.chat.db.impl;


import net.tnemc.tnc.core.common.chat.db.DataProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by creatorfromhell on 3/18/2018.
 * <p>
 * The New Chat Minecraft Server Plugin
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class H2 implements DataProvider {
  @Override
  public String getDriver() {
    return "org.h2.Driver";
  }

  @Override
  public Boolean dataSource() {
    return false;
  }

  @Override
  public String dataSourceURL() {
    return "org.h2.jdbcx.JdbcDataSource";
  }

  @Override
  public String getURL(String file, String host, int port, String database) {
    return "jdbc:h2:" + file;
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
