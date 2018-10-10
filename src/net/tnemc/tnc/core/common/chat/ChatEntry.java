package net.tnemc.tnc.core.common.chat;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class ChatEntry {
  private String handler;
  private String type;
  private String[] commands = new String[0];
  private String permission = "";
  private boolean world = false;
  private boolean radial = false;
  private int radius = 20;
  private String format = "";

  public ChatEntry(String handler, String type) {
    this.handler = handler;
    this.type = type;
  }

  public String getHandler() {
    return handler;
  }

  public void setHandler(String handler) {
    this.handler = handler;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String[] getCommands() {
    return commands;
  }

  public void setCommands(String[] commands) {
    this.commands = commands;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public boolean isWorld() {
    return world;
  }

  public void setWorld(boolean world) {
    this.world = world;
  }

  public boolean isRadial() {
    return radial;
  }

  public void setRadial(boolean radial) {
    this.radial = radial;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}