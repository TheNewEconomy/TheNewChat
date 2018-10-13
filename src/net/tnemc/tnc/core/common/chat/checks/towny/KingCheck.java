package net.tnemc.tnc.core.common.chat.checks.towny;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.tnemc.tnc.core.common.chat.ChatCheck;
import org.bukkit.entity.Player;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class KingCheck extends ChatCheck {
  @Override
  public String name() {
    return "isking";
  }

  @Override
  public boolean runCheck(Player player, String checkString) {
    try {
      if(TownyUniverse.getDataSource().getResident(player.getName()).hasNation()) {
        return TownyUniverse.getDataSource().getResident(player.getName()).getTown().getNation().isKing(TownyUniverse.getDataSource().getResident(player.getName()));
      }
    } catch(NotRegisteredException ignore) {
    }
    return false;
  }
}