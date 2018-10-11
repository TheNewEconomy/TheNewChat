package net.tnemc.tnc.core.common.chat.handlers.tnk;

import net.tnemc.tnc.core.common.chat.ChatType;
import net.tnemc.tnk.core.TheNewKings;
import net.tnemc.tnk.core.common.helper.KingdomHelper;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class KingdomType extends ChatType {
  public KingdomType() {
    super("kingdom", "<gray>[<aqua>$village<gray>]$display: <white>$message");
  }

  @Override
  public Collection<Player> getRecipients(Collection<Player> recipients, Player player) {
    TheNewKings.instance().saveManager().open();
    final String kingdom = KingdomHelper.getKingdomPlayer(player.getUniqueId(), player.getWorld().getName());

    Collection<Player> newRecipients = new HashSet<>();

    for(Player p : recipients) {
      if(KingdomHelper.getKingdomPlayer(p.getUniqueId(), p.getWorld().getName()).equalsIgnoreCase(kingdom)) {
        newRecipients.add(p);
      }
    }
    TheNewKings.instance().saveManager().close();
    return newRecipients;
  }
}