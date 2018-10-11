package net.tnemc.tnc.core.common.api;

import net.tnemc.tnc.core.TheNewChat;
import net.tnemc.tnc.core.common.chat.ChatEntry;
import net.tnemc.tnc.core.common.chat.ChatHandler;
import net.tnemc.tnc.core.common.chat.ChatType;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class TNCAPI {

  /**
   * Used to add a new {@link ChatHandler handler}.
   * @param handler The handler to add.
   */
  public static void addHandler(final ChatHandler handler) {
    TheNewChat.instance().getManager().addHandler(handler);
  }

  /**
   * Used to add a new {@link ChatType type} to an {@link ChatHandler handler}.
   * @param handler The name of the handler this type should be added to.
   * @param type The {@link ChatType} to add.
   * @return True if the {@link ChatType type} was added, otherwise false.
   */
  public static boolean addType(final String handler, final ChatType type) {
    if(TheNewChat.instance().getManager().getHandlers().containsKey(handler)) {
      TheNewChat.instance().getManager().getHandlers().get(handler).addType(type);
      return true;
    }
    return false;
  }

  /**
   * Used to add a new {@link ChatEntry}.
   * @param entry The {@link ChatEntry entry} to add.
   * @return True if the {@link ChatEntry entry} was added, otherwise false.
   */
  public static boolean addChat(final ChatEntry entry) {
    if(TheNewChat.instance().getManager().getHandlers().containsKey(entry.getHandler().toLowerCase()) &&
        TheNewChat.instance().getManager().getHandlers().get(entry.getHandler()).getType(entry.getType().toLowerCase()) != null) {
      TheNewChat.instance().getManager().addChatEntry(entry);
      TheNewChat.instance().registerChatCommand();
      return true;
    }
    return false;
  }
}