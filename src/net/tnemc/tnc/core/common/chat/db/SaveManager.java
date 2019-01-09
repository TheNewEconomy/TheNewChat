package net.tnemc.tnc.core.common.chat.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.tnemc.tnc.core.TheNewChat;
import net.tnemc.tnc.core.common.chat.db.impl.H2;
import org.javalite.activejdbc.DB;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by creatorfromhell on 3/18/2018.
 * <p>
 * The New Chat Minecraft Server Plugin
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class SaveManager {

  private Map<String, List<String>> dataTables = new HashMap<>();
  private Map<String, DataProvider> providers = new HashMap<>();

  //Instances
  private DB db = new DB("TNC");
  private final HikariConfig config;
  private final HikariDataSource dataSource;

  private String type;

  private String file;
  private String host;
  private int port;
  private String dbName;
  private String user;
  private String pass;

  public SaveManager() {
    type = "h2";
    file = new File(TheNewChat.instance().getDataFolder(), "TheNewChat").getAbsolutePath();
    host = "localhost";
    port = 3306;
    dbName = "TNC";
    user = "thenewchat";
    pass = "password";

    //Initialize DataProviders.
    providers.put("h2", new H2());

    config = new HikariConfig();

    if(providers.get(type).dataSource()) {
      config.setDataSourceClassName(providers.get(type).dataSourceURL());
    } else {
      config.setJdbcUrl(providers.get(type).getURL(file, host, port, dbName));
      config.setDriverClassName(providers.get(type).getDriver());
    }

    config.setUsername(user);
    config.setPassword(pass);

    dataSource = new HikariDataSource(config);

    //initialize tables.
    
    List<String> h2 = new ArrayList<>();

    h2.add("CREATE TABLE IF NOT EXISTS `tnc_version` (" +
                  "`id` INTEGER NOT NULL UNIQUE," +
                  "`version_value` VARCHAR(15)" +
                  ") ENGINE = INNODB;");

    h2.add("CREATE TABLE IF NOT EXISTS tnc_ignored (" +
                  "uuid VARCHAR(36) NOT NULL," +
                  "channel VARCHAR(100) NOT NULL," +
                  "PRIMARY KEY(uuid, channel)" +
                  ") ENGINE = INNODB;");
    dataTables.put("h2", h2);
  }

  public void createTables() {
    List<String> tables = dataTables.get(type);
    try {
      DataProvider provider = providers.get(type);
      provider.preConnect(file, host, port, dbName);
      db.open(provider.getDriver(), provider.getURL(file, host, port, dbName), user, pass);
      tables.forEach((table)->{
        db.exec(table);
      });
      Version.add(TheNewChat.instance().getVersion());
    } finally {
      db.close();
    }
  }

  public void updateTables(String version) {

  }

  public void open() {
    try {
      db.open(dataSource);
    } catch(Exception ignore) {
    }
  }

  public void close() {
    db.close();
  }

  public void addProvider(String type, DataProvider provider) {
    providers.put(type, provider);
  }

  public void registerTables(String type, List<String> tables) {
    dataTables.put(type, tables);
  }
}