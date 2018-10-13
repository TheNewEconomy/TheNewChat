package net.tnemc.tnc.core.common.chat.handlers;

import net.tnemc.tnc.core.common.chat.ChatHandler;
import net.tnemc.tnc.core.common.chat.checks.towny.KingCheck;
import net.tnemc.tnc.core.common.chat.checks.towny.MayorCheck;
import net.tnemc.tnc.core.common.chat.handlers.towny.AllyType;
import net.tnemc.tnc.core.common.chat.handlers.towny.NationType;
import net.tnemc.tnc.core.common.chat.handlers.towny.TownType;
import net.tnemc.tnc.core.common.chat.variables.towny.NationVariable;
import net.tnemc.tnc.core.common.chat.variables.towny.TitleVariable;
import net.tnemc.tnc.core.common.chat.variables.towny.TownVariable;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class TownyHandler extends ChatHandler {

  public TownyHandler() {

    addType(new AllyType());
    addType(new NationType());
    addType(new TownType());

    addVariable(new NationVariable());
    addVariable(new TownVariable());
    addVariable(new TitleVariable());

    addCheck(new KingCheck());
    addCheck(new MayorCheck());
  }

  @Override
  public String getName() {
    return "towny";
  }
}