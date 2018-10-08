package net.tnemc.tnc.core.common.chat.variables.towny;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.tnemc.tnc.core.common.chat.ChatVariable;
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
public class TitleVariable extends ChatVariable {
  @Override
  public String name() {
    return "$title";
  }

  @Override
  public String parse(Player player, String message) {
    try {
      return TownyUniverse.getDataSource().getResident(player.getName()).getTitle();
    } catch(NotRegisteredException e) {
      e.printStackTrace();
    }
    return "<red>ERROR";
  }
}