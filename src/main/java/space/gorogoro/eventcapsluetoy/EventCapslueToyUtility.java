package space.gorogoro.eventcapsluetoy;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/*
 * EventCapslueToyUtility
 * @license    GPLv3
 * @copyright  Copyright gorogoro.space 2021
 * @author     kubotan
 * @see        <a href="https://gorogoro.space">gorogoro.space</a>
 */
public class EventCapslueToyUtility {

  protected static final String NUMALPHA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /**
   * Output stack trace to log file.
   * @param Exception Exception
   */
  public static void logStackTrace(Exception e){
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    pw.flush();
    Bukkit.getLogger().log(Level.WARNING, sw.toString());
  }

  /**
   * Determine whether punch is being processed.
   * @param Player Player
   * @return boolean true:That's right false:That's not it
   */
  public static boolean isInPunch(Player player){
    if( player.hasMetadata(EventCapslueToyCommand.META_CHEST)){
      return true;
    }
    return false;
  }

  /**
   * get CapslueToy name in punch.
   * @param Player Player
   * @return String CapslueToy name
   */
  public static String getcapslueToyNameInPunch(Player player){
    for(MetadataValue mv:player.getMetadata(EventCapslueToyCommand.META_CHEST)) {
      return mv.asString();
    }
    return null;
  }

  /**
   * Set punch processing.
   * @param Player Player
   * @param EventCapslueToy capsluetoy
   * @param String CapslueToy Name
   */
  public static void setPunch(Player player, EventCapslueToy capsluetoy, String capslueToyName){
    removePunch(player, capsluetoy);
    player.setMetadata(EventCapslueToyCommand.META_CHEST, new FixedMetadataValue(capsluetoy, capslueToyName));
  }

  /**
   * Remove punch processing.
   * @param Player Player
   * @param EventCapslueToy capsluetoy
   */
  public static void removePunch(Player player, EventCapslueToy capsluetoy){
    player.removeMetadata(EventCapslueToyCommand.META_CHEST, capsluetoy);
  }

  /**
   * Send message to player
   * @param CommandSender CommandSender
   * @param String message
   */
  public static void sendMessage(CommandSender sender, String message){
    sender.sendMessage((Object)ChatColor.DARK_RED + "[EventCapslueToy]" + " " + (Object)ChatColor.RED + message);
  }

  /**
   * Generate random code
   * @return String code
   */
  public static String generateCode(){
    return String.format("%s-%s-%s",
      randomChars(4),
      randomChars(6),
      randomChars(4)
    );
  }

  /**
   * Random Chars
   * @param int Length
   * @return String value
   */
  public static String randomChars(int length) {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    String chars = NUMALPHA;
    for (int i = 0; i < length; i++) {
      sb.append(chars.charAt(random.nextInt(chars.length())));
    }
    return sb.toString();
  }

  public static ArrayList<Player> getTarget(Plugin capsluetoy, String selector) {
    return getTarget(capsluetoy, selector, null);
  }

  public static ArrayList<Player> getTarget(Plugin capsluetoy, String selector, CommandSender sender) {

    ArrayList<Player> list = new ArrayList<Player>();
    if(!selector.startsWith("@")) {
      Player p = capsluetoy.getServer().getPlayer(selector);
      if(p != null) {
        list.add(p);
      }
    } else {
      switch(selector) {
        case "@a":
          list.addAll(capsluetoy.getServer().getOnlinePlayers());
          break;

        case "@p":
          if((sender instanceof Player) || (sender instanceof BlockCommandSender)) {
            Location senderLocation = null;
            if(sender instanceof Player) {
              Player sp = (Player) sender;
              senderLocation = sp.getLocation();
            } else if(sender instanceof BlockCommandSender) {
              BlockCommandSender sb = (BlockCommandSender) sender;
              senderLocation = sb.getBlock().getLocation();
            }
            if(senderLocation != null) {
              list.add(getNearestPlayerByLocation(senderLocation));
            }
          }
          break;

        case "@s":
          if(sender instanceof Player) {
            list.add((Player) sender);
          }
          break;

        case "@r":
          ArrayList<Player> curPlayerList = new ArrayList<Player>();
          for (Player p : Bukkit.getOnlinePlayers()) {
            curPlayerList.add(p);
          }
          Random random = new Random();
          list.add(curPlayerList.get(random.nextInt(curPlayerList.size())));
          break;
      }
    }
    return list;
  }

  public static Player getNearestPlayerByLocation(Location l) {
    double lastDistance = Double.MAX_VALUE;
    Player latestPlayer = null;
    for(Player p : l.getWorld().getPlayers()) {
      double distance = p.getLocation().distance(l);
      if(distance < lastDistance) {
          lastDistance = distance;
          latestPlayer = p;
      }
    }
    return latestPlayer;
  }
}
