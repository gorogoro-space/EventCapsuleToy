package space.gorogoro.eventcapsluetoy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
 * EventCapslueToyCommand
 * @license    GPLv3
 * @copyright  Copyright gorogoro.space 2021
 * @author     kubotan
 * @see        <a href="https://gorogoro.space">gorogoro.space</a>
 */
public class EventCapslueToyCommand {
  private EventCapslueToy capsluetoy;
  private CommandSender sender;
  private String[] args;
  protected static final String META_CHEST = "eventcapsluetoy.chest";
  protected static final String FORMAT_TICKET_CODE = "EVENT CODE:%s";
  protected static final String EVENT_CODE = "fu0l67MRjB";

  /**
   * Constructor of CapslueToyCommand.
   * @param EventCapslueToy CapslueToy
   */
  public EventCapslueToyCommand(EventCapslueToy capsluetoy) {
    try{
      this.capsluetoy = capsluetoy;
    } catch (Exception e){
      EventCapslueToyUtility.logStackTrace(e);
    }
  }

  /**
   * Initialize
   * @param CommandSender CommandSender
   * @param String[] Argument
   */
  public void initialize(CommandSender sender, String[] args){
    try{
      this.sender = sender;
      this.args = args;
    } catch (Exception e){
      EventCapslueToyUtility.logStackTrace(e);
    }
  }

  /**
   * Finalize
   */
  public void finalize() {
    try{
      this.sender = null;
      this.args = null;
    } catch (Exception e){
      EventCapslueToyUtility.logStackTrace(e);
    }
  }

  /**
   * Processing of command list.
   * @return boolean true:Success false:Failure
   */
  public boolean list() {
    List<String> glist = capsluetoy.getDatabase().list();
    if(glist.size() <= 0) {
      EventCapslueToyUtility.sendMessage(sender, "Record not found.");
      return true;
    }

    for(String msg: glist) {
      EventCapslueToyUtility.sendMessage(sender, msg);
    }
    return true;
  }

  /**
   * Processing of command modify.
   * @return boolean true:Success false:Failure
   */
  public boolean modify() {
    if(args.length != 2) {
      return false;
    }

    if(!(sender instanceof Player)) {
      return false;
    }

    String capslueToyName = args[1];
    if(capsluetoy.getDatabase().getCapslueToy(capslueToyName) == null) {
      EventCapslueToyUtility.sendMessage(sender, "Record not found. capsluetoy_name=" + capslueToyName);
      return true;
    }
    EventCapslueToyUtility.setPunch((Player)sender, capsluetoy, capslueToyName);
    EventCapslueToyUtility.sendMessage(sender, "Please punching(right click) a chest of CapslueToy. capsluetoy_name=" + capslueToyName);
    return true;
  }

  /**
   * Processing of command delete.
   * @return boolean true:Success false:Failure
   */
  public boolean delete() {
    if(args.length != 2) {
      return false;
    }

    String capslueToyName = args[1];
    if(capsluetoy.getDatabase().deleteCapslueToy(capslueToyName)) {
      EventCapslueToyUtility.sendMessage(sender, "Deleted. capsluetoy_name=" + capslueToyName);
      return true;
    }
    return false;
  }

  /**
   * Processing of command ticket.
   * @return boolean true:Success false:Failure
   */
  public boolean ticket(Player p) {
    if(args.length != 2) {
      return false;
    }

    int emptySlot = p.getInventory().firstEmpty();
    if (emptySlot == -1) {
      // not empty
      return false;
    }

    ItemStack ticket = new ItemStack(Material.PAPER, 1);
    ItemMeta im = ticket.getItemMeta();
    im.setDisplayName(ChatColor.translateAlternateColorCodes('&', capsluetoy.getConfig().getString("ticket-display-name")));
    ArrayList<String> lore = new ArrayList<String>();
    lore.add(ChatColor.translateAlternateColorCodes('&', capsluetoy.getConfig().getString("ticket-lore1")));
    lore.add(ChatColor.translateAlternateColorCodes('&', capsluetoy.getConfig().getString("ticket-lore2")));
    lore.add("スクラッチ:  " + ChatColor.RESET + ChatColor.MAGIC + EVENT_CODE + ChatColor.RESET);
    im.setLore(lore);
    ticket.setItemMeta(im);
    p.getInventory().setItem(emptySlot, ticket);

    EventCapslueToyUtility.sendMessage(sender, "Issue a ticket. player_name=" +  p.getDisplayName());
    return true;
  }

  /**
   * Processing of command reload.
   * @return boolean true:Success false:Failure
   */
  public boolean reload() {
    capsluetoy.reloadConfig();
    EventCapslueToyUtility.sendMessage(sender, "reloaded.");
    return true;
  }

  /**
   * Processing of command enable.
   * @return boolean true:Success false:Failure
   */
  public boolean enable() {
    capsluetoy.onEnable();
    EventCapslueToyUtility.sendMessage(sender, "enabled.");
    return true;
  }

  /**
   * Processing of command fgdisable.
   * @return boolean true:Success false:Failure
   */
  public boolean disable() {
    capsluetoy.onDisable();
    EventCapslueToyUtility.sendMessage(sender, "disabled.");
    return true;
  }
}
