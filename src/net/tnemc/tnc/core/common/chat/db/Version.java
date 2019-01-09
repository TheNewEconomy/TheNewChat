package net.tnemc.tnc.core.common.chat.db;

import net.tnemc.tnc.core.TheNewChat;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by creatorfromhell on 3/18/2018.
 * <p>
 * The New Chat Minecraft Server Plugin
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
@DbName("TNC")
@Table("tnc_version")
@IdName("id")
public class Version extends Model {

  public static void add(String version) {
    net.tnemc.tnc.core.common.chat.db.Version versionInstance = (informationExists())? get() : new net.tnemc.tnc.core.common.chat.db.Version();
    versionInstance.set("id", 1);
    versionInstance.set("version_value", version);
    versionInstance.saveIt();
  }

  public static boolean informationExists() {
    return net.tnemc.tnc.core.common.chat.db.Version.findById(1) != null;
  }

  public static net.tnemc.tnc.core.common.chat.db.Version get() {
    return net.tnemc.tnc.core.common.chat.db.Version.findById(1);
  }

  public static boolean outdated() {
    if(informationExists())
      return !get().getString("version_value").equalsIgnoreCase(TheNewChat.instance().getVersion());
    return true;
  }
}
