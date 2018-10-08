package net.tnemc.tnc.core.common.configuration;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public enum CommonNodes implements IConfigNode {
  VERSION_HEADER {
    @Override
    public String getNode() {
      return "version";
    }
  },

  VERSION {
    @Override
    public String getNode() {
      return "version.version";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "This is the current version of The New Chat.  Please do not edit."
      };
    }
  },

  LAST_RUN_VERSION {
    @Override
    public String getNode() {
      return "version.last_run_version";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Please do not edit."
      };
    }
  },;

  @Override
  public String getFile() {
    return "config.yml";
  }
}
