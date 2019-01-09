package net.tnemc.tnc.core.common.chat.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.CompositePK;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

import java.util.UUID;

/**
 * Created by creatorfromhell.
 *
 * The New VPN Blocker Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
@DbName("TNC")
@Table("tnc_ignored")
@CompositePK({ "uuid", "channel" })
public class IgnoredChannel extends Model {

  public static boolean add(UUID id, String channel) {
    IgnoredChannel chan = (exists(id, channel))? getChannel(id, channel) : new IgnoredChannel();
    chan.set("uuid", id.toString());
    chan.set("channel", channel);

    return chan.saveIt();
  }

  public static boolean exists(UUID id, String channel) {
    return getChannel(id, channel) != null;
  }

  public static IgnoredChannel getChannel(UUID id, String channel) {
    return IgnoredChannel.findFirst("uuid = ? AND channel = ?", id.toString(), channel);
  }

  public static void delete(UUID id, String channel) {
    IgnoredChannel.delete("uuid = ? AND channel = ?", id.toString(), channel);
  }
}