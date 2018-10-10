package net.tnemc.tnc.core.common.chat.handlers.towny;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.tnemc.tnc.core.common.chat.ChatType;
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
public class AllyType extends ChatType {
  public AllyType() {
    super("ally", "<gray>[<aqua>$nation<gray>]: <white>$message");
  }

  @Override
  public Collection<Player> getRecipients(Collection<Player> recipients, Player player) {
    try {
      final Nation nation = TownyUniverse.getDataSource().getResident(player.getName()).getTown().getNation();

      Collection<Player> newRecipients = new HashSet<>();

      for(Player p : recipients) {
        if(TownyUniverse.getDataSource().getResident(p.getName()).getTown().getNation().getUuid().equals(nation.getUuid())
            || TownyUniverse.getDataSource().getResident(p.getName()).getTown().getNation().hasAlly(nation)) {
          newRecipients.add(p);
        }
      }
      return newRecipients;
    } catch(NotRegisteredException e) {
      e.printStackTrace();
    }
    return recipients;
  }
}